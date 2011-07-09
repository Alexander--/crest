///*
// * Copyright 2010 CodeGist.org
// *
// * Licensed under the Apache License, Version 2.0 (the "License");
// * you may not use this file except in compliance with the License.
// * You may obtain a copy of the License at
// *
// * http://www.apache.org/licenses/LICENSE-2.0
// *
// * Unless required by applicable law or agreed to in writing, software
// * distributed under the License is distributed on an "AS IS" BASIS,
// * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// * See the License for the specific language governing permissions and
// * limitations under the License.
// *
// * ===================================================================
// *
// * More information at http://www.codegist.org.
// */
//
//package org.codegist.crest;
//
//import org.junit.Test;
//
//import java.io.UnsupportedEncodingException;
//import java.net.MalformedURLException;
//import java.net.URI;
//import java.net.URISyntaxException;
//import java.net.URL;
//import java.nio.charset.Charset;
//import java.util.Collections;
//import java.util.LinkedHashMap;
//
//import static org.junit.Assert.*;
//
///**
// * @author Laurent Gilles (laurent.gilles@codegist.org)
// */
//public class HttpRequestBuilderTest {
//
//
//    @Test(expected = IllegalArgumentException.class)
//    public void testQueryString() throws URISyntaxException {
//        new HttpRequest.Builder("http://test?test=test");
//    }
//    @Test
//    public void testTimeouts() throws URISyntaxException {
//        HttpRequest.Builder io = new HttpRequest.Builder("http://test").timeoutAfter(100l);
//        assertEquals(Long.valueOf(100l), io.getSocketTimeout());
//        assertEquals(Long.valueOf(100l), io.getConnectionTimeout());
//    }
//
//    @Test
//    public void testEncoding() throws URISyntaxException {
//        assertEquals("ISO-8859-1", new HttpRequest.Builder("http://test", "ISO-8859-1").getEncoding());
//        assertEquals("utf-8", new HttpRequest.Builder("http://test").getEncoding());
//        assertEquals("ISO-8859-1", new HttpRequest.Builder("http://test").pointsTo("http://test", "ISO-8859-1").getEncoding());
//    }
//
//    @Test
//    public void testMeth() throws URISyntaxException {
//        HttpRequest.Builder io = new HttpRequest.Builder("http://test").using("PUT");
//        assertEquals("PUT", io.getMeth());
//    }
//
//    @Test
//    public void testParams() throws URISyntaxException {
//        HttpRequest.Builder io = new HttpRequest.Builder("http://test")
//                .addFormParams(new LinkedHashMap<String, Object>() {{
//                    put("dropped1", "value2");
//                    put("dropped2", "value3");
//                }})
//                .addFormParam("dropped3", "value4")
//                .setFormParams(new LinkedHashMap<String, Object>() {{
//                    put("f1", "vf1");
//                }})
//                .addFormParams(new LinkedHashMap<String, Object>() {{
//                    put("f2", "vf2");
//                    put("f3", "vf3");
//                }})
//                .addFormParam("f4", "vf4")
//                .addParam("f5", "vf5", HttpRequest.DEST_FORM)
//
//                .addQueryParams(new LinkedHashMap<String, String>() {{
//                    put("dropped1", "value2");
//                    put("dropped2", "value3");
//                }})
//                .addQueryParam("dropped3", "value4")
//                .setQueryParams(new LinkedHashMap<String, String>() {{
//                    put("f1", "vf1");
//                }})
//                .addQueryParams(new LinkedHashMap<String, String>() {{
//                    put("f2", "vf2");
//                    put("f3", "vf3");
//                }})
//                .addQueryParam("f4", "vf4")
//                .addParam("f5", "vf5", HttpRequest.DEST_QUERY)
//
//                .addPathParams(new LinkedHashMap<String, String>() {{
//                    put("dropped1", "value2");
//                    put("dropped2", "value3");
//                }})
//                .addPathParam("dropped3", "value4")
//                .setPathParams(new LinkedHashMap<String, String>() {{
//                    put("f1", "vf1");
//                }})
//                .addPathParams(new LinkedHashMap<String, String>() {{
//                    put("f2", "vf2");
//                    put("f3", "vf3");
//                }})
//                .addPathParam("f4", "vf4")
//                .addParam("f5", "vf5", HttpRequest.DEST_PATH)
//
//                .addHeaderParams(new LinkedHashMap<String, String>() {{
//                    put("dropped1", "value2");
//                    put("dropped2", "value3");
//                }})
//                .addHeaderParam("dropped3", "value4")
//                .setHeaderParams(new LinkedHashMap<String, String>() {{
//                    put("f1", "vf1");
//                }})
//                .addHeaderParams(new LinkedHashMap<String, String>() {{
//                    put("f2", "vf2");
//                    put("f3", "vf3");
//                }})
//                .addHeaderParam("f4", "vf4")
//                .addParam("f5", "vf5", HttpRequest.DEST_HEADER);
//
//        assertEquals(new LinkedHashMap<String, Object>() {{
//            put("f1", "vf1");
//            put("f2", "vf2");
//            put("f3", "vf3");
//            put("f4", "vf4");
//            put("f5", "vf5");
//        }}, io.getFormParams());
//        assertEquals(new LinkedHashMap<String, String>() {{
//            put("f1", "vf1");
//            put("f2", "vf2");
//            put("f3", "vf3");
//            put("f4", "vf4");
//            put("f5", "vf5");
//        }}, io.getQueryParams());
//        assertEquals(new LinkedHashMap<String, String>() {{
//            put("f1", "vf1");
//            put("f2", "vf2");
//            put("f3", "vf3");
//            put("f4", "vf4");
//            put("f5", "vf5");
//        }}, io.getPathParams());
//        assertEquals(new LinkedHashMap<String, String>() {{
//            put("f1", "vf1");
//            put("f2", "vf2");
//            put("f3", "vf3");
//            put("f4", "vf4");
//            put("f5", "vf5");
//        }}, io.getHeaderParams());
//
//    }
//
//    @Test
//    public void testGetUrl() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//        HttpRequest.Builder io = new HttpRequest.Builder("http://127.0.0.1/{p1}/{p2}/")
//                .addPathParam("p1", "vp1")
//                .addPathParam("p2", "vp2")
//                .addQueryParam("q1", "vq1")
//                .addQueryParam("q2", "vq2")
//                .addQueryParam("q3", "vq3");
//        assertEquals("http://127.0.0.1/vp1/vp2/", io.getBaseUrl());
//        assertEquals(new URL("http://127.0.0.1/vp1/vp2/?q1=vq1&q2=vq2&q3=vq3"), io.getUrl(true));
//        assertEquals(new URL("http://127.0.0.1/vp1/vp2/"), io.getUrl(false));
//        assertEquals("http://127.0.0.1/vp1/vp2/?q1=vq1&q2=vq2&q3=vq3", io.getUrlString(true));
//        assertEquals("http://127.0.0.1/vp1/vp2/", io.getUrlString(false));
//    }
//
//    @Test
//    public void testGetUrlWithUnresolved() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//        HttpRequest.Builder io = new HttpRequest.Builder("http://127.0.0.1/{p1}/{p2}/");
//        try {
//            io.getBaseUrl();
//            fail("should have failed");
//        } catch (IllegalStateException e) {
//        }
//        try {
//            io.getUrl(true);
//            fail("should have failed");
//        } catch (IllegalStateException e) {
//        }
//        try {
//            io.getUrl(false);
//            fail("should have failed");
//        } catch (IllegalStateException e) {
//        }
//        try {
//            io.getUrlString(true);
//            fail("should have failed");
//        } catch (IllegalStateException e) {
//        }
//        try {
//            io.getUrlString(false);
//            fail("should have failed");
//        } catch (IllegalStateException e) {
//        }
//    }
//    @Test
//    public void testDefaultBuild1() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//        HttpRequest.Builder builder = new HttpRequest.Builder("http://127.0.0.1", "ISO-8859-1");
//        HttpRequest io = builder.build();
//        assertEquals("GET", io.getMeth());
//        assertEquals(new URL("http://127.0.0.1"), io.getUrl());
//        assertEquals("http://127.0.0.1", io.getUrlString(true));
//        assertEquals(new URL("http://127.0.0.1"), io.getUrl(true));
//        assertNull(null, io.getConnectionTimeout());
//        assertNull(null, io.getSocketTimeout());
//        assertEquals("ISO-8859-1", io.getEncoding());
//        assertEquals(Charset.forName("ISO-8859-1"), io.getEncodingAsCharset());
//        assertEquals(Collections.<String, String>emptyMap(), io.getHeaderParams());
//        assertEquals(Collections.<String, String>emptyMap(), io.getQueryParams());
//        assertEquals(Collections.<String, Object>emptyMap(), io.getFormParams());
//    }
//
//    @Test
//    public void testDefaultBuild2() throws URISyntaxException, MalformedURLException, UnsupportedEncodingException {
//        HttpRequest.Builder builder = new HttpRequest.Builder("http://127.0.0.1", "iso");
//        HttpRequest io = builder.build();
//        assertEquals("GET", io.getMeth());
//        assertEquals(new URL("http://127.0.0.1"), io.getUrl());
//        assertEquals("http://127.0.0.1", io.getUrlString(true));
//        assertEquals(new URL("http://127.0.0.1"), io.getUrl(true));
//        assertNull(null, io.getConnectionTimeout());
//        assertNull(null, io.getSocketTimeout());
//        assertEquals("iso", io.getEncoding());
//        assertEquals(Collections.<String, String>emptyMap(), io.getHeaderParams());
//        assertEquals(Collections.<String, String>emptyMap(), io.getQueryParams());
//        assertEquals(Collections.<String, Object>emptyMap(), io.getFormParams());
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void testDefaultBuildFailure1() throws URISyntaxException {
//        new HttpRequest.Builder(null);
//    }
//
//    @Test(expected = NullPointerException.class)
//    public void testDefaultBuildFailure2() throws URISyntaxException {
//        new HttpRequest.Builder(null, "utf-8");
//    }
//
//
////    static HttpRequest.Builder getFull() throws URISyntaxException {
////        return getFull("http://127.0.0.1/{path-param1}/{path-param2}/?p1=v1&p2={0}&p3={1}");
////    }
////    static HttpRequest.Builder getFull(String url) throws URISyntaxException {
////        return new HttpRequest.Builder(url, "utf-8")
////
////                .addFormParams(new LinkedHashMap<String, Object>() {{
////                    put("dropped1", "value2");
////                    put("dropped2", "value3");
////                }})
////                .addFormParam("dropped3", "value4")
////                .setFormParams(new LinkedHashMap<String, Object>() {{
////                    put("b1", "vb1");
////                }})
////                .addFormParams(new LinkedHashMap<String, Object>() {{
////                    put("b2", "vb2");
////                    put("b3", "vb3");
////                }})
////                .addFormParam("b4", "vb4")
////                .addQueryParams(new LinkedHashMap<String, String>() {{
////                    put("p4", "v4");
////                    put("p5", "v5");
////                }})
////                .addQueryParam("p6", "v6")
////
////                .addPathParam("path-param1", "v3")
////                .addPathParam("path-param2", "v2")
////                .addPathParam("path-param3", "v4")
////
////                .addHeaderParams(new LinkedHashMap<String, String>() {{
////                    put("dropped1", "vh2");
////                    put("dropped2", "vh3");
////                }})
////                .addHeaderParam("dropped3", "vh4")
////                .setHeaderParams(new LinkedHashMap<String, String>() {{
////                    put("h1", "vh1");
////                }})
////                .addHeaderParams(new LinkedHashMap<String, String>() {{
////                    put("h2", "vh2");
////                    put("h3", "vh3");
////                }})
////                .addHeaderParam("h4", "vh4")
////
////                .timeoutConnectionAfter(10l)
////                .timeoutSocketAfter(20l)
////                .using("PUT");
////    }
//
//}
