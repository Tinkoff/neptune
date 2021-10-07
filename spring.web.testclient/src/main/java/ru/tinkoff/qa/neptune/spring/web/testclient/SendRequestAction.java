package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.lang.Nullable;
import org.springframework.test.web.reactive.server.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.spring.web.testclient.captors.WebTestClientStringCaptor;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ArrayUtils.add;
import static ru.tinkoff.qa.neptune.spring.web.testclient.CheckWebTestClientExpectation.checkExpectation;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetArrayFromResponse.array;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetIterableFromResponse.iterable;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBody.objectFromBody;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBody.responseBody;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBodyIterable.objectFromIterable;

@SuppressWarnings("unchecked")
@SequentialActionSupplier.DefinePerformImperativeParameterName("Send request and then get response")
public final class SendRequestAction<B> extends SequentialActionSupplier<WebTestClientContext, WebTestClient, SendRequestAction<B>> {

    private static final Function<WebTestClient.ResponseSpec, Byte[]> DEFAULT_BODY_FORMAT =
            responseSpec -> bytePrimitiveToByteWrapperArray(responseSpec.expectBody().returnResult().getResponseBody());

    private final Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec;
    private final LinkedList<CheckWebTestClientExpectation<?>> assertions = new LinkedList<>();
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

    private <T> SendRequestAction<B> addExpectation(Function<WebTestClient.ResponseSpec, T> f) {
        assertions.add(checkExpectation(f));
        return this;
    }

    <T, R> SendRequestAction<B> addExpectation(Function<WebTestClient.ResponseSpec, T> start,
                                               Function<T, R> end) {
        return addExpectation(start.andThen(end));
    }

    public SendRequestAction<B> expectStatus(Function<StatusAssertions, WebTestClient.ResponseSpec> statusCheck) {
        return addExpectation(WebTestClient.ResponseSpec::expectStatus, statusCheck);
    }

    public SendRequestAction<B> expectHeader(Function<HeaderAssertions, WebTestClient.ResponseSpec> statusCheck) {
        return addExpectation(WebTestClient.ResponseSpec::expectHeader, statusCheck);
    }

    public SendRequestAction<B> expectCookie(Function<CookieAssertions, WebTestClient.ResponseSpec> statusCheck) {
        return addExpectation(WebTestClient.ResponseSpec::expectCookie, statusCheck);
    }

    public SendRequestAction<B> expectJson(String expectedJson) {
        return addExpectation(WebTestClient.ResponseSpec::expectBody, spec -> spec.json(expectedJson));
    }

    public SendRequestAction<B> expectXml(String expectedXml) {
        return addExpectation(WebTestClient.ResponseSpec::expectBody, spec -> spec.xml(expectedXml));
    }

    public SendRequestAction<B> expectJsonPath(String expression, Object... args) {
        return addExpectation(WebTestClient.ResponseSpec::expectBody, spec -> spec.jsonPath(expression, args));
    }

    public SendRequestAction<B> expectXpath(String expression, Object... args) {
        return addExpectation(WebTestClient.ResponseSpec::expectBody, spec -> spec.xpath(expression, args));
    }

    public SendRequestAction<B> expectXpath(String expression, @Nullable Map<String, String> namespaces, Object... args) {
        return addExpectation(WebTestClient.ResponseSpec::expectBody, spec -> spec.xpath(expression, namespaces, args));
    }

    private <T> SendRequestAction<B> expectBody(String description, Function<byte[], T> f, Matcher<? super T> matcher) {
        return addExpectation(new Function<WebTestClient.ResponseSpec, WebTestClient.BodyContentSpec>() {
            @Override
            public WebTestClient.BodyContentSpec apply(WebTestClient.ResponseSpec responseSpec) {
                return responseSpec.expectBody().consumeWith(entityExchangeResult -> matcher.matches(f.apply(entityExchangeResult.getResponseBody())));
            }

            public String toString() {
                return description + " " + matcher.toString();
            }
        });
    }

    public SendRequestAction<B> expectBodyString(Matcher<? super String> matcher) {
        return expectBody(new StringContent().toString(), String::new, matcher);
    }

    public SendRequestAction<B> expectBodyBytes(Matcher<? super Byte[]> matcher) {
        return expectBody(new StringContent().toString(),
                SendRequestAction::bytePrimitiveToByteWrapperArray, matcher);
    }

    public SendRequestAction<Void> expectEmptyBody() {
        var result = (SendRequestAction<Void>) this;
        result.bodyFormat = spec -> spec.expectBody().isEmpty().getResponseBody();
        return result;
    }

    public <T> SendRequestAction<T> expectBodyAs(Class<T> tClass) {
        var result = (SendRequestAction<T>) this;
        result.bodyFormat = spec -> spec.expectBody(tClass).returnResult().getResponseBody();
        return result;
    }

    public <T> SendRequestAction<T> expectBodyAs(ParameterizedTypeReference<T> type) {
        var result = (SendRequestAction<T>) this;
        result.bodyFormat = spec -> spec.expectBody(type).returnResult().getResponseBody();
        return result;
    }

    public <T> SendRequestAction<List<T>> expectBodyAsListOf(Class<T> itemClass) {
        var result = (SendRequestAction<List<T>>) this;
        result.bodyFormat = spec -> spec.expectBodyList(itemClass).returnResult().getResponseBody();
        return result;
    }

    public <T> SendRequestAction<List<T>> expectBodyAsListOf(ParameterizedTypeReference<T> itemType) {
        var result = (SendRequestAction<List<T>>) this;
        result.bodyFormat = spec -> spec.expectBodyList(itemType).returnResult().getResponseBody();
        return result;
    }

    @Override
    protected void howToPerform(WebTestClient value) {
        errors.clear();
        result = null;
        responseSpec = requestSpec.apply(value).exchange();
        result = responseSpec.returnResult(Void.class);

        if (bodyFormat != null) {
            assertions.addLast(checkExpectation(bodyFormat));
        }

        assertions.forEach(a -> {
            try {
                a.get().performAction(responseSpec);
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

    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromArray(
            String description,
            Function<B, S> howToGet) {
        return objectFromIterable(description, new FromBodyGet<>(this, howToGet));
    }
}
