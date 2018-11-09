package ru.tinkoff.qa.neptune.http.api;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.net.http.HttpRequest.BodyPublishers.noBody;
import static java.net.http.HttpRequest.newBuilder;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

/**
 * It builds a function that prepare a {@link HttpRequest} to get a response further.
 */
public abstract class HttpRequestGetSupplier extends GetStepSupplier<HttpSteps,
        HowToGetResponse,
        HttpRequestGetSupplier> {

    private final HttpRequest.Builder builder;
    final URI uri;
    private final Map<URI, Map<String, List<String>>> cookieToAdd = new HashMap<>();

    private HttpClient.Builder clientToBeUsed;
    private boolean toUseDefaultClient = false;


    private HttpRequestGetSupplier(String uri,
                                   Function<HttpRequest.Builder, HttpRequest.Builder> builderPreparing) {
        checkArgument(!isBlank(uri), "URI parameter should not be a null or empty value");
        try {
            builder = builderPreparing.apply(newBuilder().uri(this.uri = new URI(uri)));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
        set(httpSteps -> {
            if (toUseDefaultClient) {
                httpSteps.resetHttpClient();
            }
            else {
                ofNullable(clientToBeUsed).ifPresent(httpSteps::changeCurrentHttpClientSettings);
            }

            var client = httpSteps.getCurrentClient();
            client.cookieHandler().ifPresent((cookieHandler) -> cookieToAdd.forEach((key, value) -> {
                try {
                    cookieHandler.put(key, value);
                } catch (IOException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }));

            return new HowToGetResponse(client, builder.build());
        });
    }

    /**
     * Makes preparation for the sending GET-request
     *
     * @param uri to send a GET-request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static GetHttpRequestSupplier GET(String uri) {
        return new GetHttpRequestSupplier(uri);
    }

    /**
     * Makes preparation for the sending POST-request
     *
     * @param uri to send a POST-request
     * @param publisher body publisher of the request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static HttpRequestGetSupplier POST(String uri, HttpRequest.BodyPublisher publisher) {
        return new PostHttpRequestSupplier(uri, publisher);
    }

    /**
     * Makes preparation for the sending PUT-request
     *
     * @param uri to send a PUT-request
     * @param publisher body publisher of the request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static HttpRequestGetSupplier PUT(String uri, HttpRequest.BodyPublisher publisher) {
        return new PutHttpRequestSupplier(uri, publisher);
    }

    /**
     * Makes preparation for the sending DELETE-request
     *
     * @param uri to send a DELETE-request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static HttpRequestGetSupplier DELETE(String uri) {
        return new DeleteHttpRequestSupplier(uri);
    }

    /**
     * Makes preparation for the sending a request. It sets the request method and request body to the given values.
     *
     * @param uri to send a request
     * @param method a method to send
     * @param publisher body publisher of the request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static HttpRequestGetSupplier methodRequest(String uri, String method, HttpRequest.BodyPublisher publisher) {
        return new MethodHttpRequestSupplier(uri, method, publisher);
    }

    /**
     * Makes preparation for the sending a request. It sets the request method to the given value.
     *
     * @param uri to send a request
     * @param method a method to send
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static HttpRequestGetSupplier methodRequest(String uri, String method) {
        return methodRequest(uri, method, noBody());
    }

    /**
     * Adds the given name value pair to the set of headers for this request.
     * The given value is added to the list of values for that name.
     *
     * @param name the header name
     * @param value the header value
     * @return self-reference
     */
    public HttpRequestGetSupplier header(String name, String value) {
        builder.header(name, value);
        return this;
    }

    /**
     * Adds the given name value pairs to the set of headers for this
     * request. The supplied {@code String} instances must alternate as
     * header names and header values.
     * To add several values to the same name then the same name must
     * be supplied with each new value.
     *
     * @param headers the list of name value pairs
     * @return self-reference
     */
    public HttpRequestGetSupplier headers(String... headers) {
        builder.headers(headers);
        return this;
    }

    /**
     * Sets the given name value pair to the set of headers for this
     * request. This overwrites any previously set values for name.
     *
     * @param name the header name
     * @param value the header value
     * @return self-reference
     */
    public HttpRequestGetSupplier setHeader(String name, String value) {
        builder.setHeader(name, value);
        return this;
    }

    /**
     * Sets a timeout for this request. If the response is not received
     * within the specified timeout then an {@link HttpTimeoutException} is
     * thrown when request is performed.
     *
     * @param duration the timeout duration
     * @return self-reference
     */
    public HttpRequestGetSupplier timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    /**
     * Sets the preferred {@link HttpClient.Version} for this request.
     *
     * @param version the HTTP protocol version requested
     * @return self-reference
     */
    public HttpRequestGetSupplier version(HttpClient.Version version) {
        builder.version(version);
        return this;
    }

    /**
     * Requests the server to acknowledge the request before sending the
     * body. This is disabled by default. If enabled, the server is
     * requested to send an error response or a {@code 100 Continue}
     * response before the client sends the request body. This means the
     * request publisher for the request will not be invoked until this
     * interim response is received.
     *
     * @param enable {@code true} if Expect continue to be sent
     * @return self-reference
     */
    public HttpRequestGetSupplier expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return this;
    }

    /**
     * This method defines a new client to be used until another request built by {@link HttpRequestGetSupplier}
     * is sent. This request supplier should be configured by invocation of
     * {@link #useHttpClient(HttpClient.Builder)} or
     * {@link #useDefaultHttpClient()}. Also it is possible to invoke
     * {@link HttpSteps#changeCurrentHttpClientSettings(HttpClient.Builder)} or
     * {@link HttpSteps#resetHttpClient()} for same purposes.
     *
     * @param clientToBeUsed is a builder of {@link HttpClient} that is going to be used further.
     * @return self-reference
     */
    public HttpRequestGetSupplier useHttpClient(HttpClient.Builder clientToBeUsed) {
        this.clientToBeUsed = clientToBeUsed;
        toUseDefaultClient = false;
        return this;
    }

    /**
     * This method says that default http client is used further. This client is described by properties.
     * Default http client is used until another request built by {@link HttpRequestGetSupplier}
     * is sent. This request supplier should be configured by invocation of
     * {@link #useHttpClient(HttpClient.Builder)} or
     * {@link #useDefaultHttpClient()}. Also it is possible to invoke
     * {@link HttpSteps#changeCurrentHttpClientSettings(HttpClient.Builder)} or
     * {@link HttpSteps#resetHttpClient()} for same purposes.
     *
     * @return self-reference
     */
    public HttpRequestGetSupplier useDefaultHttpClient() {
        this.clientToBeUsed = null;
        toUseDefaultClient = true;
        return this;
    }

    /**
     * Adds all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param uri a {@code URI} where the cookies come from
     * @param responseHeaders an immutable map from field names to
     *            lists of field values representing the response
     *            header fields returned
     *
     * @return self-reference
     */
    public HttpRequestGetSupplier addCookies(URI uri, Map<String, List<String>> responseHeaders) {
        ofNullable(cookieToAdd.get(uri)).ifPresentOrElse(
                (map) -> responseHeaders.forEach((key, value) ->

                        ofNullable(map.get(key)).ifPresentOrElse((strings) -> strings.addAll(value),
                                () -> map.put(key, value))),
                () -> cookieToAdd.put(uri, responseHeaders));
        return this;
    }

    /**
     * Adds all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is  an immutable map from field names to
     *                  lists of field values representing the response
     *                  header fields returned
     * @return self-reference
     */
    public HttpRequestGetSupplier addCookies(Map<URI, Map<String, List<String>>> cookieMap) {
        cookieMap.forEach(this::addCookies);
        return this;
    }

    /**
     * Sets all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param uri a {@code URI} where the cookies come from
     * @param responseHeaders an immutable map from field names to
     *            lists of field values representing the response
     *            header fields returned
     *
     * @return self-reference
     */
    public HttpRequestGetSupplier setCookies(URI uri, Map<String, List<String>> responseHeaders) {
        cookieToAdd.clear();
        cookieToAdd.put(uri, responseHeaders);
        return this;
    }

    /**
     * Sets all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is  an immutable map from field names to
     *                  lists of field values representing the response
     *                  header fields returned
     * @return self-reference
     */
    public HttpRequestGetSupplier setCookies(Map<URI, Map<String, List<String>>> cookieMap) {
        cookieToAdd.clear();
        cookieMap.putAll(cookieMap);
        return this;
    }

    public static final class GetHttpRequestSupplier extends HttpRequestGetSupplier {
        private GetHttpRequestSupplier(String uri) {
            super(uri, HttpRequest.Builder::GET);
        }

        public String toString() {
            return format("GET request URI:%s", uri);
        }
    }

    public static final class PostHttpRequestSupplier extends HttpRequestGetSupplier {
        private PostHttpRequestSupplier(String uri, HttpRequest.BodyPublisher publisher) {
            super(uri, builder -> {
                checkArgument(publisher != null, "Body publisher parameter should not be a null value");
                return builder.POST(publisher);
            });
        }

        public String toString() {
            return format("POST request URI:%s", uri);
        }
    }

    public static final class PutHttpRequestSupplier extends HttpRequestGetSupplier {
        private PutHttpRequestSupplier(String uri, HttpRequest.BodyPublisher publisher) {
            super(uri, builder -> {
                checkArgument(publisher != null, "Body publisher parameter should not be a null value");
                return builder.PUT(publisher);
            });
        }

        public String toString() {
            return format("PUT request URI:%s", uri);
        }
    }

    public static final class DeleteHttpRequestSupplier extends HttpRequestGetSupplier {
        private DeleteHttpRequestSupplier(String uri) {
            super(uri, HttpRequest.Builder::DELETE);
        }

        public String toString() {
            return format("DELETE request URI:%s", uri);
        }
    }

    public static final class MethodHttpRequestSupplier extends HttpRequestGetSupplier {

        private final String method;

        private MethodHttpRequestSupplier(String uri, String method, HttpRequest.BodyPublisher publisher) {
            super(uri, builder -> {
                checkArgument(!isBlank(method), "Method name should not be a null or empty value");
                checkArgument(publisher != null, "Body publisher parameter should not be a null value");
                return builder.method(method, publisher);
            });
            this.method = method;
        }

        public String toString() {
            return format("Request method:%s URI:%s", method, uri);
        }
    }
}
