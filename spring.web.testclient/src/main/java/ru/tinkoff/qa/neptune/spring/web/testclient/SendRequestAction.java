package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.test.web.reactive.server.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.spring.web.testclient.captors.RequestExchangeResultCaptor;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectResponseCookies;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectResponseHeaders;
import ru.tinkoff.qa.neptune.spring.web.testclient.expectation.descriptions.ExpectResponseStatus;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.spring.web.testclient.BodySpecFunction.bodyMappedAs;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetArrayFromResponse.array;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetListFromResponse.list;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBody.objectFromBody;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBody.responseBody;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBodyArray.objectFromArray;
import static ru.tinkoff.qa.neptune.spring.web.testclient.GetObjectFromResponseBodyIterable.objectFromIterable;
import static ru.tinkoff.qa.neptune.spring.web.testclient.LogWebTestClientExpectation.logExpectation;

/**
 * Performs the sending of a request.
 *
 * @param <B>    is expected type of body of a response
 * @param <F>    is a type of function that calculates value of response body on the applying
 * @param <SELF> is a type of subclass of {@link SendRequestAction}
 */
@SuppressWarnings("unchecked")
public abstract class SendRequestAction<B, F extends BodySpecFunction<B, ?, ?>, SELF extends SendRequestAction<B, F, SELF>> extends SequentialActionSupplier<WebTestClientContext, WebTestClient, SELF> {

    private final Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec;
    final LinkedList<Expectation<?>> assertions = new LinkedList<>();
    final List<AssertionError> errors = new LinkedList<>();
    final F bodyFormat;
    WebTestClient.ResponseSpec responseSpec;

    @CaptureOnSuccess(by = RequestExchangeResultCaptor.class)
    @CaptureOnFailure(by = RequestExchangeResultCaptor.class)
    private ExchangeResult result;

    SendRequestAction(Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec, F bodyFormat) {
        checkNotNull(requestSpec);
        checkNotNull(bodyFormat);
        this.bodyFormat = bodyFormat;
        this.requestSpec = requestSpec;
    }

    /**
     * Creates a step that sends specified request and receives a response.
     *
     * @param requestSpec is a request specification
     * @return an instance of {@link SendRequestActionRaw}
     */
    public static SendRequestActionRaw send(Function<WebTestClient,
            WebTestClient.RequestHeadersSpec<?>> requestSpec) {
        return new SendRequestActionRaw(requestSpec)
                .performOn(WebTestClientContext::getDefaultWebTestClient);
    }

    /**
     * Creates a step that sends specified request and receives a response.
     *
     * @param client      explicitly defined instance of {@link WebTestClient}
     * @param requestSpec is a request specification
     * @return an instance of {@link SendRequestActionRaw}
     */
    public static SendRequestActionRaw send(WebTestClient client,
                                            Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec) {
        checkNotNull(client);
        return new SendRequestActionRaw(requestSpec).performOn(webTestClientContext -> client);
    }

    /**
     * Creates a step that sends specified request and receives a response.
     *
     * @param requestSpec is a request specification
     * @param tClass      is a class of deserialized body of response
     * @param <T>         is type of deserialized body of response
     * @return an instance of {@link SendRequestActionMapped}
     */
    @Description("Send request and then get response. Deserialize response body to instance of '{class}'")
    public static <T> SendRequestActionMapped<T> send(Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec,
                                                      @DescriptionFragment("class") Class<T> tClass) {
        return new SendRequestActionMapped<>(requestSpec, bodyMappedAs(tClass))
                .performOn(WebTestClientContext::getDefaultWebTestClient);
    }

    /**
     * Creates a step that sends specified request and receives a response.
     *
     * @param client      explicitly defined instance of {@link WebTestClient}
     * @param requestSpec is a request specification
     * @param tClass      is a class of deserialized body of response
     * @param <T>         is type of deserialized body of response
     * @return an instance of {@link SendRequestActionMapped}
     */
    @Description("Send request and then get response. Deserialize response body to instance of '{class}'")
    public static <T> SendRequestActionMapped<T> send(WebTestClient client,
                                                      Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec,
                                                      @DescriptionFragment("class") Class<T> tClass) {
        return new SendRequestActionMapped<>(requestSpec, bodyMappedAs(tClass))
                .performOn(webTestClientContext -> client);
    }

    /**
     * Creates a step that sends specified request and receives a response.
     *
     * @param requestSpec is a request specification
     * @param type        is a reference to type of deserialized body of response
     * @param <T>         is type of deserialized body of response
     * @return an instance of {@link SendRequestActionMapped}
     */
    @Description("Send request and then get response. Deserialize response body to instance of '{type}'")
    public static <T> SendRequestActionMapped<T> send(Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec,
                                                      @DescriptionFragment("type") ParameterizedTypeReference<T> type) {
        return new SendRequestActionMapped<>(requestSpec, bodyMappedAs(type))
                .performOn(WebTestClientContext::getDefaultWebTestClient);
    }

