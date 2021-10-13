package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.web.testclient.captors.WebTestClientStringCaptor;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.*;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetArrayFromResponse.array;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetIterableFromResponse.iterable;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBody.objectFromBody;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBody.responseBody;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBodyArray.objectFromArray;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBodyIterable.objectFromIterable;
import static ru.tinkoff.qa.neptune.spring.web.testclient.LogWebTestClientExpectation.logExpectation;

@SuppressWarnings("unchecked")
@Description("Send request and then get response")
public final class SendRequestAction<B> extends SequentialActionSupplier<WebTestClientContext, WebTestClient, SendRequestAction<B>> {

    private static final Function<WebTestClient.ResponseSpec, Byte[]> DEFAULT_BODY_FORMAT =
            responseSpec -> bytePrimitiveToByteWrapperArray(responseSpec.expectBody().returnResult().getResponseBody());

    private final Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec;
    final LinkedList<Expectation<?>> assertions = new LinkedList<>();
    private final List<AssertionError> errors = new LinkedList<>();
    private WebTestClient.ResponseSpec responseSpec;
    private Function<WebTestClient.ResponseSpec, B> bodyFormat;

    @CaptureOnSuccess(by = WebTestClientStringCaptor.class)
    @CaptureOnFailure(by = WebTestClientStringCaptor.class)
    private ExchangeResult result;

    private SendRequestAction(Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec) {
        checkNotNull(requestSpec);
        this.requestSpec = requestSpec;
    }

    private static Byte[] bytePrimitiveToByteWrapperArray(byte[] bytes) {
        if (bytes == null) {
            return null;
        }

        var result = new Byte[]{};
        for (var b : bytes) {
            result = add(result, b);
        }

        return result;
    }

    private <T> SendRequestAction<B> addExpectation(Function<WebTestClient.ResponseSpec, T> f, String description) {
        assertions.add(new Expectation<>(new Function<WebTestClient.ResponseSpec, T>() {
            @Override
            public T apply(WebTestClient.ResponseSpec spec) {
                return f.apply(spec);
            }

            @Override
            public String toString() {
                return description;
            }
        }));
        return this;
    }

    public static SendRequestAction<Byte[]> send(Function<WebTestClient,
            WebTestClient.RequestHeadersSpec<?>> requestSpec) {
        return new SendRequestAction<Byte[]>(requestSpec)
                .performOn(WebTestClientContext::getDefaultWebTestClient);
    }

    public static SendRequestAction<Byte[]> send(WebTestClient client,
                                                 Function<WebTestClient,
                                                         WebTestClient.RequestHeadersSpec<?>> requestSpec) {
        checkNotNull(client);
        return new SendRequestAction<Byte[]>(requestSpec).performOn(webTestClientContext -> client);
    }

    public SendRequestAction<B> expectStatus(Function<StatusAssertions, WebTestClient.ResponseSpec> statusCheck) {
        return addExpectation(spec -> statusCheck.apply(spec.expectStatus()), new ExpectResponseStatus().toString());
    }

    public SendRequestAction<B> expectHeader(Function<HeaderAssertions, WebTestClient.ResponseSpec> headerCheck) {
        return addExpectation(spec -> headerCheck.apply(spec.expectHeader()), new ExpectResponseHeaders().toString());
    }

    public SendRequestAction<B> expectCookie(Function<CookieAssertions, WebTestClient.ResponseSpec> cookieCheck) {
        return addExpectation(spec -> cookieCheck.apply(spec.expectCookie()), new ExpectResponseCookies().toString());
    }

    public SendRequestAction<B> expectBodyJson(String expectedJson) {
        return addExpectation(spec -> spec.expectBody().json(expectedJson), new ExpectedBodyJson(expectedJson).toString());
    }

    public SendRequestAction<B> expectBodyXml(String expectedXml) {
        return addExpectation(spec -> spec.expectBody().xml(expectedXml), new ExpectedBodyXml(expectedXml).toString());
    }

    public <T> SendRequestAction<B> expectBodyJsonPath(String expression, Function<JsonPathAssertions, T> assertion, Object... args) {
        return addExpectation(spec -> assertion.apply(spec.expectBody().jsonPath(expression, args)),
                new ExpectJsonPath(expression, args).toString());
    }

    public <T> SendRequestAction<B> expectBodyXpath(String expression,
                                                    Function<XpathAssertions, T> assertion,
                                                    Object... args) {
        return addExpectation(spec -> assertion.apply(spec.expectBody().xpath(expression, args)),
                new ExpectXpath(expression, null, args).toString());
    }

    public <T> SendRequestAction<B> expectBodyXpath(String expression,
                                                    Function<XpathAssertions, T> assertion,
                                                    Map<String, String> namespaces,
                                                    Object... args) {
        return addExpectation(spec -> assertion.apply(spec.expectBody().xpath(expression, namespaces, args)),
                new ExpectXpath(expression, namespaces, args).toString());
    }

    private <T> SendRequestAction<B> expectBody(String description, Function<byte[], T> f, Matcher<? super T> matcher) {
        return addExpectation(responseSpec -> responseSpec
                        .expectBody()
                        .consumeWith(entityExchangeResult -> matcher.matches(f.apply(entityExchangeResult.getResponseBody()))),
                description + " " + matcher.toString());
    }

