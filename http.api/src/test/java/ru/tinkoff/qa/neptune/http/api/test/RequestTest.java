package ru.tinkoff.qa.neptune.http.api.test;

import org.hamcrest.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.*;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.test.mapping.MethodMappingAPI;
import ru.tinkoff.qa.neptune.http.api.test.request.body.JsonBodyObject;
import ru.tinkoff.qa.neptune.http.api.test.request.body.XmlBodyObject;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.getProperties;
import static java.net.http.HttpClient.Version.HTTP_2;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.time.Duration.ofSeconds;
import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.*;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

public class RequestTest {

    private final static URI TEST_URI = URI.create("http://127.0.0.1:8089");
    private static final File TEST_FILE = getTestFile();

    private static final JsonBodyObject BODY_JSON_OBJECT = new JsonBodyObject().setA("Some String")
            .setB(666)
            .setC(true);

    private static final XmlBodyObject BODY_XML_OBJECT = new XmlBodyObject().setA("Some String")
            .setB(666)
            .setC(true);

    private static final Document JSOUP_DOCUMENT = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
            "<html>\n" +
            "    <head>\n" +
            "        <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\">\n" +
            "        <title>Login Page</title>\n" +
            "    </head>\n" +
            "    <body>\n" +
            "        <div id=\"login\" class=\"simple\" >\n" +
            "            <form action=\"login.do\">\n" +
            "                Username : <input id=\"username\" type=\"text\" /><br>\n" +
            "                Password : <input id=\"password\" type=\"password\" /><br>\n" +
            "                <input id=\"submit\" type=\"submit\" />\n" +
            "                <input id=\"reset\" type=\"reset\" />\n" +
            "            </form>\n" +
            "        </div>\n" +
            "    </body>\n" +
            "</html>");

    private static final Map<String, String> FORM_PARAMS = new LinkedHashMap<>() {
        {
            put("param1", "value1");
            put("param2", "value2");
        }
    };

    private static final String REQUEST_BODY_XML_FOR_DOCUMENT = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><a><b/><c/></a>";
    private static final org.w3c.dom.Document W3C_DOCUMENT = prepareW3CDocument();

    private static File getTestFile() {
        var f = new File(randomAlphanumeric(15) + ".txt");
        try {
            if (!f.createNewFile()) {
                throw new IllegalStateException("Test file is not created");
            }
            f.deleteOnExit();
            writeStringToFile(f, "Test text", UTF_8);
            return f;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static org.w3c.dom.Document prepareW3CDocument() {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        try {
            var documentBuilder = documentBuilderFactory.newDocumentBuilder();
            var inputSource = new InputSource(new StringReader(REQUEST_BODY_XML_FOR_DOCUMENT));
            return documentBuilder.parse(inputSource);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Object[][] prepareDataForMethodMapping(MethodMappingAPI methodMappingAPI) {
        return new Object[][]{
                {methodMappingAPI.postSomething(), "POST", true},
                {methodMappingAPI.getSomething(), "GET", false},
                {methodMappingAPI.putSomething(), "PUT", true},
                {methodMappingAPI.deleteSomething(), "DELETE", false},
                {methodMappingAPI.patchSomething(), "PATCH", true},
                {methodMappingAPI.headSomething(), "HEAD", true},
                {methodMappingAPI.optionsSomething(), "OPTIONS", true},
                {methodMappingAPI.traceSomething(), "TRACE", true},
        };
    }

    private static Object[][] prepareDataForPathMapping(MethodMappingAPI methodMappingAPI) {
        return new Object[][]{
                {methodMappingAPI.getSomethingWithConstantPath(),
                        uriHasPath("/path/to/target/end/point"),
                        "/path/to/target/end/point"},
                {methodMappingAPI.getSomethingWithVariablePath("Start path", 1.5F, "Кириллический текст"),
                        uriHasPath("/Start+path/1.5/and/then/Кириллический+текст/end/point"),
                        "/Start+path/1.5/and/then/%D0%9A%D0%B8%D1%80%D0%B8%D0%BB%D0%BB%D0%B8%D1%87%D0%B5%D1%81%D0%BA%D0%B8%D0%B9+%D1%82%D0%B5%D0%BA%D1%81%D1%82/end/point"},
                {methodMappingAPI.getSomethingWithVariablePath("Start", "Next"),
                        uriHasPath("/Start/Next/and/then/Next/end/point"),
                        "/Start/Next/and/then/Next/end/point"},
        };
    }

    private static Object[][] prepareDataForQueryMapping(MethodMappingAPI methodMappingAPI) {
        return new Object[][]{
                {methodMappingAPI.getSomethingWithQuery("val1", 3, "Hello world", true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {methodMappingAPI.getSomethingWithQueryAndPath("val1", 3, "Hello world", true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},


                {methodMappingAPI.getSomethingWithQuery(of("val1", 3, "Hello world"), true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {methodMappingAPI.getSomethingWithQueryAndPath(of("val1", 3, "Hello world"), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {methodMappingAPI.getSomethingWithQuery(new Object[]{"val1", 3, "Hello world"}, true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {methodMappingAPI.getSomethingWithQueryAndPath(new Object[]{"val1", 3, "Hello world"}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {methodMappingAPI.getSomethingWithQuery(new int[]{1, 2, 3}, true),
                        equalTo("/"),
                        equalTo("param1=1&param1=2&param1=3&param2=true")},

                {methodMappingAPI.getSomethingWithQueryAndPath(new int[]{1, 2, 3}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1&param1=2&param1=3&param2=true")},
        };
    }

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {POST("https://www.google.com/"), "POST", nullValue()},
                {POST("https://www.google.com/", "Some body"), "POST", equalTo("Some body")},
                {GET("https://www.google.com/"), "GET", nullValue()},
                {DELETE("https://www.google.com/"), "DELETE", nullValue()},
                {PUT("https://www.google.com/"), "PUT", nullValue()},
                {PUT("https://www.google.com/", "Some body"), "PUT", equalTo("Some body")},
                {METHOD("CUSTOM_METHOD", "https://www.google.com/", "Some body"), "CUSTOM_METHOD", equalTo("Some body")},
        };
    }

    @DataProvider
    public static Object[][] data2() {
        return prepareDataForMethodMapping(createAPI(MethodMappingAPI.class, TEST_URI));
    }

    @DataProvider
    public static Object[][] data3() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:8089");
            return prepareDataForMethodMapping(createAPI(MethodMappingAPI.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getPropertyName());
        }
    }

    @DataProvider
    public static Object[][] data9() throws Exception {
        var methodMappingAPI = createAPI(MethodMappingAPI.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postSomeBody("ABC"), StringBody.class},
                {methodMappingAPI.postSomeBody(null), EmptyBody.class},
                {methodMappingAPI.postByteBody("ABC".getBytes()), ByteArrayBody.class},
                {methodMappingAPI.postFileBody(TEST_FILE), FileBody.class},
                {methodMappingAPI.postFileBody(TEST_FILE.toPath()), FileBody.class},
                {methodMappingAPI.postJsoupBody(JSOUP_DOCUMENT), JSoupDocumentBody.class},
                {methodMappingAPI.postW3CBody(W3C_DOCUMENT), W3CDocumentBody.class},
                {methodMappingAPI.postMap(FORM_PARAMS), URLEncodedForm.class},
                {methodMappingAPI.postStream(new FileInputStream(TEST_FILE)), StreamBody.class},
                {methodMappingAPI.postSupplier(() -> {
                    try {
                        return new FileInputStream(TEST_FILE);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }), StreamSuppliedBody.class},
                {methodMappingAPI.postJson(BODY_JSON_OBJECT), SerializedBody.class},
                {methodMappingAPI.postXml(BODY_XML_OBJECT), SerializedBody.class},
                {methodMappingAPI.postBoolean(true), StringBody.class},
        };
    }

    @DataProvider
    public static Object[][] data10() throws Exception {
        var methodMappingAPI = createAPI(MethodMappingAPI.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postListJson(of("val1", 3, "Hello world")), StringBody.class, "[\"val1\",3,\"Hello world\"]"},
                {methodMappingAPI.postXmlMap(FORM_PARAMS), StringBody.class, "<TestObject><param1>value1</param1><param2>value2</param2></TestObject>"},
                {methodMappingAPI.postXmlArray("Test", 1, true), StringBody.class, "<Root xmlns=\"http://www.example.com\"><item>Test</item><item>1</item><item>true</item></Root>"},
                {methodMappingAPI.postForm("ABC", 2, true), URLEncodedForm.class, "form_string_param1=ABC&form_int_param2=2&form_bool_param3=true"},
        };
    }

    @DataProvider
    public Object[][] data4() {
        return prepareDataForPathMapping(createAPI(MethodMappingAPI.class, TEST_URI));
    }

    @DataProvider
    public Object[][] data5() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:8089");
            return prepareDataForPathMapping(createAPI(MethodMappingAPI.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getPropertyName());
        }
    }

    @DataProvider
    public Object[][] data6() {
        return prepareDataForQueryMapping(createAPI(MethodMappingAPI.class, TEST_URI));
    }

    @DataProvider
    public Object[][] data7() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:8089");
            return prepareDataForQueryMapping(createAPI(MethodMappingAPI.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getPropertyName());
        }
    }

    @DataProvider
    public Object[][] data8() {
        var methodMappingAPI = createAPI(MethodMappingAPI.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postSomeBody("ABC"), "POST"},
                {methodMappingAPI.getSomeBody("ABC"), "GET"},
                {methodMappingAPI.putSomeBody("ABC"), "PUT"},
                {methodMappingAPI.deleteBody("ABC"), "DELETE"},
                {methodMappingAPI.patchSomeBody("ABC"), "PATCH"},
        };
    }


    @Test(dataProvider = "data")
    public void test1(RequestBuilder builder, String method, Matcher<Object> matcher) {
        var request = builder
                .header("header1", "abc")
                .header("header1", "one more value")
                .queryParam("param", "val1", 3, "Hello world")
                .timeout(ofSeconds(2))
                .version(HTTP_2)
                .expectContinue(true)
                .header("header2", "one more value")
                .header("header3", "one more value again")
                .build();

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("https://www.google.com/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
        assertThat(request.method(), is(method));

        var body = ofNullable(((NeptuneHttpRequestImpl) request).body())
                .map(RequestBody::body)
                .orElse(null);
        assertThat(body, matcher);
    }

    @Test
    public void test2() throws Exception {
        var request = METHOD("Some_method", new URL("https://www.google.com/"))
                .tuneWith(new RequestTuner1(), new RequestTuner2())
                .build();

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("https://www.google.com/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
        assertThat(request.method(), is("Some_method"));
    }

    @Test
    public void test3() {
        var request = PUT("https://www.google.com/")
                .tuneWith(RequestTuner1.class, RequestTuner1.class, RequestTuner2.class)
                .build();

        assertThat(request, instanceOf(NeptuneHttpRequestImpl.class));
        assertThat(request.uri(), is(URI.create("https://www.google.com/?param=val1&param=3&param=Hello+world")));
        assertThat(request.version().orElse(null), is(HTTP_2));
        assertThat(request.timeout().orElse(null), is(ofSeconds(2)));
        assertThat(request.expectContinue(), is(true));
        assertThat(request.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
        assertThat(request.method(), is("PUT"));
    }

    @Test(dataProvider = "data2")
    public void test4(RequestBuilder builder, String method, boolean isBodyPresent) {
        var r = builder.build();
        assertThat(r.method(), is(method));
        assertThat(r.uri(), is(URI.create("http://127.0.0.1:8089")));

        var r2 = (NeptuneHttpRequestImpl) r;
        var body = r2.body();
        if (isBodyPresent) {
            assertThat(body, instanceOf(RequestBody.class));
            assertThat(body.body(), nullValue());
        } else {
            assertThat(body, nullValue());
        }
    }

    @Test(dataProvider = "data3")
    public void test5(RequestBuilder builder, String method, boolean isBodyPresent) {
        test4(builder, method, isBodyPresent);
    }

    @Test
    public void test6() {
        var methodMappingAPI = createAPI(MethodMappingAPI.class, TEST_URI);
        var r = methodMappingAPI.postSomethingWithHeaders().build();

        assertThat(r.headers().map(), allOf(hasEntry("header1", of("abc", "one more value")),
                hasEntry("header2", of("one more value")),
                hasEntry("header3", of("one more value again"))));
    }

    @Test(dataProvider = "data4")
    public void test7(RequestBuilder builder, HasPathMatcher<URI> pathMatcher, String rawPath) {
        var uri = builder.build().uri();
        assertThat(uri, uriHasScheme("http"));
        assertThat(uri, uriHasHost("127.0.0.1"));
        assertThat(uri, uriHasPort(8089));
        assertThat(uri, pathMatcher);
        assertThat(uri.toString(), endsWith(rawPath));
    }

    @Test(dataProvider = "data5")
    public void test8(RequestBuilder builder, HasPathMatcher<URI> pathMatcher, String rawPath) {
        test7(builder, pathMatcher, rawPath);
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = ".*['Path variable 'third' is not defined by URIPath. " +
                    "Value that was defined by URIPath']")
    public void test9() {
        var methodMappingAPI = createAPI(MethodMappingAPI.class, TEST_URI);
        methodMappingAPI.getSomethingWithVariablePathFailed("StartPath",
                1.5F,
                "EndPath");
        fail("Exception was expected");
    }

    @Test(expectedExceptions = UnsupportedOperationException.class,
            expectedExceptionsMessageRegExp = ".*['Path variable 'next' is defined more than once. This is not supported']")
    public void test10() {
        var methodMappingAPI = createAPI(MethodMappingAPI.class, TEST_URI);
        methodMappingAPI.getSomethingWithVariablePathFailed("StartPath",
                1.5F,
                true);
        fail("Exception was expected");
    }

    @Test(dataProvider = "data6")
    public void test11(RequestBuilder builder, Matcher<String> pathMatcher, Matcher<String> queryMather) {
        var uri = builder.build().uri();
        assertThat(uri, uriHasScheme("http"));
        assertThat(uri, uriHasHost("127.0.0.1"));
        assertThat(uri, uriHasPort(8089));
        assertThat(uri, uriHasPath(pathMatcher));
        assertThat(uri, uriHasQuery(queryMather));
    }

    @Test(dataProvider = "data7")
    public void test12(RequestBuilder builder, Matcher<String> pathMatcher, Matcher<String> queryMather) {
        test11(builder, pathMatcher, queryMather);
    }

    @Test(dataProvider = "data8")
    public void test13(RequestBuilder builder, String method) {
        var r = builder.build();
        assertThat(r.method(), is(method));

        var body = ((NeptuneHttpRequestImpl) r).body();
        assertThat(body, notNullValue());
        assertThat(body.body(), notNullValue());
    }

    @Test(dataProvider = "data9")
    public void test14(RequestBuilder builder, Class<?> bodyClass) {
        var r = builder.build();

        var body = ((NeptuneHttpRequestImpl) r).body();
        assertThat(body, instanceOf(bodyClass));
    }

    @Test(dataProvider = "data10")
    public void test15(RequestBuilder builder, Class<?> bodyClass, String body) {
        var r = builder.build();

        var rBody = ((NeptuneHttpRequestImpl) r).body();
        assertThat(rBody, instanceOf(bodyClass));
        assertThat(rBody.body(), is(body));
    }

    @Test
    public void test16() {
        var body = ((NeptuneHttpRequestImpl) createAPI(MethodMappingAPI.class, TEST_URI)
                .postMultipart(TEST_FILE,
                        new Object[]{"ABC", 2, true},
                        TEST_FILE.toPath(),
                        new byte[]{1, 2, 3},
                        TEST_FILE.toPath(),
                        new byte[]{1, 2, 3},
                        TEST_FILE.toPath())
                .build())
                .body()
                .body();

        assertThat(body, instanceOf(BodyPart[].class));

        var parts = (BodyPart[]) body;
        assertThat(parts, arrayWithSize(7));

        assertThat(parts[0].toString(), is("Content-Disposition: form-data;name=\"test_file\"\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content-Transfer-Encoding: binary\r\n" +
                "Content: File " + TEST_FILE.getAbsolutePath() + " of size " + TEST_FILE.length() + "(bytes)\r\n"));

        assertThat(parts[1].toString(), is("Content-Disposition: form-data;name=\"test_xml\"\r\n" +
                "Content-Type: application/xml\r\n" +
                "Content: <Root xmlns=\"http://www.example.com\"><item>ABC</item><item>2</item><item>true</item></Root>\r\n"));

        assertThat(parts[2].toString(), is("Content-Disposition: form-data;name=\"test_file2\";filename=\"" + TEST_FILE.getName() + "\"\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content: File " + TEST_FILE.getAbsolutePath() + " of size " + TEST_FILE.length() + "(bytes)\r\n"));

        assertThat(parts[3].toString(), startsWith("Content-Disposition: form-data;name=\"test_binary\";filename=\""));
        assertThat(parts[3].toString(), endsWith("\r\nContent-Type: application/octet-stream\r\nContent: Byte array of length 3\r\n"));

        assertThat(parts[4].toString(), is("Content-Disposition: form-data;name=\"test_file3\"\r\n" +
                "Content-Type: text/plain\r\n" +
                "Content: File " + TEST_FILE.getAbsolutePath() + " of size " + TEST_FILE.length() + "(bytes)\r\n"));

        assertThat(parts[5].toString(), is("Content-Disposition: form-data;name=\"test_binary2\";filename=\"tezzt_file\"\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content: Byte array of length 3\r\n"));

        assertThat(parts[6].toString(), is("Content-Disposition: form-data;name=\"test_file4\";filename=\"tezzt_file\"\r\n" +
                "Content-Type: application/octet-stream\r\n" +
                "Content: File " + TEST_FILE.getAbsolutePath() + " of size " + TEST_FILE.length() + "(bytes)\r\n"));
    }
}
