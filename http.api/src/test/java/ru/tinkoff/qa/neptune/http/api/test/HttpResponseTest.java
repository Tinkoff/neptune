package ru.tinkoff.qa.neptune.http.api.test;

import org.mockserver.model.HttpResponse;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.http.api.DesiredResponseHasNotBeenReceivedException;
import ru.tinkoff.qa.neptune.http.api.HttpStepPerformer;

import static java.lang.String.format;
import static java.lang.System.currentTimeMillis;
import static java.lang.System.getProperties;
import static java.lang.System.setProperty;
import static java.net.http.HttpResponse.BodyHandlers.ofString;
import static java.time.Duration.ofSeconds;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.mockserver.matchers.Times.exactly;
import static org.mockserver.model.HttpRequest.request;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.condition;
import static ru.tinkoff.qa.neptune.core.api.steps.proxy.ProxyFactory.getProxied;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.stringBody;
import static ru.tinkoff.qa.neptune.http.api.PreparedHttpRequest.*;
import static ru.tinkoff.qa.neptune.http.api.HttpResponseSequentialGetSupplier.responseOf;
import static ru.tinkoff.qa.neptune.http.api.properties.TimeToGetDesiredResponseProperty.DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeUnitToGetDesiredResponseProperty.TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.time.TimeValueToGetDesiredResponseProperty.TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY;

public class HttpResponseTest extends BaseHttpTest {

    private static final String URI = format("%s/testResponse.html", REQUEST_URI);
    private HttpStepPerformer httpSteps = getProxied(HttpStepPerformer.class);

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
    public void getResponsePositiveConditionalTest() {
        prepareResponse("GET", "Hello get conditional method");

        var response = httpSteps.get(responseOf(GET(URI), ofString())
                .conditionToReceiveDesiredResponse(condition("Body contains 'get conditional method'",
                        stringHttpResponse ->
                        stringHttpResponse.body().contains("get conditional method"))));
        assertThat(response.body(), is("Hello get conditional method"));
    }

