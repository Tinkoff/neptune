package ru.tinkoff.qa.neptune.http.api;

import org.apache.commons.validator.routines.UrlValidator;
import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpDomainToRespondProperty;

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
import static java.net.http.HttpRequest.newBuilder;
import static java.util.Map.entry;
import static java.util.Map.ofEntries;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.empty;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpDomainToRespondProperty.DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY;

/**
 * It builds a function that prepare a {@link HttpRequest} to get a response further.
 */
@SuppressWarnings("unchecked")
public abstract class HttpRequestGetSupplier<T extends HttpRequestGetSupplier<T>> extends GetStepSupplier<HttpSteps,
        HowToGetResponse,
        HttpRequestGetSupplier<T>> {

    /**
     * According to RFC 2965 3.2.2, request header field name must be 'Set-Cookie2'
     * for the cookie setting.
     */
    private static final String SET_COOKIE = "Set-Cookie2";
    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    private final HttpRequest.Builder builder;
    final URI uri;
    private final Map<URI, Map<String, List<String>>> cookieToAdd = new HashMap<>();

    private HttpClient.Builder clientToBeUsed;
    private boolean toUseDefaultClient = false;


    private HttpRequestGetSupplier(String uri,
                                   Function<HttpRequest.Builder, HttpRequest.Builder> builderPreparing) {
        checkArgument(!isBlank(uri), "URI parameter should not be a null or empty value");
        try {
            if (URL_VALIDATOR.isValid(uri)) {
                this.uri = new URI(uri);
            }
            else {
                this.uri = ofNullable(DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY.get())
                        .map(url -> {
                            try {
                                return new URI(url.toString() + uri);
                            } catch (URISyntaxException e) {
                                throw new IllegalArgumentException(e.getMessage(), e);
                            }
                        })
                        .orElseThrow(() -> new IllegalArgumentException(format("It is impossible to make a request by URI %s. " +
                                        "This value is not a valid URI and the property %s is not defined", uri,
                                DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY.getPropertyName())));
            }

            builder = builderPreparing.apply(newBuilder().uri(this.uri));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        set(toGet("Prepared request", httpSteps -> {
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
        }));
    }

    /**
     * Makes preparation for the sending GET-request
     *
     * @param uri to send a GET-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static GetHttpRequestSupplier GET(String uri) {
        return new GetHttpRequestSupplier(uri);
    }

    /**
     * Makes preparation for the sending POST-request
     *
     * @param uri to send a POST-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param publisher body publisher of the request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static PostHttpRequestSupplier POST(String uri, HttpRequest.BodyPublisher publisher) {
        return new PostHttpRequestSupplier(uri, publisher);
    }

    /**
     * Makes preparation for the sending PUT-request
     *
     * @param uri to send a PUT-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param publisher body publisher of the request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static PutHttpRequestSupplier PUT(String uri, HttpRequest.BodyPublisher publisher) {
        return new PutHttpRequestSupplier(uri, publisher);
    }

    /**
     * Makes preparation for the sending DELETE-request
     *
     * @param uri to send a DELETE-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static DeleteHttpRequestSupplier DELETE(String uri) {
        return new DeleteHttpRequestSupplier(uri);
    }

    /**
     * Makes preparation for the sending a request. It sets the request method and request body to the given values.
     *
     * @param uri to send a request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param method a method to send
     * @param publisher body publisher of the request
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static MethodHttpRequestSupplier methodRequest(String uri, String method, HttpRequest.BodyPublisher publisher) {
        return new MethodHttpRequestSupplier(uri, method, publisher);
    }

    /**
     * Makes preparation for the sending a request. It sets the request method to the given value.
     *
     * @param uri to send a request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param method a method to send
     * @return a new instance of {@link HttpRequestGetSupplier}
     */
    public static MethodHttpRequestSupplier methodRequest(String uri, String method) {
        return methodRequest(uri, method, empty());
    }

    /**
     * Adds the given name value pair to the set of headers for this request.
     * The given value is added to the list of values for that name.
     *
     * @param name the header name
     * @param value the header value
     * @return self-reference
     */
    public T header(String name, String value) {
        builder.header(name, value);
        return (T) this;
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
    public T headers(String... headers) {
        builder.headers(headers);
        return (T) this;
    }

    /**
     * Sets the given name value pair to the set of headers for this
     * request. This overwrites any previously set values for name.
     *
     * @param name the header name
     * @param value the header value
     * @return self-reference
     */
    public T setHeader(String name, String value) {
        builder.setHeader(name, value);
        return (T) this;
    }

    /**
     * Sets a timeout for this request. If the response is not received
     * within the specified timeout then an {@link HttpTimeoutException} is
     * thrown when request is performed.
     *
     * @param duration the timeout duration
     * @return self-reference
     */
    public T timeout(Duration duration) {
        builder.timeout(duration);
        return (T) this;
    }

    /**
     * Sets the preferred {@link HttpClient.Version} for this request.
     *
     * @param version the HTTP protocol version requested
     * @return self-reference
     */
    public T version(HttpClient.Version version) {
        builder.version(version);
        return (T) this;
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
    public T expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return (T) this;
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
    public T useHttpClient(HttpClient.Builder clientToBeUsed) {
        this.clientToBeUsed = clientToBeUsed;
        toUseDefaultClient = false;
        return (T) this;
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
    public T useDefaultHttpClient() {
        this.clientToBeUsed = null;
        toUseDefaultClient = true;
        return (T) this;
    }

    /**
     * Adds all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param uri a {@code URI} where the cookies come from
     * @param responseHeaders a list of field values representing response header fields returned
     *
     * @return self-reference
     */
    public T addCookies(String uri, List<String> responseHeaders) {
        URI uriInstance;
        try {
            uriInstance = new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        ofNullable(cookieToAdd.get(uriInstance)).ifPresentOrElse(
                (map) -> map.get(SET_COOKIE).addAll(responseHeaders),
                () -> cookieToAdd.put(uriInstance, ofEntries(entry(SET_COOKIE, responseHeaders))));
        return (T) this;
    }

    /**
     * Adds all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is a list of field values representing response header fields returned
     * @return self-reference
     */
    public T addCookies(Map<String, List<String>> cookieMap) {
        cookieMap.forEach(this::addCookies);
        return (T) this;
    }

    /**
     * Sets all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param uri a {@code URI} where the cookies come from
     * @param responseHeaders a list of field values representing response header fields returned
     *
     * @return self-reference
     */
    public T setCookies(String uri, List<String> responseHeaders) {
        URI uriInstance;
        try {
            uriInstance = new URI(uri);
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
        cookieToAdd.clear();
        cookieToAdd.put(uriInstance, ofEntries(entry(SET_COOKIE, responseHeaders)));
        return (T) this;
    }

    /**
     * Sets all the applicable cookies, examples are response header
     * fields that are named Set-Cookie2, present in the response
     * headers into a cookie cache.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is a list of field values representing response header fields returned
     * @return self-reference
     */
    public T setCookies(Map<String, List<String>> cookieMap) {
        cookieToAdd.clear();
        cookieMap.forEach((key, value) -> {
            URI uriInstance;
            try {
                uriInstance = new URI(key);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
            cookieToAdd.put(uriInstance, ofEntries(entry(SET_COOKIE, value)));
        });
        return (T) this;
    }

    public static final class GetHttpRequestSupplier extends HttpRequestGetSupplier<GetHttpRequestSupplier> {
        private GetHttpRequestSupplier(String uri) {
            super(uri, HttpRequest.Builder::GET);
        }

        public String toString() {
            return format("GET request URI:%s", uri);
        }
    }

    public static final class PostHttpRequestSupplier extends HttpRequestGetSupplier<PostHttpRequestSupplier> {
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

    public static final class PutHttpRequestSupplier extends HttpRequestGetSupplier<PutHttpRequestSupplier> {
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

    public static final class DeleteHttpRequestSupplier extends HttpRequestGetSupplier<DeleteHttpRequestSupplier> {
        private DeleteHttpRequestSupplier(String uri) {
            super(uri, HttpRequest.Builder::DELETE);
        }

        public String toString() {
            return format("DELETE request URI:%s", uri);
        }
    }

    public static final class MethodHttpRequestSupplier extends HttpRequestGetSupplier<MethodHttpRequestSupplier> {

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
