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

package org.codegist.crest.request.crest;

import org.codegist.crest.BaseCRestTest;
import org.junit.Test;
import org.junit.runners.Parameterized;

import java.util.Collection;

import static org.codegist.crest.request.common.CommonRequestsTest.*;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class HeadsTest extends BaseCRestTest<Heads> {

    public HeadsTest(CRestHolder crest) {
        super(crest, Heads.class);
    }

    @Parameterized.Parameters
    public static Collection<CRestHolder[]> getData() {
        return crest(byRestServicesAndCustomContentTypes());
    }

    @Test
    public void testRaw() {
        toTest.raw();
        String actual = toTest.last();
        assertRaw(actual);
    }

    @Test
    public void testAccept() {
        toTest.accept();
        String actual = toTest.last();
        assertAccept(actual);
    }

    @Test
    public void testContentType() {
        toTest.contentType();
        String actual = toTest.last();
        assertContentType(actual);
    }

}
