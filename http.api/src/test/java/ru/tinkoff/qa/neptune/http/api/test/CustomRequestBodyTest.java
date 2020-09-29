package ru.tinkoff.qa.neptune.http.api.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.test.request.body.BodyObject;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.StringReader;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.nio.charset.StandardCharsets.UTF_8;
import static java.nio.file.Files.readAllBytes;
import static org.apache.commons.io.FileUtils.writeStringToFile;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphanumeric;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;
import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.JSON;
import static ru.tinkoff.qa.neptune.http.api.mapping.DefaultMapper.XML;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.BodyPart.bodyPart;
import static ru.tinkoff.qa.neptune.http.api.request.body.multipart.ContentTransferEncoding.BINARY;
import static ru.tinkoff.qa.neptune.http.api.request.body.url.encoded.FormParameter.formParameter;

public class CustomRequestBodyTest extends BaseHttpTest {

    private static final BodyObject BODY_OBJECT = new BodyObject().setA("Some String")
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

    private static final File TEST_FILE = getTestFile();


    private static final String PATH_TO_GSON = "/gson";
    private static final String PATH_TO_XML = "/jackson_xml";
    private static final String PATH_DOCUMENT_XML = "/document_xml";
    private static final String PATH_DOCUMENT_HTML = "/document_html";
    private static final String PATH_URL_UNLOADED = "/urlencoded";
    private static final String PATH_MULTI_PART = "/multipart";

    private static final String REQUEST_BODY_GSON = "{\"A\":\"Some String\",\"B\":666,\"C\":true}";
    private static final String REQUEST_BODY_MAPPED = "<BodyObject><wstxns1:A1 xmlns:wstxns1=\"http://www.test.com\">Some String</wstxns1:A1>" +
            "<wstxns2:B1 xmlns:wstxns2=\"http://www.test.com\">666</wstxns2:B1>" +
            "<wstxns3:C1 xmlns:wstxns3=\"http://www.test.com\">true</wstxns3:C1></BodyObject>";
    private static final String REQUEST_BODY_XML_FOR_DOCUMENT = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><a><b/><c/></a>";
    private static final org.w3c.dom.Document W3C_DOCUMENT = prepareW3CDocument();

    private static final String REQUEST_BODY_URL_UNLOADED = "param1=value1&param2=value2";
    private static final String REQUEST_BODY_URL_UNLOADED2 = "chip&dale=rescue+rangers&how+to+get+water=2H2+%2B+O2+%3D+2H2O";

    private static final String JSON_HAS_BEEN_SUCCESSFULLY_POSTED = "Json has been successfully posted";
    private static final String JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED = "Jackson xml has been successfully posted";
    private static final String DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED = "Document xml has been successfully posted";
    private static final String DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED = "Document html has been successfully posted";
    private static final String FORM_HAS_BEEN_SUCCESSFULLY_POSTED = "Form has been successfully posted";
    private static final String MULTIPART_SUCCESSFULLY_POSTED = "Multipart successfully posted";

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

    @BeforeClass
    public static void beforeClass() throws Exception {
        stubFor(post(urlPathEqualTo(PATH_TO_GSON))
                .withHeader("Content-Type", equalTo("application/json"))
                .withRequestBody(equalTo(REQUEST_BODY_GSON))
                .willReturn(aResponse().withBody(JSON_HAS_BEEN_SUCCESSFULLY_POSTED)));

        stubFor(post(urlPathEqualTo(PATH_TO_XML))
                .withHeader("Content-Type", equalTo("application/xml"))
                .withRequestBody(equalTo(REQUEST_BODY_MAPPED))
                .willReturn(aResponse().withBody(JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED)));

        stubFor(post(urlPathEqualTo(PATH_DOCUMENT_XML))
                .withHeader("Content-Type", equalTo("application/xml"))
                .withRequestBody(equalTo(REQUEST_BODY_XML_FOR_DOCUMENT))
                .willReturn(aResponse().withBody(DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED)));

        stubFor(post(urlPathEqualTo(PATH_DOCUMENT_HTML))
                .withHeader("Content-Type", equalTo("application/xhtml+xml"))
                .withRequestBody(equalTo(JSOUP_DOCUMENT.outerHtml()))
                .willReturn(aResponse().withBody(DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED)));

        stubFor(post(urlPathEqualTo(PATH_URL_UNLOADED))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo(REQUEST_BODY_URL_UNLOADED))
                .willReturn(aResponse().withBody(FORM_HAS_BEEN_SUCCESSFULLY_POSTED)));

