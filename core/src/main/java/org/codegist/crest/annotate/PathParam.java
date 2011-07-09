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
 *  ==================================================================
 *
 *  More information at http://www.codegist.org.
 */

package org.codegist.crest.annotate;

import org.codegist.crest.io.http.HttpRequest;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * <p>If specified at method parameter level, replace any method path template matching its name with it's value.
 * <p>If specified at interface or method level, replace any method path template matching its name with it's default value.
 * @author laurent.gilles@codegist.org
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE,ElementType.METHOD, ElementType.PARAMETER})
@Param(HttpRequest.DEST_PATH)
public @interface PathParam {

    /**
     * Indicates the template name to use
     * @return parameter name
     */
    String value();

    /**
     * Indicates the parameter default value to use.
     * <p>At method parameter level, this value is used if the parameter is null
     * <p>At interface/method levels, this value is used to specifie the value of the parameter to add for each io
     * @return parameter default value
     */
    String defaultValue() default "";
}
