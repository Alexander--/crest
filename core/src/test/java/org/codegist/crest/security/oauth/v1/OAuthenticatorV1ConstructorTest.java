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

package org.codegist.crest.security.oauth.v1;

import org.codegist.crest.security.oauth.OAuthToken;
import org.junit.Test;

import java.lang.reflect.Field;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

/**
 * @author Laurent Gilles (laurent.gilles@codegist.org)
 */
public class OAuthenticatorV1ConstructorTest {

    @Test
    public void shouldUseDefaultVariantProviderInstance() throws Exception {
        OAuthenticatorV1 toTest = new OAuthenticatorV1(mock(OAuthToken.class));
        Field variantProvider = OAuthenticatorV1.class.getDeclaredField("variantProvider");
        variantProvider.setAccessible(true);
        assertEquals(DefaultVariantProvider.INSTANCE, variantProvider.get(toTest));
    }

}