    public SendRequestAction<B> expectBodyStringContent(Matcher<? super String> matcher) {
        return expectBody(new StringContent().toString(), String::new, matcher);
    }

    public SendRequestAction<B> expectBodyByteContent(Matcher<? super Byte[]> matcher) {
        return expectBody(new ByteContent().toString(),
                SendRequestAction::bytePrimitiveToByteWrapperArray, matcher);
    }

    private <T> SendRequestAction<T> setBodyFormat(Function<WebTestClient.ResponseSpec, T> f, String description) {
        var result = (SendRequestAction<T>) this;
        result.bodyFormat = new Function<>() {
            @Override
            public T apply(WebTestClient.ResponseSpec spec) {
                return f.apply(spec);
            }

            @Override
            public String toString() {
                return description;
            }
        };
        return result;
    }

    public SendRequestAction<Void> emptyBody() {
        return setBodyFormat(spec -> spec.expectBody().isEmpty().getResponseBody(), new ExpectEmptyBody().toString());
    }

    public SendRequestAction<Byte[]> hasBody() {
        return setBodyFormat(spec -> DEFAULT_BODY_FORMAT.andThen(bytes -> {
            assertThat(new ExpectAnyBody().toString(), bytes, not(nullValue()));
            return bytes;
        }).apply(spec), new ExpectAnyBody().toString());
    }

    public <T> SendRequestAction<T> bodyAs(Class<T> tClass) {
        return setBodyFormat(spec -> {
            var body = spec.expectBody(tClass).returnResult().getResponseBody();
            assertThat(body, not(nullValue()));
            return body;
        }, new ExpectedBodyOfClass(tClass).toString());
    }

    public <T> SendRequestAction<T> bodyAs(ParameterizedTypeReference<T> type) {
        return setBodyFormat(spec -> {
            var body = spec.expectBody(type).returnResult().getResponseBody();
            assertThat(body, not(nullValue()));
            return body;
        }, new ExpectedBodyOfType(type).toString());
    }

    public <T> SendRequestAction<List<T>> bodyAsListOf(Class<T> itemClass) {
        return setBodyFormat(spec -> {
            var body = spec.expectBodyList(itemClass).returnResult().getResponseBody();
            assertThat(body, not(nullValue()));
            return body;
        }, new ExpectedBodyListOfClass(itemClass).toString());
    }

    public <T> SendRequestAction<List<T>> bodyAsListOf(ParameterizedTypeReference<T> itemType) {
        return setBodyFormat(spec -> {
            var body = spec.expectBodyList(itemType).returnResult().getResponseBody();
            assertThat(body, not(nullValue()));
            return body;
        }, new ExpectedBodyListOfType(itemType).toString());
    }

    @Override
    protected void howToPerform(WebTestClient value) {
        errors.clear();
        result = null;
        responseSpec = requestSpec.apply(value).exchange();
        result = responseSpec.returnResult(Void.class);

        if (bodyFormat != null) {
            assertions.addLast(new Expectation<>(bodyFormat));
        }

        assertions.forEach(ex -> {
            try {
                ex.verify(responseSpec);
                logExpectation(ex).get().performAction(ex);
            } catch (AssertionError e) {
                errors.add(e);
            }
        });

        if (bodyFormat != null) {
            assertions.removeLast();
        }

        if (errors.size() == 0) {
            return;
        }

        var messageBuilder = new StringBuilder();
        messageBuilder.append("Mismatches: ");
        errors.forEach(e -> messageBuilder.append("\r\n").append("\r\n").append(e.getMessage()));
        throw new AssertionError(messageBuilder.toString());
    }

    @Override
    protected Map<String, String> additionalParameters() {
        if (result == null) {
            return super.additionalParameters();
        }

        var map = new LinkedHashMap<String, String>();
        map.put("URL", result.getUrl().toString());
        map.put("Method", result.getMethod().toString());
        var headers = result.getRequestHeaders();

        headers.forEach((k, v) -> map.put(k, String.join(";", v)));
        return map;
    }

    B getBody() {
        if (bodyFormat == null) {
            return (B) DEFAULT_BODY_FORMAT.apply(responseSpec);
        }

        return bodyFormat.apply(responseSpec);
    }

    public GetObjectFromResponseBody<B> thenGetBody() {
        return responseBody(new FromBodyGet<>(this, b -> b));
    }

    public <T> GetObjectFromResponseBody<T> thenGetValue(String description, Function<B, T> howToGet) {
        return objectFromBody(description, new FromBodyGet<>(this, howToGet));
    }

    public <T, S extends Iterable<T>> GetIterableFromResponse<T, S> thenGetIterable(String description,
                                                                                    Function<B, S> howToGet) {
        return iterable(description, new FromBodyGet<>(this, howToGet));
    }

    public <T> GetArrayFromResponse<T> thenGetArray(String description, Function<B, T[]> howToGet) {
        return array(description, new FromBodyGet<>(this, howToGet));
    }

    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            Function<B, S> howToGet) {
        return objectFromIterable(description, new FromBodyGet<>(this, howToGet));
    }

    public <T> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            Function<B, T[]> howToGet) {
        return objectFromArray(description, new FromBodyGet<>(this, howToGet));
    }
}
