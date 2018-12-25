package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;

import javax.net.ssl.SSLSession;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpHeaders;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;

/**
 * This class is designed to build chains of functions that get needed data from a http response.
 * @param <T> is a type of data to get from a http response
 */
public final class HttpResponseInfoSequentialGetSupplier<T> extends SequentialGetStepSupplier<HttpSteps, T,
        HttpResponse<?>, HttpResponseInfoSequentialGetSupplier<T>> {

    private static final Function<HttpResponse<?>, Integer> STATUS_CODE = HttpResponse::statusCode;
    private static final Function<HttpResponse<?>, HttpRequest> REQUEST = HttpResponse::request;
    private static final Function<HttpResponse<?>, HttpResponse<?>> PREVIOUS_RESPONSE = httpResponse ->
            httpResponse.previousResponse().orElse(null);
    private static final Function<HttpResponse<?>, HttpHeaders> HEADERS = HttpResponse::headers;
    private static final Function<HttpResponse<?>, SSLSession> SSL_SESSION = httpResponse -> httpResponse
            .sslSession()
            .orElse(null);
    private static final Function<HttpResponse<?>, URI> URI = HttpResponse::uri;
    private static final Function<HttpResponse<?>, HttpClient.Version> VERSION = HttpResponse::version;

    private final Function<HttpResponse<?>, T> endFunction;

    private HttpResponseInfoSequentialGetSupplier(Function<HttpResponse<?>, T> endFunction) {
        this.endFunction = endFunction;
    }

    @SuppressWarnings("unchecked")
    private static <T> Function<HttpResponse<?>, T> bodyFunction() {
        return httpResponse -> (T) httpResponse.body();
    }

    /**
     * Builds a function to get the status code for a response.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get a status code
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<Integer> statusCodeOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Status code of the response", STATUS_CODE))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the status code for a response.
     *
     * @param httpResponse response to get a status code
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<Integer> statusCodeOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Status code of the response", STATUS_CODE))
                .from(httpResponse);
    }

    /**
     * Builds a function to get the {@link HttpRequest} corresponding to given response response.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get the request
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpRequest> correspondingRequestOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Corresponding request of the response", REQUEST))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the {@link HttpRequest} corresponding to given response response.
     *
     * @param httpResponse response to get the request
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpRequest> correspondingRequestOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Corresponding request of the response", REQUEST))
                .from(httpResponse);
    }

    /**
     * Builds a function to get the previous intermediate response of the received one. An intermediate
     * response is one that is received as a result of redirection or authentication.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get the previous response from
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpResponse<?>> previousResponseOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Previous response", PREVIOUS_RESPONSE))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the previous intermediate response of the received one. An intermediate
     * response is one that is received as a result of redirection or authentication.
     *
     * @param httpResponse response to get the previous response from
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpResponse<?>> previousResponseOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Previous response", PREVIOUS_RESPONSE))
                .from(httpResponse);
    }

    /**
     * Builds a function that gets headers of a received response.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get headers
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpHeaders> headersOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Response headers of the response", HEADERS))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function that gets headers of a received response.
     *
     * @param httpResponse response to get headers
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpHeaders> headersOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Response headers of the response", HEADERS))
                .from(httpResponse);
    }

    /**
     * Builds a function to get the body of received response. Depending on the type of {@code T}, the returned body
     * may represent the body after it was read (such as {@code byte[]}, or {@code String}, or {@code Path}) or it may
     * represent an object with which the body is read, such as an {@link java.io.InputStream}.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get the body
     * @param <T> type of body to be returned
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static <T> HttpResponseInfoSequentialGetSupplier<T> bodyOf(HttpResponseSequentialGetSupplier<T, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<T>(toGet("Body of the response", bodyFunction()))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the body of received response. Depending on the type of {@code T}, the returned body
     * may represent the body after it was read (such as {@code byte[]}, or {@code String}, or {@code Path}) or it may
     * represent an object with which the body is read, such as an {@link java.io.InputStream}.
     *
     * @param httpResponse response to get the body
     * @param <T> type of body to be returned
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static <T> HttpResponseInfoSequentialGetSupplier<T> bodyOf(HttpResponse<T> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<T>(toGet("Body of the response", bodyFunction()))
                .from(httpResponse);
    }

    /**
     * Builds a function to get the {@link SSLSession} in effect for received response.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get the {@link SSLSession}
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<SSLSession> sslSessionOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Ssl session of the response", SSL_SESSION))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the {@link SSLSession} in effect for received response.
     *
     * @param httpResponse response to get the {@link SSLSession}
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<SSLSession> sslSessionOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Ssl session of the response", SSL_SESSION))
                .from(httpResponse);
    }

    /**
     * Builds a function to get the {@code URI} that the response was received from. This may be different from the
     * request {@code URI} if redirection occurred.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get the {@link URI}
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<URI> uriOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("URI of the response", URI))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the {@code URI} that the response was received from. This may be different from the
     * request {@code URI} if redirection occurred.
     *
     * @param httpResponse response to get the {@link URI}
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<URI> uriOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("URI of the response", URI))
                .from(httpResponse);
    }

    /**
     * Builds a function to get the HTTP protocol version that was used for received response.
     *
     * @param httpResponseSequentialGetSupplier is a description of the response to get the HTTP protocol version
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpClient.Version> httpVersionOf(HttpResponseSequentialGetSupplier<?, ?> httpResponseSequentialGetSupplier) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Http version of the response", VERSION))
                .from(httpResponseSequentialGetSupplier);
    }

    /**
     * Builds a function to get the HTTP protocol version that was used for received response.
     *
     * @param httpResponse response to get the HTTP protocol version
     * @return instance of {@link HttpResponseInfoSequentialGetSupplier}
     */
    public static HttpResponseInfoSequentialGetSupplier<HttpClient.Version> httpVersionOf(HttpResponse<?> httpResponse) {
        return new HttpResponseInfoSequentialGetSupplier<>(toGet("Http version of the response", VERSION))
                .from(httpResponse);
    }

    @Override
    protected Function<HttpResponse<?>, T> getEndFunction() {
        return endFunction;
    }
}
