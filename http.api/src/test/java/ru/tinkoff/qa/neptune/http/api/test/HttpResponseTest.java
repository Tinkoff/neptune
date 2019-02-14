package ru.tinkoff.qa.neptune.http.api.test;

import org.mockserver.model.HttpResponse;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.HttpStepContext;

import static java.lang.String.format;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.stringBody;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.*;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.responseOf;

public class HttpResponseTest extends BaseHttpTest {

    private static final String URI = format("%s/testResponse.html", REQUEST_URI);
    private HttpStepContext httpSteps = getProxied(HttpStepContext.class);

    private static void prepareResponse(String method, String bodyText) {
        clientAndServer.when(
                request()
                        .withMethod(method)
                        .withPath("/testResponse.html"), exactly(1))
                .respond(HttpResponse.response().withBody(bodyText));
    }

    @Test
    public void getResponseTest() {
        prepareResponse("GET", "Hello get method");
        var response = httpSteps.get(responseOf(GET(URI), ofString()));
        assertThat(response.body(), is("Hello get method"));
    }

    @Test
    public void postResponseTest() {
        prepareResponse("POST", "Hello post method");
        var response = httpSteps.get(responseOf(POST(URI, stringBody("Test body")), ofString()));
        assertThat(response.body(), is("Hello post method"));
    }

    @Test
    public void putResponseTest() {
        prepareResponse("PUT", "Hello put method");
        var response = httpSteps.get(responseOf(PUT(URI, stringBody("Test body")), ofString()));
        assertThat(response.body(), is("Hello put method"));
    }

    @Test
    public void deleteResponseTest() {
        prepareResponse("DELETE", "Hello delete method");
        var response = httpSteps.get(responseOf(DELETE(URI), ofString()));
        assertThat(response.body(), is("Hello delete method"));
    }

    @Test
    public void methodResponseTest() {
        prepareResponse("CUSTOM_METHOD", "Hello custom method");
        var response = httpSteps.get(responseOf(methodRequest(URI, "CUSTOM_METHOD"), ofString()));
        assertThat(response.body(), is("Hello custom method"));
    }
}
