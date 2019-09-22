package ru.tinkoff.qa.neptune.http.api.test;

import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.module.jaxb.JaxbAnnotationModule;
import com.google.gson.GsonBuilder;
import org.jsoup.Jsoup;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xml.sax.InputSource;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;
import ru.tinkoff.qa.neptune.http.api.test.request.body.BodyObject;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerFactory;
import java.io.StringReader;
import java.util.LinkedHashMap;
import java.util.Map;

import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.*;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseInfoSequentialGetSupplier.bodyOf;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.responseOf;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.POST;

public class CustomRequestBodyTest extends BaseHttpTest {

    private HttpStepContext httpSteps = getProxied(HttpStepContext.class);

    private static final BodyObject BODY_OBJECT = new BodyObject().setA("Some String")
            .setB(666)
            .setC(true);

    @BeforeClass
    public static void beforeClass() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/json")
                        .withBody("{\"A\":\"Some String\",\"B\":666,\"C\":true}")
                        .withPath("/gson"))
                .respond(response().withBody("Json has been successfully posted"));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<BodyObject><wstxns1:A1 xmlns:wstxns1=\"http://www.test.com\">Some String</wstxns1:A1><wstxns2:B1 xmlns:wstxns2=\"http://www.test.com\">666</wstxns2:B1><wstxns3:C1 xmlns:wstxns3=\"http://www.test.com\">true</wstxns3:C1></BodyObject>")
                        .withPath("/jackson_xml"))
                .respond(response().withBody("Jackson xml has been successfully posted"));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/xml")
                        .withBody("<?xml version=\"1.0\" encoding=\"utf-8\" standalone=\"no\"?><a><b/><c/></a>")
                        .withPath("/document_xml"))
                .respond(response().withBody("Document xml has been successfully posted"));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "multipart/form-data")
                        .withBody("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
                                "<html>\n" +
                                " <head> \n" +
                                "  <meta http-equiv=\"Content-Type\" content=\"text/html; charset=ISO-8859-1\"> \n" +
                                "  <title>Login Page</title> \n" +
                                " </head> \n" +
                                " <body> \n" +
                                "  <div id=\"login\" class=\"simple\"> \n" +
                                "   <form action=\"login.do\">\n" +
                                "     Username : \n" +
                                "    <input id=\"username\" type=\"text\">\n" +
                                "    <br> Password : \n" +
                                "    <input id=\"password\" type=\"password\">\n" +
                                "    <br> \n" +
                                "    <input id=\"submit\" type=\"submit\"> \n" +
                                "    <input id=\"reset\" type=\"reset\"> \n" +
                                "   </form> \n" +
                                "  </div>  \n" +
                                " </body>\n" +
                                "</html>")
                        .withPath("/document_html"))
                .respond(response().withBody("Document html has been successfully posted"));

        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withHeader("Content-Type", "application/x-www-form-urlencoded")
                        .withBody("param1=value1&param2=value2")
                        .withPath("/urlencoded"))
                .respond(response().withBody("Form has been successfully posted"));
    }

    @Test
    public void ofGsonBodyTest() {
        assertThat(httpSteps.get(bodyOf(responseOf(
                POST(REQUEST_URI + "/gson",
                        jsonStringBody(BODY_OBJECT, new GsonBuilder()))
                        .header("Content-Type", "application/json"),
                ofString()))),
                is("Json has been successfully posted"));
    }

    @Test
    public void testOfXmlMappedBodyTest() {
        JaxbAnnotationModule module = new JaxbAnnotationModule();
        assertThat(httpSteps.get(bodyOf(responseOf(
                POST(REQUEST_URI + "/jackson_xml",
                        serializedStringBody(BODY_OBJECT, new XmlMapper().registerModule(module)))
                        .header("Content-Type", "application/xml"),
                ofString()))),
                is("Jackson xml has been successfully posted"));
    }

    @Test
    public void ofXmlDocumentBodyTest() throws Exception {
        var documentBuilderFactory = DocumentBuilderFactory.newInstance();
        var documentBuilder = documentBuilderFactory.newDocumentBuilder();
        var inputSource = new InputSource(new StringReader("<?xml version=\"1.0\" encoding=\"utf-8\"?><a><b></b><c></c></a>"));
        var doc = documentBuilder.parse(inputSource);

        assertThat(httpSteps.get(bodyOf(responseOf(
                POST(REQUEST_URI + "/document_xml",
                        documentStringBody(doc, TransformerFactory.newInstance().newTransformer()))
                        .header("Content-Type", "application/xml"),
                ofString()))),
                is("Document xml has been successfully posted"));
    }

    @Test
    public void ofHtmlDocumentBodyTest() {
        var doc = Jsoup.parse("<!DOCTYPE html PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\" \"http://www.w3.org/TR/html4/loose.dtd\">\n" +
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

        assertThat(httpSteps.get(bodyOf(responseOf(
                POST(REQUEST_URI + "/document_html",
                        documentStringBody(doc))
                        .header("Content-Type", "multipart/form-data"),
                ofString()))),
                is("Document html has been successfully posted"));
    }

    @Test
    public void ofFormUrlEncodedStringParamsBodyTest() {
        var params = new LinkedHashMap<String, String>();
        params.put("param1", "value1");
        params.put("param2", "value2");

        assertThat(httpSteps.get(bodyOf(responseOf(
                POST(REQUEST_URI + "/urlencoded",
                        formUrlEncodedStringParamsBody(params))
                        .header("Content-Type", "application/x-www-form-urlencoded"),
                ofString()))),
                is("Form has been successfully posted"));
    }
}
