package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.*;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParamFormat;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineContentType;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineFileName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;
import ru.tinkoff.qa.neptune.http.api.test.request.body.JsonBodyObject;
import ru.tinkoff.qa.neptune.http.api.test.request.body.XmlBodyObject;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.*;
import java.net.URI;
import java.nio.file.Path;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import static java.nio.charset.StandardCharsets.UTF_8;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.Is.is;
import static ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters.SPACE;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.BINARY;
import static ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter.formParameter;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.*;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.XML;

public class BodyMappingTest {

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

    private static final Map<String, Object> FORM_PARAMS = new LinkedHashMap<>() {
        {
            put("param1", "value1");
            put("param2", "value2");
            put("param3", new Object[]{1, true, "Кирилица", "Hello world"});
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

    @DataProvider
    public static Object[][] data2() throws Exception {
        var methodMappingAPI = createAPI(BodyMapping.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postSomeBody("ABC"), StringBody.class},
                {methodMappingAPI.postSomeBody(null), EmptyBody.class},
                {methodMappingAPI.postByteBody("ABC".getBytes()), ByteArrayBody.class},
                {methodMappingAPI.postFileBody(TEST_FILE), FileBody.class},
                {methodMappingAPI.postFileBody(TEST_FILE.toPath()), FileBody.class},
                {methodMappingAPI.postJsoupBody(JSOUP_DOCUMENT), JSoupDocumentBody.class},
                {methodMappingAPI.postW3CBody(W3C_DOCUMENT), W3CDocumentBody.class},
                {methodMappingAPI.postMap(FORM_PARAMS), URLEncodedForm.class},
                {methodMappingAPI.postForm(formParameter("param1", SPACE, false, 1, 2),
                        formParameter("param2", true, 3, 4, 5)),
                        URLEncodedForm.class},
                {methodMappingAPI.postForm(formParameter("param1", SPACE, false, 1, 2)),
                        URLEncodedForm.class},
                {methodMappingAPI.postForm(List.of(formParameter("param1", SPACE, false, 1, 2),
                        formParameter("param2", true, 3, 4, 5))),
                        URLEncodedForm.class},
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
    public Object[][] data1() {
        var methodMappingAPI = createAPI(BodyMapping.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postSomeBody("ABC"), "POST"},
                {methodMappingAPI.getSomeBody("ABC"), "GET"},
                {methodMappingAPI.putSomeBody("ABC"), "PUT"},
                {methodMappingAPI.deleteBody("ABC"), "DELETE"},
                {methodMappingAPI.patchSomeBody("ABC"), "PATCH"},
                {methodMappingAPI.headSomeBody("ABC"), "HEAD"},
                {methodMappingAPI.optionsSomeBody("ABC"), "OPTIONS"},
                {methodMappingAPI.traceSomeBody("ABC"), "TRACE"},
                {methodMappingAPI.customSomeBody("ABC"), "CUSTOM_METHOD"},
        };
    }

    @DataProvider
    public static Object[][] data3() {
        var methodMappingAPI = createAPI(BodyMapping.class, TEST_URI);
        return new Object[][]{
                {methodMappingAPI.postListJson(List.of("val1", 3, "Hello world")), StringBody.class, "[\"val1\",3,\"Hello world\"]"},
                {methodMappingAPI.postXmlMap(FORM_PARAMS), StringBody.class, "<TestObject><param1>value1</param1><param2>value2</param2><param3>1</param3><param3>true</param3><param3>Кирилица</param3><param3>Hello world</param3></TestObject>"},
                {methodMappingAPI.postXmlArray("Test", 1, true), StringBody.class, "<Root xmlns=\"http://www.example.com\"><item>Test</item><item>1</item><item>true</item></Root>"},
                {methodMappingAPI.postForm("ABC", 2, true), URLEncodedForm.class, "form_string_param1=ABC&form_int_param2=2&form_bool_param3=true"},
                {methodMappingAPI.postForm(FORM_PARAMS), URLEncodedForm.class, "form_string_param1=ABC&form_int_param2=2&form_bool_param3=true"},
        };
    }


    @Test(dataProvider = "data1")
    public void test1(RequestBuilder builder, String method) {
        var r = builder.build();
        assertThat(r.method(), is(method));

        var body = ((NeptuneHttpRequestImpl) r).body();
        assertThat(body, notNullValue());
        assertThat(body.body(), is("ABC"));
    }

    @Test(dataProvider = "data2")
    public void test2(RequestBuilder builder, Class<?> bodyClass) {
        var r = builder.build();

        var body = ((NeptuneHttpRequestImpl) r).body();
        assertThat(body, instanceOf(bodyClass));
    }

    @Test(dataProvider = "data3")
    public void test3(RequestBuilder builder, Class<?> bodyClass, String body) {
        var r = builder.build();

        var rBody = ((NeptuneHttpRequestImpl) r).body();
        assertThat(rBody, instanceOf(bodyClass));
        assertThat(rBody.body(), is(body));
    }


    private interface BodyMapping extends HttpAPI<BodyMapping> {

        @HttpMethod(httpMethod = POST)
        RequestBuilder postSomeBody(@Body(isRequired = false) String body);

        @HttpMethod(httpMethod = GET)
        RequestBuilder getSomeBody(@Body String body);

        @HttpMethod(httpMethod = PUT)
        RequestBuilder putSomeBody(@Body String body);

        @HttpMethod(httpMethod = DELETE)
        RequestBuilder deleteBody(@Body String body);

        @HttpMethod(httpMethod = PATCH)
        RequestBuilder patchSomeBody(@Body String body);

        @HttpMethod(httpMethod = HEAD)
        RequestBuilder headSomeBody(@Body String body);

        @HttpMethod(httpMethod = OPTIONS)
        RequestBuilder optionsSomeBody(@Body String body);

        @HttpMethod(httpMethod = TRACE)
        RequestBuilder traceSomeBody(@Body String body);

        @HttpMethod(httpMethodStr = "CUSTOM_METHOD")
        RequestBuilder customSomeBody(@Body String body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postByteBody(@Body byte[] body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postFileBody(@Body File body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postFileBody(@Body Path body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postJsoupBody(@Body org.jsoup.nodes.Document body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postW3CBody(@Body org.w3c.dom.Document body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postMap(@Body Map<?, ?> body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm(@Body FormParameter... body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm(@Body FormParameter body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm(@Body List<FormParameter> body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postStream(@Body InputStream body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postSupplier(@Body Supplier<InputStream> body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postJson(@Body JsonDTObject body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postXml(@Body XmlDTObject body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postBoolean(@Body Boolean body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postListJson(@Body @BodyParamFormat(format = JSON) List<Object> body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postXmlMap(@Body @BodyParamFormat(format = XML, mixIns = MapRootElementMixIn.class) Map<?, ?> body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postXmlArray(@Body @BodyParamFormat(format = XML, mixIns = ArrayMixIn.class) Object... body);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm(@URLEncodedParameter(name = "form_string_param1") String param1,
                                @URLEncodedParameter(name = "form_int_param2") int param2,
                                @URLEncodedParameter(name = "form_bool_param3") boolean param3);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm(@Body Map<?, ?> form);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postMultipart(@MultiPartBody(name = "test_file", contentTransferEncoding = BINARY) File file,
                                     @MultiPartBody(name = "test_xml")
                                     @BodyParamFormat(format = XML, mixIns = ArrayMixIn.class)
                                     @DefineContentType(contentType = "application/xml") Object[] array,

                                     @MultiPartBody(name = "test_file2")
                                     @DefineFileName(useGivenFileName = true) Path path,

                                     @MultiPartBody(name = "test_binary")
                                     @DefineFileName
                                     @DefineContentType byte[] binary,

                                     @MultiPartBody(name = "test_file3")
                                     @DefineContentType Path path2,

                                     @MultiPartBody(name = "test_binary2")
                                     @DefineFileName(fileName = "tezzt_file")
                                     @DefineContentType byte[] binary2,

                                     @MultiPartBody(name = "test_file4")
                                     @DefineFileName(fileName = "tezzt_file") Path path3);
    }
}
