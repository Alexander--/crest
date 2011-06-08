/*
 * Copyright 2010 CodeGist.org
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *        http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 *
 * ===================================================================
 *
 * More information at http://www.codegist.org.
 */

package org.codegist.crest;

import org.codegist.common.lang.Disposable;
import org.codegist.common.lang.Disposables;
import org.codegist.common.reflect.ObjectMethodsAwareInvocationHandler;
import org.codegist.crest.config.ConfigFactoryException;
import org.codegist.crest.config.InterfaceConfig;
import org.codegist.crest.config.MethodConfig;
import org.codegist.crest.config.ParamConfig;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.http.HttpException;
import org.codegist.crest.http.HttpRequest;
import org.codegist.crest.http.HttpRequestExecutor;
import org.codegist.crest.http.HttpResponse;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.DeserializationManager;

import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.lang.reflect.Method;
import java.net.URISyntaxException;

/**
 * Default CRest implementation based on {@link org.codegist.crest.CRestContext} interface data model.
 * <p>On top of the behavior described in {@link org.codegist.crest.CRest}, this implementation adds :
 * <p>- {@link org.codegist.crest.interceptor.RequestInterceptor} to intercept any requests before it gets fired.
 * <p>- {@link org.codegist.crest.serializer.Serializer} to customize the serialization process of any types.
 * <p>- {@link org.codegist.crest.injector.Injector} to inject complexe types that can't be reduced to a String via the serializers.
 * <p>- {@link org.codegist.crest.handler.ResponseHandler} to customize response handling when interface method's response type is not one of raw types.
 * <p>- {@link org.codegist.crest.handler.ErrorHandler} to customize how the created interface behaves when any error occurs during the method call process.
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class DefaultCRest implements CRest, Disposable {

    private final CRestContext context;

    /**
     * @param context The CRest configuration holder
     */
    public DefaultCRest(CRestContext context) {
        this.context = context;   
    }

    /**
     * @inheritDoc
     */
    @SuppressWarnings("unchecked")
    public <T> T build(Class<T> interfaze) throws CRestException {
        try {
            return (T) context.getProxyFactory().createProxy(interfaze.getClassLoader(), new RestInterfacer(interfaze), new Class[]{interfaze});
        } catch (Exception e) {
            return CRestException.<T>doThrow(e);
        }
    }

    class RestInterfacer<T> extends ObjectMethodsAwareInvocationHandler {

        private final InterfaceConfig interfaceConfig;
        private final DeserializationManager deserializationManager;

        private RestInterfacer(Class<T> interfaze) throws ConfigFactoryException {
            this.interfaceConfig = context.getConfigFactory().newConfig(interfaze, context);
            this.deserializationManager = (DeserializationManager) context.getProperties().get(DeserializationManager.class.getName());
        }

        @Override
        protected Object doInvoke(Object proxy, Method method, Object[] args) throws Throwable {
            try {
                return doInvoke(method, args);
            } catch (Throwable e) {
                return CRestException.doThrow(e);
            }
        }

        private Object doInvoke(Method method, Object[] args) throws Throwable {
            MethodConfig mc = interfaceConfig.getMethodConfig(method);
            RequestContext requestContext = new DefaultRequestContext(interfaceConfig, method, args);

            int attemptCount = 0;
            ResponseContext responseContext;
            Exception exception;
            RetryHandler retryHandler = mc.getRetryHandler();
            HttpRequestExecutor httpRequestExecutor = context.getHttpRequestExecutor();
            do {
                exception = null;
                // build the request, can throw exception but that should not be part of the retry policy
                HttpRequest request = buildRequest(requestContext);
                try {
                    // doInvoke the request
                    HttpResponse response = httpRequestExecutor.execute(request);
                    // wrap the response in response context
                    responseContext = new DefaultResponseContext(deserializationManager, requestContext, response);
                } catch (HttpException e) {
                    responseContext = new DefaultResponseContext(deserializationManager, requestContext, e.getResponse());
                    exception = e;
                } catch (RuntimeException e) {
                    responseContext = new DefaultResponseContext(deserializationManager, requestContext, null);
                    exception = e;
                }
                // loop until an exception has been thrown and the retry handle ask for retry
            }while(exception != null && retryHandler.retry(responseContext, exception, ++attemptCount));

            if (exception != null) {
                // An exception has been thrown during request execution, invoke the error handler and return
                return mc.getErrorHandler().handle(responseContext, exception);
            }else{
                // all good, handle the response
                return handle(responseContext);
            }
        }

        /**
         * Response handling base implementation, returns raw response if InputStream or Reader is the requested return type.
         * <p>Otherwise delegate response handling to the given response handler.
         * @param responseContext current response context
         * @return response
         */
        private Object handle(ResponseContext responseContext) throws IOException {
            boolean closeResponse = false;
            MethodConfig mc = responseContext.getRequestContext().getMethodConfig();
            HttpResponse response = responseContext.getResponse();
            Class<?> returnTypeClass = mc.getMethod().getReturnType();
            try {
                if (returnTypeClass.equals(InputStream.class)) {
                    // If InputStream return type, then return raw response ()
                    return response.asStream();
                } else if (returnTypeClass.equals(Reader.class)) {
                    // If Reader return type, then return raw response
                    return response.asReader();
                } else {
                    // otherwise, delegate to response handler
                    return mc.getResponseHandler().handle(responseContext);
                }
            } catch (RuntimeException e) {
                closeResponse = true;
                throw e;
            } catch (IOException e) {
                closeResponse = true;
                throw e;
            } finally {
                if (closeResponse && response != null) {
                    response.close();
                }
            }
        }

        /**
         *
         * @param requestContext
         * @return
         * @throws URISyntaxException
         */
        private HttpRequest buildRequest(RequestContext requestContext) throws Exception {
            MethodConfig mc = requestContext.getMethodConfig();
            RequestInterceptor gi = interfaceConfig.getGlobalInterceptor();
            RequestInterceptor ri = mc.getRequestInterceptor();
            String encoding = interfaceConfig.getEncoding();

            // Build base request
            HttpRequest.Builder builder = new HttpRequest.Builder(mc.getPathTemplate(), mc.getBodyWriter(), interfaceConfig.getEncoding())
                    .within(requestContext)
                    .using(mc.getHttpMethod())
                    .timeoutSocketAfter(mc.getSocketTimeout())
                    .timeoutConnectionAfter(mc.getConnectionTimeout());


            // Notify injectors (Global and method) before param injection
            gi.beforeParamsInjectionHandle(builder, requestContext);
            ri.beforeParamsInjectionHandle(builder, requestContext);

            // Add default params
            for(ParamConfig p : mc.getExtraParams()){
                builder.addParam(p);
            }

            for (int i = 0; i < mc.getParamCount(); i++) {
                builder.addParam(mc.getParamConfig(i), requestContext.getValue(i));
            }

            // Notify injectors (Global and method after param injection
            ri.afterParamsInjectionHandle(builder, requestContext);
            gi.afterParamsInjectionHandle(builder, requestContext);

            return builder.build();
        }
    }


    public void dispose() {
        Disposables.dispose(context.getHttpRequestExecutor());
    }
}
