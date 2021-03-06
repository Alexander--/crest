/*
 * Copyright 2011 CodeGist.org
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

package org.codegist.crest.server.stubs.params.forms;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import static org.codegist.crest.server.utils.ToStrings.string;

/**
 * @author laurent.gilles@codegist.org
 */
@Produces("text/html;charset=UTF-8")
@Path("params/form/collection")
public class CollectionsStub {


    @POST
    @Path("default")
    public String defaults(
            @FormParam("p1") List<String> p1,
            @FormParam("p2") List<String> p2,
            @FormParam("p3") List<Integer> p3,
            @FormParam("p4") List<Long> p4) {
        return String.format("default() p1=%s p2=%s p3=%s p4=%s", string(p1), string(p2), string(p3), string(p4));
    }

    @POST
    @Path("merging")
    public String merging(
            @FormParam("p1") String p1,
            @FormParam("p2") String p2,
            @FormParam("p3") String p3,
            @FormParam("p4") String p4) {
        return String.format("merging() p1=%s p2=%s p3=%s p4=%s", p1, p2, p3, p4);
    }
}
