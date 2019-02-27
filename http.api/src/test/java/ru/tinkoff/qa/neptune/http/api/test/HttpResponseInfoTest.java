package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import java.net.URI;

import static java.lang.String.format;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.stringBody;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.GET;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.POST;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseInfoSequentialGetSupplier.*;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.responseOf;

public class HttpResponseInfoTest extends BaseHttpTest {

    private HttpStepContext httpSteps = getProxied(HttpStepContext.class);

    @Test
    public void statusCodeResponseSupplierTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/testStatusCode.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(httpSteps.get(statusCodeOf(responseOf(GET(format("%s/testStatusCode.html", REQUEST_URI)),
                ofString()))),
                equalTo(200));
    }

    @Test
    public void statusCodeResponseTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/testStatusCodeDoesNotExist.html"), exactly(1))
                .respond(response().withBody("NOT FOUND").withStatusCode(404));

        var response = httpSteps.get(responseOf(GET(format("%s/testStatusCodeDoesNotExist.html", REQUEST_URI))));
        assertThat(httpSteps.get(statusCodeOf(response)),
                equalTo(404));
    }

    @Test
    public void correspondingRequestSupplierTest() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/correspondingRequest.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(httpSteps.get(
                correspondingRequestOf(responseOf(POST(format("%s/correspondingRequest.html", REQUEST_URI),
                        stringBody("Request body"))))).toString(),
                equalTo("http://127.0.0.1:1080/correspondingRequest.html POST"));
    }

    @Test
    public void correspondingRequestTest() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/correspondingRequest2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        var response = httpSteps.get(responseOf(POST(format("%s/correspondingRequest2.html", REQUEST_URI),
                stringBody("Request body"))));

        assertThat(httpSteps.get(
                correspondingRequestOf(response)).toString(),
                equalTo("http://127.0.0.1:1080/correspondingRequest2.html POST"));
    }


    @Test
    public void headersSupplierTest() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/header.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(httpSteps.get(
                headersOf(responseOf(POST(format("%s/header.html", REQUEST_URI), stringBody("Request body"))))),
                allOf(hasEntry(equalTo("connection"), contains("keep-alive")),
                        hasEntry(equalTo("content-length"), contains("7"))));
    }

    @Test
    public void headersTest() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/header2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        var response = httpSteps.get(responseOf(POST(format("%s/header2.html", REQUEST_URI),
                stringBody("Request body"))));

        assertThat(httpSteps.get(headersOf(response)),
                allOf(hasEntry(equalTo("connection"), contains("keep-alive")),
                        hasEntry(equalTo("content-length"), contains("7"))));
    }

    @Test
    public void bodySupplierTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/body.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(httpSteps.get(bodyOf(responseOf(GET(format("%s/body.html", REQUEST_URI)), ofString()))),
                equalTo("SUCCESS"));
    }

    @Test
    public void bodyTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/body2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        var response = httpSteps.get(responseOf(GET(format("%s/body2.html", REQUEST_URI)), ofString()));

        assertThat(httpSteps.get(bodyOf(response)), equalTo("SUCCESS"));
    }

    @Test
    public void uriSupplierTest() throws Throwable {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/uri.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(httpSteps.get(uriOf(responseOf(GET(format("%s/uri.html", REQUEST_URI))))),
                equalTo(new URI("http://127.0.0.1:1080/uri.html")));
    }

    @Test
    public void uriTest()  throws Throwable {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/uri2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        var response = httpSteps.get(responseOf(GET(format("%s/uri2.html", REQUEST_URI))));

        assertThat(httpSteps.get(uriOf(response)), equalTo(new URI("http://127.0.0.1:1080/uri2.html")));
    }

    @Test
    public void versionSupplierTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/version.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(httpSteps.get(httpVersionOf(responseOf(GET(format("%s/version.html", REQUEST_URI))))),
                equalTo(HTTP_1_1));
    }

    @Test
    public void versionTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/version2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        var response = httpSteps.get(responseOf(GET(format("%s/version2.html", REQUEST_URI))));

        assertThat(httpSteps.get(httpVersionOf(response)), equalTo(HTTP_1_1));
    }
}
