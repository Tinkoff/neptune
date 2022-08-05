package ru.tinkoff.qa.neptune.spring.web.testclient;

import org.hamcrest.Matcher;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.CacheControl;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;
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

    <T> SELF addExpectation(String description, Function<WebTestClient.ResponseSpec, T> assertion) {
        assertions.add(new Expectation.SimpleExpectation<>(description, assertion));
        return (SELF) this;
    }

    <R, T> SELF addExpectation(String description,
                               Function<WebTestClient.ResponseSpec, R> getObj,
                               Function<R, T> assertion) {
        assertions.add(new Expectation.ExpectationWithSpringAssertion<>(description, getObj, assertion));
        return (SELF) this;
    }

    /**
     * Defines specification how to check status of a response
     *
     * @param statusCheck specification how to check status of a response
     * @return self-reference
     */
    public SELF expectStatus(Function<StatusAssertions, WebTestClient.ResponseSpec> statusCheck) {
        return addExpectation(new ExpectResponseStatus().toString(),
                WebTestClient.ResponseSpec::expectStatus,
                statusCheck);
    }

    /**
     * Assert the response status as an integer.
     */
    public SELF expectStatus(int code) {
        return expectStatus(statusAssertions -> statusAssertions.isEqualTo(code));
    }

    /**
     * Assert the response status as an {@link HttpStatus}.
     */
    public SELF expectStatus(HttpStatus status) {
        return expectStatus(statusAssertions -> statusAssertions.isEqualTo(status));
    }

    /**
     * Assert the response status code is in the 1xx range.
     */
    public SELF expectStatusIs1xxInformational() {
        return expectStatus(StatusAssertions::is1xxInformational);
    }

    /**
     * Assert the response status code is in the 2xx range.
     */
    public SELF expectStatusIs2xxSuccessful() {
        return expectStatus(StatusAssertions::is2xxSuccessful);
    }

    /**
     * Assert the response status code is in the 3xx range.
     */
    public SELF expectStatusIs3xxRedirection() {
        return expectStatus(StatusAssertions::is3xxRedirection);
    }

    /**
     * Assert the response status code is in the 4xx range.
     */
    public SELF expectStatusIs4xxClientError() {
        return expectStatus(StatusAssertions::is4xxClientError);
    }

    /**
     * Assert the response status code is in the 5xx range.
     */
    public SELF expectStatusIsis5xxServerError() {
        return expectStatus(StatusAssertions::is5xxServerError);
    }

    /**
     * Defines specification how to check headers of a response
     *
     * @param headerCheck specification how to check headers of a response
     * @return self-reference
     */
    public SELF expectHeader(Function<HeaderAssertions, WebTestClient.ResponseSpec> headerCheck) {
        return addExpectation(new ExpectResponseHeaders().toString(),
                WebTestClient.ResponseSpec::expectHeader,
                headerCheck);
    }

    /**
     * Expect that the header with the given name is present.
     */
    public SELF expectHeader(String header) {
        return expectHeader(headerAssertions -> headerAssertions.exists(header));
    }

    /**
     * Assert the first value of the response header is the same as defined.
     */
    public SELF expectHeaderValue(String header, String value) {
        return expectHeaderValue(header, equalTo(value));
    }

    /**
     * Assert the first value of the response header with a Hamcrest {@link Matcher}.
     */
    public SELF expectHeaderValue(String header, Matcher<? super String> matcher) {
        return expectHeader(headerAssertions -> headerAssertions.value(header, matcher));
    }

    /**
     * Assert all values of the response header with a Hamcrest {@link Matcher}.
     */
    public SELF expectHeaderValues(String header, Matcher<? super Iterable<String>> matcher) {
        return expectHeader(headerAssertions -> headerAssertions.values(header, matcher));
    }

    /**
     * Assert all values of the response header are same as defined in the same order.
     */
    public SELF expectHeaderValues(String header, String... values) {
        return expectHeaderValues(header, iterableInOrder(values));
    }

    /**
     * Match the first value of the response header with a regex.
     */
    public SELF expectHeaderValueMatches(String header, String pattern) {
        return expectHeader(headerAssertions -> headerAssertions.valueMatches(header, pattern));
    }

    /**
     * Match all values of the response header with the given regex
     * patterns which are applied to the values of the header in the
     * same order. Note that the number of patterns must match the
     * number of actual values.
     */
    public SELF expectHeaderValuesMatch(String header, String... patterns) {
        return expectHeader(headerAssertions -> headerAssertions.valuesMatch(header, patterns));
    }

    /**
     * Expect a "Cache-Control" header with the given value.
     */
    public SELF expectCacheControl(CacheControl cacheControl) {
        return expectHeader(headerAssertions -> headerAssertions.cacheControl(cacheControl));
    }

    /**
     * Expect a "Content-Disposition" header with the given value.
     */
    public SELF expectContentDisposition(ContentDisposition contentDisposition) {
        return expectHeader(headerAssertions -> headerAssertions.contentDisposition(contentDisposition));
    }

    /**
     * Expect a "Content-Length" header with the given value.
     */
    public SELF expectContentLength(long contentLength) {
        return expectHeader(headerAssertions -> headerAssertions.contentLength(contentLength));
    }

    /**
     * Expect a "Content-Type" header with the given value.
     */
    public SELF expectContentType(MediaType mediaType) {
        return expectHeader(headerAssertions -> headerAssertions.contentType(mediaType));
    }

    /**
     * Expect a "Content-Type" header with the given value.
     */
    public SELF expectContentType(String mediaType) {
        return expectHeader(headerAssertions -> headerAssertions.contentType(mediaType));
    }

    /**
     * Expect a "Content-Type" header compatible with the given value.
     */
    public SELF expectContentTypeCompatibleWith(MediaType mediaType) {
        return expectHeader(headerAssertions -> headerAssertions.contentTypeCompatibleWith(mediaType));
    }

    /**
     * Expect a "Content-Type" header compatible with the given value.
     */
    public SELF expectContentTypeCompatibleWith(String mediaType) {
        return expectHeader(headerAssertions -> headerAssertions.contentTypeCompatibleWith(mediaType));
    }

    /**
     * Expect an "Expires" header with the given value.
     */
    public SELF expectExpires(long expires) {
        return expectHeader(headerAssertions -> headerAssertions.expires(expires));
    }

    /**
     * Expect a "Last-Modified" header with the given value.
     */
    public SELF expectLastModified(long lastModified) {
        return expectHeader(headerAssertions -> headerAssertions.lastModified(lastModified));
    }

    /**
     * Expect a "Location" header with the given value.
     */
    public SELF expectLocation(String location) {
        return expectHeader(headerAssertions -> headerAssertions.location(location));
    }

    /**
     * Defines specification how to check cookie of a response
     *
     * @param cookieCheck specification how to check cookie of a response
     * @return self-reference
     */
    public SELF expectCookie(Function<CookieAssertions, WebTestClient.ResponseSpec> cookieCheck) {
        return addExpectation(new ExpectResponseCookies().toString(),
                WebTestClient.ResponseSpec::expectCookie,
                cookieCheck);
    }

    /**
     * Expect that the cookie with the given name is present.
     */
    public SELF expectCookie(String name) {
        return expectCookie(cookieAssertions -> cookieAssertions.exists(name));
    }

    /**
     * Expect a header with the given name to match the specified values.
     */
    public SELF expectCookieValue(String name, String value) {
        return expectCookie(cookieAssertions -> cookieAssertions.valueEquals(name, value));
    }

    /**
     * Assert the first value of the response cookie with a Hamcrest {@link Matcher}.
     */
    public SELF expectCookieValue(String name, Matcher<? super String> matcher) {
        return expectCookie(cookieAssertions -> cookieAssertions.value(name, matcher));
    }

    /**
     * Assert a cookie's maxAge attribute.
     */
    public SELF expectCookieMaxAge(String name, Duration expected) {
        return expectCookie(cookieAssertions -> cookieAssertions.maxAge(name, expected));
    }

    /**
     * Assert a cookie's maxAge attribute with a Hamcrest {@link Matcher}.
     */
    public SELF expectCookieMaxAge(String name, Matcher<? super Long> matcher) {
        return expectCookie(cookieAssertions -> cookieAssertions.maxAge(name, matcher));
    }

    /**
     * Assert a cookie's path attribute.
     */
    public SELF expectCookiePath(String name, String expected) {
        return expectCookie(cookieAssertions -> cookieAssertions.path(name, expected));
    }

    /**
     * Assert a cookie's path attribute with a Hamcrest {@link Matcher}.
     */
    public SELF expectCookiePath(String name, Matcher<? super String> matcher) {
        return expectCookie(cookieAssertions -> cookieAssertions.path(name, matcher));
    }

    /**
     * Assert a cookie's domain attribute.
     */
    public SELF expectCookieDomain(String name, String expected) {
        return expectCookie(cookieAssertions -> cookieAssertions.domain(name, expected));
    }

    /**
     * Assert a cookie's domain attribute with a Hamcrest {@link Matcher}.
     */
    public SELF expectCookieDomain(String name, Matcher<? super String> matcher) {
        return expectCookie(cookieAssertions -> cookieAssertions.domain(name, matcher));
    }

    /**
     * Assert a cookie's secure attribute.
     */
    public SELF expectCookieSecure(String name, boolean expected) {
        return expectCookie(cookieAssertions -> cookieAssertions.secure(name, expected));
    }

    /**
     * Assert a cookie's httpOnly attribute.
     */
    public SELF expectCookieHttpOnly(String name, boolean expected) {
        return expectCookie(cookieAssertions -> cookieAssertions.httpOnly(name, expected));
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
