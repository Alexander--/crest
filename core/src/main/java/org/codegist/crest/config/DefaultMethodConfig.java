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

package org.codegist.crest.config;

import org.codegist.common.collect.Maps;
import org.codegist.common.lang.ToStringBuilder;
import org.codegist.common.reflect.Annotations;
import org.codegist.common.reflect.Methods;
import org.codegist.crest.EntityWriter;
import org.codegist.crest.handler.ErrorHandler;
import org.codegist.crest.handler.ResponseHandler;
import org.codegist.crest.handler.RetryHandler;
import org.codegist.crest.interceptor.RequestInterceptor;
import org.codegist.crest.serializer.Deserializer;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Map;

/**
 * Default immutable in-memory implementation of {@link org.codegist.crest.config.MethodConfig}
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
class DefaultMethodConfig implements MethodConfig {

    private final Method method;
    private final PathTemplate path;
    private final String httpMethod;
    private final Long socketTimeout;
    private final Long connectionTimeout;
    private final EntityWriter entityWriter;
    private final RequestInterceptor requestInterceptor;
    private final ResponseHandler responseHandler;
    private final ErrorHandler errorHandler;
    private final RetryHandler retryHandler;
    private final Deserializer[] deserializers;
    private final ParamConfig[] extraParams;
    private final MethodParamConfig[] methodParamConfigs;
    private final Map<Class<? extends Annotation>, Annotation> annotations;

    DefaultMethodConfig(Method method, PathTemplate path, String httpMethod, Long socketTimeout, Long connectionTimeout, EntityWriter entityWriter, RequestInterceptor requestInterceptor, ResponseHandler responseHandler, ErrorHandler errorHandler, RetryHandler retryHandler, Deserializer[] deserializers, MethodParamConfig[] methodParamConfigs, ParamConfig[] extraParams) {
        this.method = method;
        this.path = path;
        this.httpMethod = httpMethod;
        this.socketTimeout = socketTimeout;
        this.connectionTimeout = connectionTimeout;
        this.entityWriter = entityWriter;
        this.requestInterceptor = requestInterceptor;
        this.responseHandler = responseHandler;
        this.errorHandler = errorHandler;
        this.retryHandler = retryHandler;
        this.deserializers = deserializers != null ? deserializers.clone() : null;
        this.methodParamConfigs = methodParamConfigs != null ? methodParamConfigs.clone() : null;
        this.extraParams = extraParams != null ? extraParams.clone() : null;
        this.annotations = method != null ? Maps.unmodifiable(Annotations.toMap(method.getAnnotations())) : null;
    }

    public PathTemplate getPathTemplate() {
        return path;
    }

    public ResponseHandler getResponseHandler() {
        return responseHandler;
    }

    public Method getMethod() {
        return method;
    }

    public String getHttpMethod() {
        return httpMethod;
    }

    public Long getSocketTimeout() {
        return socketTimeout;
    }

    public Long getConnectionTimeout() {
        return connectionTimeout;
    }

    public EntityWriter getBodyWriter() {
        return entityWriter;
    }

    public RequestInterceptor getRequestInterceptor() {
        return requestInterceptor;
    }

    public ErrorHandler getErrorHandler() {
        return errorHandler;
    }

    public RetryHandler getRetryHandler() {
        return retryHandler;
    }

    public Deserializer[] getDeserializers() {
        return deserializers;
    }

    public MethodParamConfig getParamConfig(int index) {
        return methodParamConfigs != null && index < methodParamConfigs.length ? methodParamConfigs[index] : null;
    }

    public Integer getParamCount() {
        return methodParamConfigs != null ? methodParamConfigs.length : null;
    }

    public ParamConfig[] getExtraParams() {
        return extraParams != null ? extraParams.clone() : null;
    }

    public Map<Class<? extends Annotation>, Annotation> getAnnotations() {
        return this.annotations;
    }

    public String toString() {
        return new ToStringBuilder(this)
                .append("path", path)
                .append("method", method)
                .append("httpMethod", httpMethod)
                .append("socketTimeout", socketTimeout)
                .append("connectionTimeout", connectionTimeout)
                .append("entityWriter", entityWriter)
                .append("requestInterceptor", requestInterceptor)
                .append("responseHandler", responseHandler)
                .append("deserializers", deserializers)
                .append("errorHandler", errorHandler)
                .append("retryHandler", retryHandler)
                .append("methodParamConfigs", methodParamConfigs)
                .toString();
    }
}
