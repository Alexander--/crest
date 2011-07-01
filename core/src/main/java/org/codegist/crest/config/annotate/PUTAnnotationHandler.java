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

package org.codegist.crest.config.annotate;

import org.codegist.crest.annotate.HttpMethod;
import org.codegist.crest.annotate.PUT;
import org.codegist.crest.config.InterfaceConfigBuilder;
import org.codegist.crest.config.MethodConfigBuilder;

/**
 * @author laurent.gilles@codegist.org
 */
class PUTAnnotationHandler extends NoOpAnnotationHandler<PUT> {

    @Override
    public void handleInterfaceAnnotation(PUT annotation, InterfaceConfigBuilder builder) {
        builder.setMethodsHttpMethod(org.codegist.crest.http.HttpMethod.valueOf(annotation.annotationType().getAnnotation(HttpMethod.class).value()));
    }

    @Override
    public void handleMethodAnnotation(PUT annotation, MethodConfigBuilder builder) {
        builder.setHttpMethod(org.codegist.crest.http.HttpMethod.valueOf(annotation.annotationType().getAnnotation(HttpMethod.class).value()));
    }

}
