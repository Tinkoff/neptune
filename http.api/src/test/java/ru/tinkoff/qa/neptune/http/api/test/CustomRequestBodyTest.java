package ru.tinkoff.qa.neptune.http.api.test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import ru.tinkoff.qa.neptune.http.api.test.request.body.BodyObject;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.io.StringReader;
import java.net.http.HttpRequest;
import java.util.LinkedHashMap;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;
import static ru.tinkoff.qa.neptune.http.api.request.CommonBodyPublishers.*;
import static ru.tinkoff.qa.neptune.http.api.request.PostRequest.POST;

public class CustomRequestBodyTest extends BaseHttpTest {

    private static final BodyObject BODY_OBJECT = new BodyObject().setA("Some String")
            .setB(666)
            .setC(true);

    private static final String PATH_TO_GSON = "/gson";
    private static final String PATH_TO_JACKSON = "/jackson_xml";
    private static final String PATH_DOCUMENT_XML = "/document_xml";
    private static final String PATH_DOCUMENT_HTML = "/document_html";
    private static final String PATH_URL_UNLOADED = "/urlencoded";

    private static final String REQUEST_BODY_GSON = "{\"A\":\"Some String\",\"B\":666,\"C\":true}";
    private static final String REQUEST_BODY_MAPPED = "<BodyObject><wstxns1:A1 xmlns:wstxns1=\"http://www.test.com\">Some String</wstxns1:A1>" +
            "<wstxns2:B1 xmlns:wstxns2=\"http://www.test.com\">666</wstxns2:B1>" +
            "<wstxns3:C1 xmlns:wstxns3=\"http://www.test.com\">true</wstxns3:C1></BodyObject>";

    private static final String REQUEST_BODY_XML_FOR_DOCUMENT = "<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><a><b/><c/></a>";
    private static final Document DOCUMENT = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
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

    private static final String REQUEST_BODY_URL_UNLOADED = "param1=value1&param2=value2";
    private static final String REQUEST_BODY_URL_UNLOADED2 = "chip%26dale=rescue+rangers&how+to+get+water=2H2+%2B+O2+%3D+2H2O";

    private static final String JSON_HAS_BEEN_SUCCESSFULLY_POSTED = "Json has been successfully posted";
    private static final String JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED = "Jackson xml has been successfully posted";
    private static final String DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED = "Document xml has been successfully posted";
    private static final String DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED = "Document html has been successfully posted";
    private static final String FORM_HAS_BEEN_SUCCESSFULLY_POSTED = "Form has been successfully posted";


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
                        .withPath(PATH_TO_JACKSON))
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
                        .withBody(DOCUMENT.outerHtml())
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
    public static Object[][] data() throws Exception {
        var module = new JaxbAnnotationModule();

        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var inputSource = new InputSource(new StringReader(REQUEST_BODY_XML_FOR_DOCUMENT));
        var doc = documentBuilder.parse(inputSource);

        var params = new LinkedHashMap<String, String>();
        params.put("param1", "value1");
        params.put("param2", "value2");

        var params2 = new LinkedHashMap<String, String>();
        params2.put("chip&dale", "rescue rangers");
        params2.put("how to get water", "2H2 + O2 = 2H2O");

        return new Object[][]{
                {PATH_TO_GSON,
                        jsonStringBody(BODY_OBJECT, new GsonBuilder()),
                        "application/json",
                        JSON_HAS_BEEN_SUCCESSFULLY_POSTED},

                {PATH_TO_JACKSON,
                        serializedStringBody(BODY_OBJECT, new XmlMapper().registerModule(module)),
                        "application/xml",
                        JACKSON_XML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {PATH_DOCUMENT_XML,
                        documentStringBody(doc, TransformerFactory.newInstance().newTransformer()),
                        "application/xml",
                        DOCUMENT_XML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {PATH_DOCUMENT_HTML,
                        documentStringBody(DOCUMENT),
                        "multipart/form-data",
                        DOCUMENT_HTML_HAS_BEEN_SUCCESSFULLY_POSTED},

                {PATH_URL_UNLOADED,
                        formUrlEncodedStringParamsBody(params),
                        "application/x-www-form-urlencoded",
                        FORM_HAS_BEEN_SUCCESSFULLY_POSTED},

                {PATH_URL_UNLOADED,
                        formUrlEncodedStringParamsBody(params2),
                        "application/x-www-form-urlencoded",
                        FORM_HAS_BEEN_SUCCESSFULLY_POSTED},
        };
    }

    @Test(dataProvider = "data")
    public void customRequestBodyTest(String urlPath,
                                      HttpRequest.BodyPublisher bodyPublisher,
                                      String contentType,
                                      String expectedMessage) {
        assertThat(http().responseOf(
                POST(REQUEST_URI + urlPath,
                        bodyPublisher)
                        .header("Content-Type", contentType),
                ofString()),
                hasBody(is(expectedMessage)));
    }
}
