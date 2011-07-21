/*
 * Copyright 2010 CodeGist.org
 *
 *     Licensed under the Apache License, Version 2.0 (the "License");
 *     you may not use this file except in compliance with the License.
 *     You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 *     Unless required by applicable law or agreed to in writing, software
 *     distributed under the License is distributed on an "AS IS" BASIS,
 *     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *     See the License for the specific language governing permissions and
 *     limitations under the License.
 *
 *  ===================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.config;

import org.codegist.crest.entity.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;
import org.codegist.crest.serializer.Serializer;

import java.lang.reflect.Method;

/**
 * @author laurent.gilles@codegist.org
 */
public interface InterfaceConfigBuilder {
    
    InterfaceConfig build() throws Exception;

    MethodConfigBuilder startMethodConfig(Method meth);

    InterfaceConfigBuilder setMethodsCharset(String charset);

    InterfaceConfigBuilder setMethodsSocketTimeout(int socketTimeout);

    InterfaceConfigBuilder setMethodsConnectionTimeout(int connectionTimeout);

    InterfaceConfigBuilder setMethodsRequestInterceptor(Class<? extends RequestInterceptor> requestInterceptorCls);

    InterfaceConfigBuilder setMethodsResponseHandler(Class<? extends ResponseHandler> responseHandlerClass);

    InterfaceConfigBuilder setMethodsErrorHandler(Class<? extends ErrorHandler> errorHandler);

    InterfaceConfigBuilder setMethodsRetryHandler(Class<? extends RetryHandler> retryHandler);

    InterfaceConfigBuilder setMethodsEntityWriter(Class<? extends EntityWriter> bodyWriter);

    InterfaceConfigBuilder setMethodsConsumes(String... mimeType);

    InterfaceConfigBuilder setMethodsDeserializer(Class<? extends Deserializer> deserializer);

    InterfaceConfigBuilder setMethodsProduces(String contentType);

    InterfaceConfigBuilder setMethodsType(MethodType meth);

    InterfaceConfigBuilder appendMethodsPath(String path);

    InterfaceConfigBuilder setMethodsEndPoint(String endPoint);

    ParamConfigBuilder startMethodsExtraParamConfig();

    InterfaceConfigBuilder setParamsSerializer(Class<? extends Serializer> paramSerializerCls);

    InterfaceConfigBuilder setParamsEncoded(boolean encoded);

    InterfaceConfigBuilder setParamsListSeparator(String separator);
}