    /**
     * Creates a step that sends specified request and receives a response.
     *
     * @param client      explicitly defined instance of {@link WebTestClient}
     * @param requestSpec is a request specification
     * @param type        is a reference to type of deserialized body of response
     * @param <T>         is type of deserialized body of response
     * @return an instance of {@link SendRequestActionMapped}
     */
    @Description("Send request and then get response. Deserialize response body to instance of '{type}'")
    public static <T> SendRequestActionMapped<T> send(WebTestClient client,
                                                      Function<WebTestClient, WebTestClient.RequestHeadersSpec<?>> requestSpec,
                                                      @DescriptionFragment("type") ParameterizedTypeReference<T> type) {
        return new SendRequestActionMapped<>(requestSpec, bodyMappedAs(type))
                .performOn(webTestClientContext -> client);
    }

    <T> SELF addExpectation(Function<WebTestClient.ResponseSpec, T> f, String description) {
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
        return (SELF) this;
    }

    /**
     * Defines specification how to check status of a response
     *
     * @param statusCheck specification how to check status of a response
     * @return self-reference
     */
    public SELF expectStatus(Function<StatusAssertions, WebTestClient.ResponseSpec> statusCheck) {
        return addExpectation(spec -> statusCheck.apply(spec.expectStatus()), new ExpectResponseStatus().toString());
    }

    /**
     * Defines specification how to check headers of a response
     *
     * @param headerCheck specification how to check headers of a response
     * @return self-reference
     */
    public SELF expectHeader(Function<HeaderAssertions, WebTestClient.ResponseSpec> headerCheck) {
        return addExpectation(spec -> headerCheck.apply(spec.expectHeader()), new ExpectResponseHeaders().toString());
    }

    /**
     * Defines specification how to check cookie of a response
     *
     * @param cookieCheck specification how to check cookie of a response
     * @return self-reference
     */
    public SELF expectCookie(Function<CookieAssertions, WebTestClient.ResponseSpec> cookieCheck) {
        return addExpectation(spec -> cookieCheck.apply(spec.expectCookie()), new ExpectResponseCookies().toString());
    }

    abstract void readBody();

    @Override
    protected void howToPerform(WebTestClient value) {
        errors.clear();
        result = null;
        responseSpec = requestSpec.apply(value).exchange();

        readBody();

        assertions.forEach(ex -> {
            try {
                ex.verify(responseSpec);
                logExpectation(ex).get().performAction(ex);
            } catch (AssertionError e) {
                errors.add(e);
            }
        });

        result = bodyFormat.exchangeResult();
        if (errors.isEmpty()) {
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
        return bodyFormat.getBody();
    }

    /**
     * Creates a step that returns deserialized content of body of the received response.
     *
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public GetObjectFromResponseBody<B> thenGetBody() {
        return responseBody(new FromBodyGet<>(this, b -> b));
    }

    /**
     * Creates a step that returns value taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param howToGet    describes how to get desired value
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBody}
     */
    public <T> GetObjectFromResponseBody<T> thenGetValue(String description, Function<B, T> howToGet) {
        return objectFromBody(description, new FromBodyGet<>(this, howToGet));
    }

    /**
     * Creates a step that returns {@link Iterable} value taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param howToGet    describes how to get desired value
     * @param <T>         is a type of item from resulted iterable
     * @param <S>         is a type of resulted iterable
     * @return an instance of {@link GetListFromResponse}
     */
    public <T, S extends Iterable<T>> GetListFromResponse<T, S> thenGetList(String description,
                                                                            Function<B, S> howToGet) {
        return list(description, new FromBodyGet<>(this, howToGet));
    }

    /**
     * Creates a step that returns array value taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param howToGet    describes how to get desired value
     * @param <T>         is a type of item from resulted array
     * @return an instance of {@link GetArrayFromResponse}
     */
    public <T> GetArrayFromResponse<T> thenGetArray(String description, Function<B, T[]> howToGet) {
        return array(description, new FromBodyGet<>(this, howToGet));
    }

    /**
     * Creates a step that returns an item from {@link Iterable} taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param howToGet    describes how to get {@link Iterable}
     * @param <T>         is a type of resulted value
     * @param <S>         is a type of iterable
     * @return an instance of {@link GetObjectFromResponseBodyIterable}
     */
    public <T, S extends Iterable<T>> GetObjectFromResponseBodyIterable<T> thenGetValueFromIterable(
            String description,
            Function<B, S> howToGet) {
        return objectFromIterable(description, new FromBodyGet<>(this, howToGet));
    }

    /**
     * Creates a step that returns an item from array taken from / calculated by deserialized content.
     *
     * @param description is description of value to get
     * @param howToGet    describes how to get an array
     * @param <T>         is a type of resulted value
     * @return an instance of {@link GetObjectFromResponseBodyArray}
     */
    public <T> GetObjectFromResponseBodyArray<T> thenGetValueFromArray(
            String description,
            Function<B, T[]> howToGet) {
        return objectFromArray(description, new FromBodyGet<>(this, howToGet));
    }
}