    @Test
    public void getResponseNegativeConditionalWithNullValueAndDefaultTimeTest() {
        prepareResponse("GET", "Bad GET conditional method 1");

        var start = currentTimeMillis();
        var response = httpSteps.get(responseOf(GET(URI), ofString())
                .conditionToReceiveDesiredResponse(condition("Body contains 'get conditional method'",
                        stringHttpResponse ->
                                stringHttpResponse.body().contains("get conditional method"))));
        var stop = currentTimeMillis();

        assertThat(response, nullValue());

        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void getResponseNegativeConditionalWithNullValueAndDefinedInPropertiesTimeTest() {
        prepareResponse("GET", "Bad GET conditional method 2");
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            var response = httpSteps.get(responseOf(GET(URI), ofString())
                    .conditionToReceiveDesiredResponse(condition("Body contains 'get conditional method'",
                            stringHttpResponse ->
                                    stringHttpResponse.body().contains("get conditional method"))));
            var stop = currentTimeMillis();

            assertThat(response, nullValue());

            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void getResponseNegativeConditionalWithNullValueAndDefinedExplicitlyTimeTest() {
        prepareResponse("GET", "Bad GET conditional method 3");
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            var response = httpSteps.get(responseOf(GET(URI), ofString())
                    .conditionToReceiveDesiredResponse(condition("Body contains 'get conditional method'",
                            stringHttpResponse ->
                                    stringHttpResponse.body().contains("get conditional method")))
                    .timeToReceiveDesiredResponse(ofSeconds(5)));
            var stop = currentTimeMillis();

            assertThat(response, nullValue());

            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void getResponseNegativeConditionalWithExceptionThrowing() {
        prepareResponse("GET", "Bad GET conditional method 4");

        var start = currentTimeMillis();
        long stop = -1;
        try {
            httpSteps.get(responseOf(GET(URI), ofString())
                    .conditionToReceiveDesiredResponse(condition("Body contains 'get conditional method'",
                            stringHttpResponse ->
                                    stringHttpResponse.body().contains("get conditional method")))
                    .timeToReceiveDesiredResponse(ofSeconds(5))
                    .toThrowIfNotReceived(() -> new DesiredResponseHasNotBeenReceivedException("Test exception")));
        }
        catch (Throwable t) {
            stop = currentTimeMillis();
            throw t;
        }
        finally {
            if (stop < 0) {
                stop = currentTimeMillis();
            }
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        }
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

    @Test
    public void methodResponsePositiveConditionalTest() {
        prepareResponse("CUSTOM_METHOD", "Hello custom method conditional method");

        var response = httpSteps.get(responseOf(methodRequest(URI, "CUSTOM_METHOD"), ofString())
                .conditionToReceiveDesiredResponse(condition("Body contains 'method conditional method'",
                        stringHttpResponse ->
                                stringHttpResponse.body().contains("method conditional method"))));
        assertThat(response.body(), is("Hello custom method conditional method"));
    }

    @Test
    public void methodResponseNegativeConditionalWithNullValueAndDefaultTimeTest() {
        prepareResponse("CUSTOM_METHOD", "Bad CUSTOM_METHOD conditional method 1");

        var start = currentTimeMillis();
        var response = httpSteps.get(responseOf(methodRequest(URI, "CUSTOM_METHOD"), ofString())
                .conditionToReceiveDesiredResponse(condition("Body contains 'custom method conditional method'",
                        stringHttpResponse ->
                                stringHttpResponse.body().contains("custom method conditional method"))));
        var stop = currentTimeMillis();

        assertThat(response, nullValue());

        var time = stop - start;
        assertThat(time, lessThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis() + 500));
        assertThat(time, greaterThanOrEqualTo(DEFAULT_TIME_TO_GET_DESIRED_RESPONSE_PROPERTY.get().toMillis()));
    }

    @Test
    public void methodResponseNegativeConditionalWithNullValueAndDefinedInPropertiesTimeTest() {
        prepareResponse("CUSTOM_METHOD", "Bad CUSTOM_METHOD conditional method 2");
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            var response = httpSteps.get(responseOf(methodRequest(URI, "CUSTOM_METHOD"), ofString())
                    .conditionToReceiveDesiredResponse(condition("Body contains 'custom method conditional method'",
                            stringHttpResponse ->
                                    stringHttpResponse.body().contains("custom method conditional method"))));
            var stop = currentTimeMillis();

            assertThat(response, nullValue());

            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(10).toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(10).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test
    public void methodResponseNegativeConditionalWithNullValueAndDefinedExplicitlyTimeTest() {
        prepareResponse("CUSTOM_METHOD", "Bad CUSTOM_METHOD conditional method 3");
        setProperty(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), SECONDS.name());
        setProperty(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName(), "10");

        try {
            var start = currentTimeMillis();
            var response = httpSteps.get(responseOf(GET(URI), ofString())
                    .conditionToReceiveDesiredResponse(condition("Body contains 'custom method conditional method'",
                            stringHttpResponse ->
                                    stringHttpResponse.body().contains("custom method conditional method")))
                    .timeToReceiveDesiredResponse(ofSeconds(5)));
            var stop = currentTimeMillis();

            assertThat(response, nullValue());

            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        }
        finally {
            getProperties().remove(TIME_UNIT_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
            getProperties().remove(TIME_VALUE_TO_GET_DESIRED_RESPONSE_PROPERTY.getPropertyName());
        }
    }

    @Test(expectedExceptions = DesiredResponseHasNotBeenReceivedException.class, expectedExceptionsMessageRegExp = "Test exception")
    public void methodResponseNegativeConditionalWithExceptionThrowing() {
        prepareResponse("CUSTOM_METHOD", "Bad CUSTOM_METHOD conditional method 4");

        var start = currentTimeMillis();
        long stop = -1;
        try {
            httpSteps.get(responseOf(GET(URI), ofString())
                    .conditionToReceiveDesiredResponse(condition("Body contains 'custom method conditional method'",
                            stringHttpResponse ->
                                    stringHttpResponse.body().contains("custom method conditional method")))
                    .timeToReceiveDesiredResponse(ofSeconds(5))
                    .toThrowIfNotReceived(() -> new DesiredResponseHasNotBeenReceivedException("Test exception")));
        }
        catch (Throwable t) {
            stop = currentTimeMillis();
            throw t;
        }
        finally {
            if (stop < 0) {
                stop = currentTimeMillis();
            }
            var time = stop - start;
            assertThat(time, lessThanOrEqualTo(ofSeconds(5).toMillis() + 500));
            assertThat(time, greaterThanOrEqualTo(ofSeconds(5).toMillis()));
        }
    }
}
