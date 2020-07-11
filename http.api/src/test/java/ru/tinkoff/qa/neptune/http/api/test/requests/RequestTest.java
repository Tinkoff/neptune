package ru.tinkoff.qa.neptune.http.api.test.requests;

import org.hamcrest.Matcher;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.*;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.test.request.body.JsonBodyObject;
import ru.tinkoff.qa.neptune.http.api.test.request.body.XmlBodyObject;
import ru.tinkoff.qa.neptune.http.api.test.requests.mapping.SomeMappedAPI;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;
import java.net.URI;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.lang.System.getProperties;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.util.List.of;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultEndPointOfTargetAPIProperty.DEFAULT_END_POINT_OF_TARGET_API_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

@Deprecated
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

    private static Object[][] prepareDataForQueryMapping(SomeMappedAPI someMappedAPI) {
        return new Object[][]{
                {someMappedAPI.getSomethingWithQuery("val1", 3, "Hello world", true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath("val1", 3, "Hello world", true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},


                {someMappedAPI.getSomethingWithQuery(of("val1", 3, "Hello world"), true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath(of("val1", 3, "Hello world"), true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQuery(new Object[]{"val1", 3, "Hello world"}, true),
                        equalTo("/"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath(new Object[]{"val1", 3, "Hello world"}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=val1&param1=3&param1=Hello+world&param2=true")},

                {someMappedAPI.getSomethingWithQuery(new int[]{1, 2, 3}, true),
                        equalTo("/"),
                        equalTo("param1=1&param1=2&param1=3&param2=true")},

                {someMappedAPI.getSomethingWithQueryAndPath(new int[]{1, 2, 3}, true),
                        equalTo("/path/to/target/end/point"),
                        equalTo("param1=1&param1=2&param1=3&param2=true")},
        };
    }

    @DataProvider
    public static Object[][] data9() throws Exception {
        var methodMappingAPI = createAPI(SomeMappedAPI.class, TEST_URI);
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
        var methodMappingAPI = createAPI(SomeMappedAPI.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postListJson(of("val1", 3, "Hello world")), StringBody.class, "[\"val1\",3,\"Hello world\"]"},
                {methodMappingAPI.postXmlMap(FORM_PARAMS), StringBody.class, "<TestObject><param1>value1</param1><param2>value2</param2></TestObject>"},
                {methodMappingAPI.postXmlArray("Test", 1, true), StringBody.class, "<Root xmlns=\"http://www.example.com\"><item>Test</item><item>1</item><item>true</item></Root>"},
                {methodMappingAPI.postForm("ABC", 2, true), URLEncodedForm.class, "form_string_param1=ABC&form_int_param2=2&form_bool_param3=true"},
        };
    }

    @DataProvider
    public Object[][] data6() {
        return prepareDataForQueryMapping(createAPI(SomeMappedAPI.class, TEST_URI));
    }

    @DataProvider
    public Object[][] data7() {
        try {
            DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.accept("http://127.0.0.1:8089");
            return prepareDataForQueryMapping(createAPI(SomeMappedAPI.class));
        } finally {
            getProperties().remove(DEFAULT_END_POINT_OF_TARGET_API_PROPERTY.getPropertyName());
        }
    }

    @DataProvider
    public Object[][] data8() {
        var methodMappingAPI = createAPI(SomeMappedAPI.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postSomeBody("ABC"), "POST"},
                {methodMappingAPI.getSomeBody("ABC"), "GET"},
                {methodMappingAPI.putSomeBody("ABC"), "PUT"},
                {methodMappingAPI.deleteBody("ABC"), "DELETE"},
                {methodMappingAPI.patchSomeBody("ABC"), "PATCH"},
        };
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
        var body = ((NeptuneHttpRequestImpl) createAPI(SomeMappedAPI.class, TEST_URI)
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
