package ru.tinkoff.qa.neptune.http.api;

import org.apache.commons.validator.routines.UrlValidator;
import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpDomainToRespondProperty;

import javax.ws.rs.core.UriBuilder;
import java.net.CookieManager;
import java.net.HttpCookie;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.*;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.net.http.HttpRequest.newBuilder;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.http.api.CommonBodyPublishers.empty;
import static ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpDomainToRespondProperty.DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY;

/**
 * It builds a function that prepare a {@link HttpRequest} to get a response further.
 */
@SuppressWarnings("unchecked")
public class PreparedHttpRequest<T extends PreparedHttpRequest<T>> implements Function<HttpStepPerformer,
        HowToGetResponse> {

    private static final UrlValidator URL_VALIDATOR = new UrlValidator();

    private final HttpRequest.Builder builder;
    private final Map<URI, List<HttpCookie>> cookieToAdd = new HashMap<>();
    private final UriBuilder uriBuilder = new JerseyUriBuilder();
    private final Function<HttpRequest.Builder, HttpRequest.Builder> builderPreparing;

    private HttpClient.Builder clientToBeUsed;
    private boolean toUseDefaultClient = false;


    private PreparedHttpRequest(String uri, Function<HttpRequest.Builder, HttpRequest.Builder> builderPreparing) {
        checkArgument(!isBlank(uri), "URI parameter should not be a null or empty value");
        try {
            if (URL_VALIDATOR.isValid(uri)) {
                uriBuilder.uri(new URI(uri));
            } else {
                uriBuilder.uri(ofNullable(DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY.get())
                        .map(url -> {
                            try {
                                return new URI(url.toString() + uri);
                            } catch (URISyntaxException e) {
                                throw new IllegalArgumentException(e.getMessage(), e);
                            }
                        })
                        .orElseThrow(() -> new IllegalArgumentException(format("It is impossible to make a request by URI %s. " +
                                        "This value is not a valid URI and the property %s is not defined", uri,
                                DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY.getPropertyName()))));
            }

            builder = newBuilder();
            uriBuilder.uri(uri);
            this.builderPreparing = builderPreparing;
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }

    /**
     * Makes preparation for the sending GET-request
     *
     * @param uri to send a GET-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedGetHttpRequest GET(String uri) {
        return new PreparedGetHttpRequest(uri);
    }

    /**
     * Makes preparation for the sending POST-request
     *
     * @param uri       to send a POST-request. It is possible to define the fully qualified URI as well as a relative path
     *                  when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param publisher body publisher of the request
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedPostHttpRequest POST(String uri, HttpRequest.BodyPublisher publisher) {
        return new PreparedPostHttpRequest(uri, publisher);
    }

    /**
     * Makes preparation for the sending PUT-request
     *
     * @param uri       to send a PUT-request. It is possible to define the fully qualified URI as well as a relative path
     *                  when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param publisher body publisher of the request
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedPutHttpRequest PUT(String uri, HttpRequest.BodyPublisher publisher) {
        return new PreparedPutHttpRequest(uri, publisher);
    }

    /**
     * Makes preparation for the sending DELETE-request
     *
     * @param uri to send a DELETE-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedDeleteHttpRequest DELETE(String uri) {
        return new PreparedDeleteHttpRequest(uri);
    }

    /**
     * Makes preparation for the sending a request. It sets the request method and request body to the given values.
     *
     * @param uri       to send a request. It is possible to define the fully qualified URI as well as a relative path
     *                  when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param method    a method to send
     * @param publisher body publisher of the request
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedMethodHttpRequest methodRequest(String uri, String method, HttpRequest.BodyPublisher publisher) {
        return new PreparedMethodHttpRequest(uri, method, publisher);
    }

    /**
     * Makes preparation for the sending a request. It sets the request method to the given value.
     *
     * @param uri    to send a request. It is possible to define the fully qualified URI as well as a relative path
     *               when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param method a method to send
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedMethodHttpRequest methodRequest(String uri, String method) {
        return methodRequest(uri, method, empty());
    }

    /**
     * Adds the given name value pair to the set of headers for this request.
     * The given value is added to the list of values for that name.
     *
     * @param name  the header name
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
     * @param name  the header name
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
     * This method defines a new client to be used until another request built by {@link PreparedHttpRequest}
     * is sent. This request supplier should be configured by invocation of
     * {@link #useHttpClient(HttpClient.Builder)} or
     * {@link #useDefaultHttpClient()}. Also it is possible to invoke
     * {@link HttpStepPerformer#changeCurrentHttpClientSettings(HttpClient.Builder)} or
     * {@link HttpStepPerformer#resetHttpClient()} for same purposes.
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
     * Default http client is used until another request built by {@link PreparedHttpRequest}
     * is sent. This request supplier should be configured by invocation of
     * {@link #useHttpClient(HttpClient.Builder)} or
     * {@link #useDefaultHttpClient()}. Also it is possible to invoke
     * {@link HttpStepPerformer#changeCurrentHttpClientSettings(HttpClient.Builder)} or
     * {@link HttpStepPerformer#resetHttpClient()} for same purposes.
     *
     * @return self-reference
     */
    public T useDefaultHttpClient() {
        this.clientToBeUsed = null;
        toUseDefaultClient = true;
        return (T) this;
    }

    /**
     * Adds all the cookies into cookie cache of the current http client.
     *
     * @param uri              a {@code URI} where the cookies come from
     * @param cookiesToBeAdded cookies to be added
     * @return self-reference
     */
    public T addCookies(String uri, List<HttpCookie> cookiesToBeAdded) {
        checkArgument(nonNull(cookiesToBeAdded), "List of cookies to be added should not be a null value");
        checkArgument(cookiesToBeAdded.size() > 0, "At least one cookie should be defined for the adding");
        URI uriInstance = ofNullable(uri).map(s -> {
            try {
                return new URI(s);
            } catch (URISyntaxException e) {
                throw new IllegalArgumentException(e.getMessage(), e);
            }
        }).orElse(null);

        ofNullable(cookieToAdd.get(uriInstance)).ifPresentOrElse(
                httpCookies -> {
                    List<HttpCookie> toAdd = new ArrayList<>();
                    cookiesToBeAdded.forEach(cookie -> {
                        if (!httpCookies.contains(cookie)) {
                            toAdd.add(cookie);
                        }
                    });
                    httpCookies.addAll(toAdd);
                },
                () -> cookieToAdd.put(uriInstance, cookiesToBeAdded));
        return (T) this;
    }

    /**
     * Adds all the cookies into cookie cache of the current http client.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is a list of cookies to be added.
     * @return self-reference
     */
    public T addCookies(Map<String, List<HttpCookie>> cookieMap) {
        cookieMap.forEach(this::addCookies);
        return (T) this;
    }

    /**
     * Sets all the cookies into cookie cache of the current http client.
     *
     * @param uri            a {@code URI} where the cookies come from
     * @param cookiesToBeSet cookies to be set
     * @return self-reference
     */
    public T setCookies(String uri, List<HttpCookie> cookiesToBeSet) {
        cookieToAdd.clear();
        return addCookies(uri, cookiesToBeSet);
    }

    /**
     * Sets all the cookies into cookie cache of the current http client.
     *
     * @param cookieMap is a map of cached cookies. Key is a {@code URI} where the cookies come from.
     *                  Value is a list of cookies to be set.
     * @return self-reference
     */
    public T setCookies(Map<String, List<HttpCookie>> cookieMap) {
        cookieToAdd.clear();
        return addCookies(cookieMap);
    }

    /**
     * Adds query parameter to the given URI
     *
     * @param name   parameter name
     * @param values values of the parameter
     * @return self-reference
     */
    public T queryParam(String name, final Object... values) {
        uriBuilder.queryParam(name, values);
        return (T) this;
    }

    @Override
    public HowToGetResponse apply(HttpStepPerformer httpSteps) {
        builder.uri(uriBuilder.build());
        if (toUseDefaultClient) {
            httpSteps.resetHttpClient();
        } else {
            ofNullable(clientToBeUsed).ifPresent(httpSteps::changeCurrentHttpClientSettings);
        }

        var client = httpSteps.getCurrentClient();

        if (cookieToAdd.size() > 0) {
            client.cookieHandler()
                    .ifPresentOrElse(cookieHandler ->
                            {
                                if (!CookieManager.class.isAssignableFrom(cookieHandler.getClass())) {
                                    throw new IllegalStateException(format("It is unknown how to add cookies. We support only %s " +
                                            "as cookie handler for a while", CookieManager.class.getName()));
                                }
                                var cookieStore = ((CookieManager) cookieHandler).getCookieStore();
                                cookieToAdd.forEach((key, value) ->
                                        value.forEach(cookie -> cookieStore.add(key, cookie)));
                            },
                            () -> {
                                throw new IllegalStateException("Can't get access to a cookie store of the current http client");
                            });
        }

        return new HowToGetResponse(client, this.builderPreparing
                .apply(builder).build());
    }

    public static final class PreparedGetHttpRequest extends PreparedHttpRequest<PreparedGetHttpRequest> {
        private PreparedGetHttpRequest(String uri) {
            super(uri, HttpRequest.Builder::GET);
        }
    }

    public static final class PreparedPostHttpRequest extends PreparedHttpRequest<PreparedPostHttpRequest> {
        private PreparedPostHttpRequest(String uri, HttpRequest.BodyPublisher publisher) {
            super(uri, builder -> {
                checkArgument(nonNull(publisher), "Body publisher parameter should not be a null value");
                return builder.POST(publisher);
            });
        }
    }

    public static final class PreparedPutHttpRequest extends PreparedHttpRequest<PreparedPutHttpRequest> {
        private PreparedPutHttpRequest(String uri, HttpRequest.BodyPublisher publisher) {
            super(uri, builder -> {
                checkArgument(nonNull(publisher), "Body publisher parameter should not be a null value");
                return builder.PUT(publisher);
            });
        }
    }

    public static final class PreparedDeleteHttpRequest extends PreparedHttpRequest<PreparedDeleteHttpRequest> {
        private PreparedDeleteHttpRequest(String uri) {
            super(uri, HttpRequest.Builder::DELETE);
        }
    }

    public static final class PreparedMethodHttpRequest extends PreparedHttpRequest<PreparedMethodHttpRequest> {

        private PreparedMethodHttpRequest(String uri, String method, HttpRequest.BodyPublisher publisher) {
            super(uri, builder -> {
                checkArgument(!isBlank(method), "Method name should not be a null or empty value");
                checkArgument(nonNull(publisher), "Body publisher parameter should not be a null value");
                return builder.method(method, publisher);
            });
        }
    }
}
