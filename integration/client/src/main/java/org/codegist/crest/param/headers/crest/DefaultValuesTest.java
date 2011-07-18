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

package org.codegist.crest.param.headers.crest;

import org.codegist.crest.annotate.*;
import org.codegist.crest.annotate.GET;
import org.codegist.crest.annotate.HeaderParam;
import org.codegist.crest.annotate.HeaderParams;
import org.codegist.crest.param.headers.common.IDefaultValuesTest;

/**
 * @author laurent.gilles@codegist.org
 */
public class DefaultValuesTest extends IDefaultValuesTest<DefaultValuesTest.DefaultValues> {

    public DefaultValuesTest(CRestHolder crest) {
        super(crest, DefaultValues.class);
    }

    @EndPoint("{crest.server.end-point}")
    @Path("params/header/default-value")
    @GET
    public static interface DefaultValues extends IDefaultValuesTest.IDefaultValues {

        @Path("value")
        String value(
                @HeaderParam(value = "p1", defaultValue = "default-p1") String p1,
                @HeaderParam(value = "p2", defaultValue = "123") Integer p2);

        @HeaderParam(value = "p2", defaultValue = "p2-val")
        @HeaderParams({
                @HeaderParam(value = "p1", defaultValue = "p1-val"),
                @HeaderParam(value = "p3", defaultValue = "p3-val")
        })
        @Path("param")
        String param(@HeaderParam("p1") String p1);

    }
}