        stubFor(post(urlPathEqualTo(PATH_URL_UNLOADED))
                .withHeader("Content-Type", equalTo("application/x-www-form-urlencoded"))
                .withRequestBody(equalTo(REQUEST_BODY_URL_UNLOADED2))
                .willReturn(aResponse().withBody(FORM_HAS_BEEN_SUCCESSFULLY_POSTED)));

        stubFor(post(urlPathEqualTo(PATH_MULTI_PART))
                .withHeader("Content-Type", containing("multipart/form-data"))
                .withMultipartRequestBody(aMultipart()
                        .withName("testFile")
                        .withHeader("Content-Disposition", containing("filename=\"test_file.txt\""))
                        .withHeader("Content-Type", containing("application/octet-stream"))
                        .withBody(binaryEqualTo(readAllBytes(TEST_FILE.toPath())))).

                        withMultipartRequestBody(aMultipart()
                                .withName("testFile2")
                                .withHeader("Content-Disposition", containing("filename=\"" + TEST_FILE.getName() + "\""))
                                .withHeader("Content-Type", equalTo("text/plain"))
                                .withBody(binaryEqualTo(readAllBytes(TEST_FILE.toPath())))).

                        withMultipartRequestBody(aMultipart()
                                .withName("testBytes")
                                .withHeader("Content-Type", containing("application/octet-stream"))
                                .withHeader("Content-Transfer-Encoding", equalTo(BINARY.toString()))
                                .withBody(binaryEqualTo(new byte[]{1, 2, 3}))).

                        withMultipartRequestBody(aMultipart()
                                .withName("testJson")
                                .withHeader("Content-Type", containing("application/json"))
                                .withBody(containing(JSON.getMapper().writeValueAsString(BODY_OBJECT))))
                .willReturn(aResponse().withBody(MULTIPART_SUCCESSFULLY_POSTED)));
    }

    @DataProvider
    public static Object[][] data() throws Exception {

        return new Object[][]{
                {POST(REQUEST_URI + PATH_TO_GSON, JSON, BODY_OBJECT)
                        .header("Content-Type", "application/json"),
                        JSON_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_TO_XML, XML, BODY_OBJECT)
                        .header("Content-Type", "application/xml"),
                        JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_DOCUMENT_XML, W3C_DOCUMENT)
                        .header("Content-Type", "application/xml"),
                        DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_DOCUMENT_HTML, JSOUP_DOCUMENT)
                        .header("Content-Type", "application/xhtml+xml"),
                        DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_URL_UNLOADED,
                        formParameter("param1", false, "value1"),
                        formParameter("param2", false, "value2"))
                        .header("Content-Type", "application/x-www-form-urlencoded"),
                        FORM_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_URL_UNLOADED,
                        formParameter("chip&dale", false, "rescue rangers"),
                        formParameter("how to get water", false, "2H2 + O2 = 2H2O"))
                        .header("Content-Type", "application/x-www-form-urlencoded"),
                        FORM_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_MULTI_PART,
                        bodyPart(TEST_FILE, "testFile", "test_file.txt", false),
                        bodyPart(TEST_FILE, "testFile2", true, true),
                        bodyPart(new byte[]{1, 2, 3}, "testBytes").setContentTransferEncoding(BINARY),
                        bodyPart(new FileInputStream(TEST_FILE), "testFile2", TEST_FILE.getName()).setContentType("text/plain"),
                        bodyPart(BODY_OBJECT, JSON, "testJson").setContentType("application/json"))
                        .header("Content-Type", "multipart/form-data"),
                        MULTIPART_SUCCESSFULLY_POSTED}

        };
    }

    @Test(dataProvider = "data")
    public void customRequestBodyTest(RequestBuilder requestBuilder,
                                      String expectedMessage) {
        assertThat(http().responseOf(requestBuilder, ofString()),
                hasBody(is(expectedMessage)));
    }
}
