package ru.tinkoff.qa.neptune.http.api.test;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.test.request.body.JsonBodyObject;
import ru.tinkoff.qa.neptune.http.api.test.request.body.XmlBodyObject;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.POST;

public class CustomRequestBodyTest extends BaseHttpTest {

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
    private static final Map<String, String> FORM_PARAMS2 = new LinkedHashMap<>() {
        {
            put("chip&dale", "rescue rangers");
            put("how to get water", "2H2 + O2 = 2H2O");
        }
    };
    private static final String PATH_TO_GSON = "/gson";
    private static final String PATH_TO_XML = "/jackson_xml";
    private static final String PATH_DOCUMENT_XML = "/document_xml";
    private static final String PATH_DOCUMENT_HTML = "/document_html";
    private static final String PATH_URL_UNLOADED = "/urlencoded";
    private static final String REQUEST_BODY_GSON = "{\"A\":\"Some String\",\"B\":666,\"C\":true}";
    private static final String REQUEST_BODY_MAPPED = "<XmlBodyObject><wstxns1:A1 xmlns:wstxns1=\"http://www.test.com\">Some String</wstxns1:A1>" +
            "<wstxns2:B1 xmlns:wstxns2=\"http://www.test.com\">666</wstxns2:B1>" +
            "<wstxns3:C1 xmlns:wstxns3=\"http://www.test.com\">true</wstxns3:C1></XmlBodyObject>";
    private static final String REQUEST_BODY_XML_FOR_DOCUMENT = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><a><b/><c/></a>";
    private static final org.w3c.dom.Document W3C_DOCUMENT = prepareW3CDocument();

    private static final String REQUEST_BODY_URL_UNLOADED = "param1=value1&param2=value2";
    private static final String REQUEST_BODY_URL_UNLOADED2 = "chip%26dale=rescue+rangers&how+to+get+water=2H2+%2B+O2+%3D+2H2O";

    private static final String JSON_HAS_BEEN_SUCCESSFULLY_POSTED = "Json has been successfully posted";
    private static final String JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED = "Jackson xml has been successfully posted";
    private static final String DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED = "Document xml has been successfully posted";
    private static final String DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED = "Document html has been successfully posted";
    private static final String FORM_HAS_BEEN_SUCCESSFULLY_POSTED = "Form has been successfully posted";

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

    @BeforeClass
    public static void beforeClass() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/json")
                        .withBody(REQUEST_BODY_GSON)
                        .withPath(PATH_TO_GSON))
                .respond(response().withBody(JSON_HAS_BEEN_SUCCESSFULLY_POSTED));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/xml")
                        .withBody(REQUEST_BODY_MAPPED)
                        .withPath(PATH_TO_XML))
                .respond(response().withBody(JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/xml")
                        .withBody(REQUEST_BODY_XML_FOR_DOCUMENT)
                        .withPath(PATH_DOCUMENT_XML))
                .respond(response().withBody(DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "multipart/form-data")
                        .withBody(JSOUP_DOCUMENT.outerHtml())
                        .withPath(PATH_DOCUMENT_HTML))
                .respond(response().withBody(DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/x-www-form-urlencoded")
                        .withBody(REQUEST_BODY_URL_UNLOADED)
                        .withPath(PATH_URL_UNLOADED))
                .respond(response().withBody(FORM_HAS_BEEN_SUCCESSFULLY_POSTED));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/x-www-form-urlencoded")
                        .withBody(REQUEST_BODY_URL_UNLOADED2)
                        .withPath(PATH_URL_UNLOADED))
                .respond(response().withBody(FORM_HAS_BEEN_SUCCESSFULLY_POSTED));
    }

    @DataProvider
    public static Object[][] data() {

        return new Object[][]{
                {POST(REQUEST_URI + PATH_TO_GSON, BODY_JSON_OBJECT)
                        .header("Content-Type", "application/json"),
                        JSON_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_TO_XML, BODY_XML_OBJECT)
                        .header("Content-Type", "application/xml"),
                        JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_DOCUMENT_XML, W3C_DOCUMENT)
                        .header("Content-Type", "application/xml"),
                        DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_DOCUMENT_HTML, JSOUP_DOCUMENT)
                        .header("Content-Type", "multipart/form-data"),
                        DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_URL_UNLOADED, FORM_PARAMS)
                        .header("Content-Type", "application/x-www-form-urlencoded"),
                        FORM_HAS_BEEN_SUCCESSFULLY_POSTED},

                {POST(REQUEST_URI + PATH_URL_UNLOADED, FORM_PARAMS2)
                        .header("Content-Type", "application/x-www-form-urlencoded"),
                        FORM_HAS_BEEN_SUCCESSFULLY_POSTED},
        };
    }

    @Test(dataProvider = "data")
    public void customRequestBodyTest(RequestBuilder requestBuilder,
                                      String expectedMessage) {
        assertThat(http().responseOf(requestBuilder, ofString()),
                hasBody(is(expectedMessage)));
    }
}
