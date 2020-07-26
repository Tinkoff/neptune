package ru.tinkoff.qa.neptune.http.api.test.requests.mapping;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.http.api.dto.JsonDTObject;
import ru.tinkoff.qa.neptune.http.api.dto.XmlDTObject;
import ru.tinkoff.qa.neptune.http.api.mapper.DefaultBodyMappers;
import ru.tinkoff.qa.neptune.http.api.request.NeptuneHttpRequestImpl;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.*;
import ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter;
import ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.URLEncodedForm;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.HttpMethod;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.MethodParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.ParameterFieldName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.Body;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyParamFormat;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineContentType;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.DefineFileName;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.multipart.MultiPartBody;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.url.encoded.URLEncodedParameter;
import ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormParam;
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
import static java.util.List.of;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.http.api.request.FormValueDelimiters.SPACE;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.BINARY;
import static ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter.formParameter;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods.DefaultHttpMethods.*;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.JSON;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.body.BodyDataFormat.XML;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.PIPE_DELIMITED;
import static ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.parameters.form.FormStyles.SPACE_DELIMITED;

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

    private static final Map<String, Object> SOME_MAP = new LinkedHashMap<>() {
        {
            put("param1", "value1");
            put("param2", "value2");
            put("param3", new Object[]{1, true, "Кирилица", "Hello world"});
        }
    };

    private static final String REQUEST_BODY_XML_FOR_DOCUMENT = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><a><b/><c/></a>";
    private static final org.w3c.dom.Document W3C_DOCUMENT = prepareW3CDocument();

    private final static Map<String, Object> LOW_LEVEL_MAP = new LinkedHashMap<>() {
        {
            put("someNum3", 3);
            put("someString3", "string3=");
            put("someArray3", new Object[]{1, 2, 3, 3});
        }
    };

    private final static Map<String, Object> MID_LEVEL_MAP = new LinkedHashMap<>() {
        {
            put("someNum2", 2);
            put("someString2", "string2");
            put("someArray2", new Object[]{1, 2, 3, 3});
            put("nested2", LOW_LEVEL_MAP);
        }
    };

    private final static Map<String, Object> MID_LEVEL_MAP2 = new LinkedHashMap<>() {
        {
            put("someNum4", 4);
            put("someArray4", new Object[]{1, 2, 3, 3});
            put("someString4", "string4$/=");
            put("nested4", LOW_LEVEL_MAP);
        }
    };

    private final static Map<String, Object> HIGH_LEVEL_MAP = new LinkedHashMap<>() {
        {
            put("someNumber", 1);
            put("someString", "string[1]");
            put("someArray", List.of("ABCD?", "EF:GH", "АБВГ Д&="));
            put("nested", MID_LEVEL_MAP);
            put("nestedNext", MID_LEVEL_MAP2);
        }
    };

    private final static BodyFormParameterObject3 LOW_LEVEL_NESTED_OBJECT = new BodyFormParameterObject3()
            .setSomeNum(3)
            .setSomeString("string3=")
            .setSomeArray(new Integer[]{1, 2, 3, 3});

    private final static BodyFormParameterObject FORM_PARAMETER_OBJECT = new BodyFormParameterObject()
            .setSomeNum(1)
            .setSomeString("string[1]")
            .setSomeArray(of("ABCD?", "EF:GH", "АБВГ Д&="))
            .setNested(new BodyFormParameterObject2()
                    .setSomeNum(2)
                    .setSomeString("string2")
                    .setSomeArray(new Integer[]{1, 2, 3, 3})
                    .setNested(LOW_LEVEL_NESTED_OBJECT))
            .setNestedNext(new BodyFormParameterObject4()
                    .setSomeNum(4)
                    .setSomeString("string4$/=")
                    .setSomeArray(new Integer[]{1, 2, 3, 3})
                    .setNested(LOW_LEVEL_NESTED_OBJECT));


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
                {methodMappingAPI.postListJson(List.of("val1", 3, "Hello world", new Object[]{1, true, "Hello worl"})),
                        StringBody.class,
                        "[\"val1\",3,\"Hello world\",[1,true,\"Hello worl\"]]"},

                {methodMappingAPI.postXmlMap(SOME_MAP),
                        StringBody.class,
                        "<TestObject><param1>value1</param1><param2>value2</param2><param3>1</param3><param3>true</param3><param3>Кирилица</param3><param3>Hello world</param3></TestObject>"},

                {methodMappingAPI.postXmlArray("Test", 1, true),
                        StringBody.class,
                        "<Root xmlns=\"http://www.example.com\"><item>Test</item><item>1</item><item>true</item></Root>"},

                {methodMappingAPI.postForm2("ABC АБВ@%&", 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=ABC+%D0%90%D0%91%D0%92%40%25%26&form_int_param2=2&form_bool_param3=true"},

                {methodMappingAPI.postForm2_1("ABC АБВ@%&", 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=ABC+%D0%90%D0%91%D0%92@%25&&form_int_param2=2&form_bool_param3=true"},


                {methodMappingAPI.postForm2(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=ABC" +
                                "&form_object_param1=CDE%24,1,2,3,false" +
                                "&form_object_param1=%D0%90%D0%91%D0%92%40%25%26" +
                                "&form_object_param1=true" +
                                "&form_object_param1=123" +
                                "&form_int_param2=2&form_bool_param3=true"},


                {methodMappingAPI.postForm2(HIGH_LEVEL_MAP, 2, true),
                        URLEncodedForm.class,
                        "someNumber=1&someString=string%5B1%5D" +
                                "&someArray=ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4%24%2F%3D&" +
                                "form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm2(FORM_PARAMETER_OBJECT, 2, true),
                        URLEncodedForm.class,
                        "someNumber=1&someString=string%5B1%5D" +
                                "&someArray=ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4%24%2F%3D&" +
                                "form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm2_1(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=ABC" +
                                "&form_object_param1=CDE$,1,2,3,false" +
                                "&form_object_param1=%D0%90%D0%91%D0%92@%25&" +
                                "&form_object_param1=true" +
                                "&form_object_param1=123" +
                                "&form_int_param2=2&form_bool_param3=true"},


                {methodMappingAPI.postForm2_1(HIGH_LEVEL_MAP, 2, true),
                        URLEncodedForm.class,
                        "someNumber=1&someString=string[1]" +
                                "&someArray=ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4$/=" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm2_1(FORM_PARAMETER_OBJECT, 2, true),
                        URLEncodedForm.class,
                        "someNumber=1&someString=string[1]" +
                                "&someArray=ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&nested=someNum2,2,someString2,string2,someArray2,1,2,3,3" +
                                "&nestedNext=someNum4,4,someArray4,1,2,3,3,someString4,string4$/=" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm2_2(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=ABC,CDE%24,1,2,3,false,%D0%90%D0%91%D0%92%40%25%26,true,123" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},

                {methodMappingAPI.postForm2_2(HIGH_LEVEL_MAP, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=someNumber,1,someString,string%5B1%5D,someArray,ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm2_2(FORM_PARAMETER_OBJECT, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=someNumber,1,someString,string%5B1%5D,someArray,ABCD%3F,EF%3AGH,%D0%90%D0%91%D0%92%D0%93+%D0%94%26%3D" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm2_3(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=ABC,CDE$,1,2,3,false,%D0%90%D0%91%D0%92@%25&,true,123" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},

                {methodMappingAPI.postForm2_3(HIGH_LEVEL_MAP, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=someNumber,1,someString,string[1],someArray,ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},

                {methodMappingAPI.postForm2_3(FORM_PARAMETER_OBJECT, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=someNumber,1,someString,string[1],someArray,ABCD?,EF:GH,%D0%90%D0%91%D0%92%D0%93+%D0%94&=" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm3(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_array_param1=ABC%20CDE%24,1,2,3,false%20%D0%90%D0%91%D0%92%40%25%26%20true%20123" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm3_1(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_array_param1=ABC%20CDE$,1,2,3,false%20%D0%90%D0%91%D0%92@%25&%20true%20123" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm4(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_array_param1=ABC%7CCDE%24,1,2,3,false%7C%D0%90%D0%91%D0%92%40%25%26%7Ctrue%7C123" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm4_1(new Object[]{"ABC", List.of("CDE$", 1, 2, 3, false), "АБВ@%&", true, 123}, 2, true),
                        URLEncodedForm.class,
                        "form_array_param1=ABC%7CCDE$,1,2,3,false%7C%D0%90%D0%91%D0%92@%25&%7Ctrue%7C123" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},


                {methodMappingAPI.postForm5_1(SOME_MAP, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1={\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":[1,true,\"Кирилица\",\"Hello world\"]}" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},

                {methodMappingAPI.postForm5_2(SOME_MAP, 2, true),
                        URLEncodedForm.class,
                        "form_object_param1=<TestObject><param1>value1</param1><param2>value2</param2><param3>1</param3><param3>true</param3><param3>Кирилица</param3><param3>Hello world</param3></TestObject>" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"},

                {methodMappingAPI.postForm5_2(null, 2, true),
                        URLEncodedForm.class,
                        "form_int_param2=2&form_bool_param3=true"},


                {methodMappingAPI.postForm(formParameter("form_string_param1", false, "ABC АБВ@%&"),
                        formParameter("form_int_param2", 2),
                        formParameter("form_bool_param3", true)),
                        URLEncodedForm.class,
                        "form_string_param1=ABC+%D0%90%D0%91%D0%92%40%25%26&form_int_param2=2&form_bool_param3=true"},

                {methodMappingAPI.postForm(formParameter("form_string_param1", true, "ABC АБВ@%&"),
                        formParameter("form_int_param2", 2),
                        formParameter("form_bool_param3", true)),
                        URLEncodedForm.class,
                        "form_string_param1=ABC+%D0%90%D0%91%D0%92@%25&&form_int_param2=2&form_bool_param3=true"},

                {methodMappingAPI.postForm(List.of(formParameter("form_string_param1", false, "ABC АБВ@%&"),
                        formParameter("form_int_param2", 2),
                        formParameter("form_bool_param3", true))),
                        URLEncodedForm.class,
                        "form_string_param1=ABC+%D0%90%D0%91%D0%92%40%25%26&form_int_param2=2&form_bool_param3=true"},

                {methodMappingAPI.postForm(List.of(formParameter("form_string_param1", true, "ABC АБВ@%&"),
                        formParameter("form_int_param2", 2),
                        formParameter("form_bool_param3", true))),
                        URLEncodedForm.class,
                        "form_string_param1=ABC+%D0%90%D0%91%D0%92@%25&&form_int_param2=2&form_bool_param3=true"},

                {methodMappingAPI.postForm(List.of(formParameter("form_string_param1", true, "ABC АБВ@%&"),
                        formParameter("form_int_param2", 2),
                        formParameter("form_bool_param3", true))),
                        URLEncodedForm.class,
                        "form_string_param1=ABC+%D0%90%D0%91%D0%92@%25&&form_int_param2=2&form_bool_param3=true"},

                {methodMappingAPI.postForm(List.of(formParameter("form_object_param1", DefaultBodyMappers.JSON, SOME_MAP),
                        formParameter("form_int_param2", 2),
                        formParameter("form_bool_param3", true))),
                        URLEncodedForm.class,
                        "form_object_param1={\"param1\":\"value1\",\"param2\":\"value2\",\"param3\":[1,true,\"Кирилица\",\"Hello world\"]}" +
                                "&form_int_param2=2" +
                                "&form_bool_param3=true"}
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

    @Test
    public void test4() {
        var body = ((NeptuneHttpRequestImpl) createAPI(BodyMapping.class, TEST_URI)
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

    @Test
    public void test5() {
        var body = ((NeptuneHttpRequestImpl) createAPI(BodyMapping.class, TEST_URI)
                .postMultipart(TEST_FILE,
                        new Object[]{"ABC", 2, true},
                        TEST_FILE.toPath(),
                        new byte[]{1, 2, 3},
                        TEST_FILE.toPath(),
                        new byte[]{1, 2, 3},
                        null)
                .build())
                .body()
                .body();

        assertThat(body, instanceOf(BodyPart[].class));

        var parts = (BodyPart[]) body;
        assertThat(parts, arrayWithSize(6));

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
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test6() {
        createAPI(BodyMapping.class, TEST_URI).putSomeBody(null);
        fail("Exception was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test7() {
        createAPI(BodyMapping.class, TEST_URI).postForm2(null, 2, true);
        fail("Exception was expected");
    }

    @Test(expectedExceptions = IllegalArgumentException.class)
    public void test8() {
        createAPI(BodyMapping.class, TEST_URI).postMultipart(TEST_FILE,
                new Object[]{"ABC", 2, true},
                null,
                new byte[]{1, 2, 3},
                TEST_FILE.toPath(),
                new byte[]{1, 2, 3},
                TEST_FILE.toPath());
        fail("Exception was expected");
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
        RequestBuilder postForm2(@URLEncodedParameter(name = "form_object_param1") Object param1,
                                 @URLEncodedParameter(name = "form_int_param2") int param2,
                                 @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm2_1(@URLEncodedParameter(name = "form_object_param1")
                                   @FormParam(allowReserved = true) Object param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm2_2(@URLEncodedParameter(name = "form_object_param1")
                                   @FormParam(explode = false) Object param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm2_3(@URLEncodedParameter(name = "form_object_param1")
                                   @FormParam(explode = false, allowReserved = true) Object param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm3(@URLEncodedParameter(name = "form_array_param1")
                                 @FormParam(style = SPACE_DELIMITED, explode = false) Object[] param1,
                                 @URLEncodedParameter(name = "form_int_param2") int param2,
                                 @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm3_1(@URLEncodedParameter(name = "form_array_param1")
                                   @FormParam(style = SPACE_DELIMITED, explode = false, allowReserved = true) Object[] param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm4(@URLEncodedParameter(name = "form_array_param1")
                                 @FormParam(style = PIPE_DELIMITED, explode = false) Object[] param1,
                                 @URLEncodedParameter(name = "form_int_param2") int param2,
                                 @URLEncodedParameter(name = "form_bool_param3") boolean param3);


        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm4_1(@URLEncodedParameter(name = "form_array_param1")
                                   @FormParam(style = PIPE_DELIMITED, explode = false, allowReserved = true) Object[] param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm5_1(@URLEncodedParameter(name = "form_object_param1")
                                   @BodyParamFormat(format = JSON) Object param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);

        @HttpMethod(httpMethod = POST)
        RequestBuilder postForm5_2(@URLEncodedParameter(name = "form_object_param1", isRequired = false)
                                   @BodyParamFormat(format = XML, mixIns = MapRootElementMixIn.class) Object param1,
                                   @URLEncodedParameter(name = "form_int_param2") int param2,
                                   @URLEncodedParameter(name = "form_bool_param3") boolean param3);


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

                                     @MultiPartBody(name = "test_file4", isRequired = false)
                                     @DefineFileName(fileName = "tezzt_file") Path path3);
    }

    @MethodParameter
    private static class BodyFormParameterObject {

        @ParameterFieldName("someNumber")
        private Integer someNum;

        private String someString;

        private List<String> someArray;

        private BodyFormParameterObject2 nested;

        private BodyFormParameterObject4 nestedNext;


        public BodyFormParameterObject setSomeNum(Integer someNum) {
            this.someNum = someNum;
            return this;
        }

        public String getSomeString() {
            return someString;
        }

        public BodyFormParameterObject setSomeString(String someString) {
            this.someString = someString;
            return this;
        }

        public BodyFormParameterObject setSomeArray(List<String> someArray) {
            this.someArray = someArray;
            return this;
        }

        public BodyFormParameterObject setNested(BodyFormParameterObject2 nested) {
            this.nested = nested;
            return this;
        }

        public BodyFormParameterObject setNestedNext(BodyFormParameterObject4 nestedNext) {
            this.nestedNext = nestedNext;
            return this;
        }
    }

    @MethodParameter
    private static class BodyFormParameterObject2 {
        Integer someNum2;

        String someString2;

        Integer[] someArray2;

        BodyFormParameterObject3 nested2;

        public BodyFormParameterObject2 setSomeNum(Integer someNum) {
            this.someNum2 = someNum;
            return this;
        }

        public BodyFormParameterObject2 setSomeString(String someString) {
            this.someString2 = someString;
            return this;
        }

        public BodyFormParameterObject2 setSomeArray(Integer[] someArray) {
            this.someArray2 = someArray;
            return this;
        }

        public BodyFormParameterObject2 setNested(BodyFormParameterObject3 nested) {
            this.nested2 = nested;
            return this;
        }
    }

    @MethodParameter
    private static class BodyFormParameterObject3 {
        Integer someNum3;

        String someString3;

        Integer[] someArray3;

        public BodyFormParameterObject3 setSomeNum(Integer someNum) {
            this.someNum3 = someNum;
            return this;
        }

        public BodyFormParameterObject3 setSomeString(String someString) {
            this.someString3 = someString;
            return this;
        }

        public BodyFormParameterObject3 setSomeArray(Integer[] someArray) {
            this.someArray3 = someArray;
            return this;
        }
    }

    @MethodParameter
    private static class BodyFormParameterObject4 {
        Integer someNum4;

        Integer[] someArray4;

        String someString4;

        BodyFormParameterObject3 nested4;

        public BodyFormParameterObject4 setSomeNum(Integer someNum) {
            this.someNum4 = someNum;
            return this;
        }

        public BodyFormParameterObject4 setSomeString(String someString) {
            this.someString4 = someString;
            return this;
        }

        public BodyFormParameterObject4 setSomeArray(Integer[] someArray) {
            this.someArray4 = someArray;
            return this;
        }

        public BodyFormParameterObject4 setNested(BodyFormParameterObject3 nested) {
            this.nested4 = nested;
            return this;
        }
    }
}
