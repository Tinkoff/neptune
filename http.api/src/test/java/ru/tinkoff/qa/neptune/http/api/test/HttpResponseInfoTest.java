package ru.tinkoff.qa.neptune.http.api.test;

import org.testng.annotations.Test;

import static java.lang.String.format;
import static java.net.http.HttpClient.Version.HTTP_1_1;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasHostMatcher.uriHasHost;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPathMatcher.uriHasPath;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasPortMatcher.uriHasPort;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasQueryMatcher.uriHasQuery;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.resorce.locator.HasSchemeMatcher.uriHasScheme;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.stringBody;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.responseOf;
import static ru.tinkoff.qa.neptune.http.api.HttpStepContext.http;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.GET;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.POST;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasBody.hasBody;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasHeaders.hasHeader;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasPreviousResponse.hasPreviousResponse;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasStatusCode.hasStatusCode;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasURI.hasURI;
import static ru.tinkoff.qa.neptune.http.api.hamcrest.response.HasVersion.hasVersion;

public class HttpResponseInfoTest extends BaseHttpTest {

    @Test
    public void statusCode() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/testStatusCode.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(http().get(responseOf(GET(format("%s/testStatusCode.html", REQUEST_URI)),
                ofString())),
                hasStatusCode(200));
    }


    @Test
    public void headersTest() {
        clientAndServer.when(
                request()
                        .withMethod("POST")
                        .withPath("/header2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));


        assertThat(http().get(responseOf(POST(format("%s/header2.html", REQUEST_URI),
                stringBody("Request body")))),
                allOf(
                        hasHeader("connection", contains("keep-alive")),
                        hasHeader("content-length", of("7")),
                        hasHeader("content-length", not(emptyIterable()))
                ));
    }

    @Test
    public void bodyTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/body2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));


        assertThat(http().get(responseOf(GET(format("%s/body2.html", REQUEST_URI)), ofString())),
                hasBody("SUCCESS"));
    }

    @Test
    public void uriTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/uri.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(http().get(responseOf(GET(format("%s/uri.html", REQUEST_URI)))),
                hasURI(allOf(uriHasScheme("http"),
                        uriHasHost("127.0.0.1"),
                        uriHasPort(1080),
                        uriHasPath("/uri.html"),
                        uriHasQuery(nullValue())
                )));
    }

    @Test
    public void versionTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/version.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(http().get(responseOf(GET(format("%s/version.html", REQUEST_URI)))),
                hasVersion(HTTP_1_1));
    }


    @Test
    public void previousResponseTest() {
        clientAndServer.when(
                request()
                        .withMethod("GET")
                        .withPath("/version2.html"), exactly(1))
                .respond(response().withBody("SUCCESS"));

        assertThat(http().get(responseOf(GET(format("%s/version2.html", REQUEST_URI)))),
                not(hasPreviousResponse()));
    }
}
