package ru.tinkoff.qa.neptune.http.api;

import org.glassfish.jersey.uri.internal.JerseyUriBuilder;
import ru.tinkoff.qa.neptune.http.api.properties.DefaultHttpDomainToRespondProperty;

import javax.ws.rs.core.UriBuilder;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpTimeoutException;
import java.time.Duration;
import java.util.function.Consumer;

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
public class PreparedHttpRequest {

    private final HttpRequest.Builder builder;
    private final UriBuilder uriBuilder = new JerseyUriBuilder();


    private PreparedHttpRequest(String uri, Consumer<HttpRequest.Builder> builderPreparing) {
        checkArgument(!isBlank(uri), "URI parameter should not be a null or empty value");

        try {
            uriBuilder.uri(new URL(uri).toString());
        } catch (MalformedURLException e) {
            uriBuilder.uri(ofNullable(DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY.get())
                    .map(url -> {
                        try {
                            return new URL(url.toString() + uri).toString();
                        } catch (MalformedURLException e1) {
                            throw new IllegalArgumentException(e1.getMessage(), e1);
                        }
                    })
                    .orElseThrow(() -> new IllegalArgumentException(format("It is impossible to make a request by URI %s. " +
                                    "This value is not a valid URI and the property %s is not defined", uri,
                            DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY.getPropertyName()))));
        }

        builder = newBuilder();
        uriBuilder.uri(uri);
        builderPreparing.accept(builder);
    }

    /**
     * Makes preparation for the sending GET-request
     *
     * @param uri to send a GET-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedHttpRequest GET(String uri) {
        return new PreparedHttpRequest(uri, HttpRequest.Builder::GET);
    }

    /**
     * Makes preparation for the sending POST-request
     *
     * @param uri       to send a POST-request. It is possible to define the fully qualified URI as well as a relative path
     *                  when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param publisher body publisher of the request
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedHttpRequest POST(String uri, HttpRequest.BodyPublisher publisher) {
        return new PreparedHttpRequest(uri, builder -> {
            checkArgument(nonNull(publisher), "Body publisher parameter should not be a null value");
            builder.POST(publisher);
        });
    }

    /**
     * Makes preparation for the sending PUT-request
     *
     * @param uri       to send a PUT-request. It is possible to define the fully qualified URI as well as a relative path
     *                  when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param publisher body publisher of the request
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedHttpRequest PUT(String uri, HttpRequest.BodyPublisher publisher) {
        return new PreparedHttpRequest(uri, builder -> {
            checkArgument(nonNull(publisher), "Body publisher parameter should not be a null value");
            builder.PUT(publisher);
        });
    }

    /**
     * Makes preparation for the sending DELETE-request
     *
     * @param uri to send a DELETE-request. It is possible to define the fully qualified URI as well as a relative path
     *            when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedHttpRequest DELETE(String uri) {
        return new PreparedHttpRequest(uri, HttpRequest.Builder::DELETE);
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
    public static PreparedHttpRequest methodRequest(String uri, String method, HttpRequest.BodyPublisher publisher) {
        return new PreparedHttpRequest(uri, builder -> {
            checkArgument(!isBlank(method), "Method name should not be a null or empty value");
            checkArgument(nonNull(publisher), "Body publisher parameter should not be a null value");
            builder.method(method, publisher);
        });
    }

    /**
     * Makes preparation for the sending a request. It sets the request method to the given value.
     *
     * @param uri    to send a request. It is possible to define the fully qualified URI as well as a relative path
     *               when the {@link DefaultHttpDomainToRespondProperty#DEFAULT_HTTP_DOMAIN_TO_RESPOND_PROPERTY} is defined
     * @param method a method to send
     * @return a new instance of {@link PreparedHttpRequest}
     */
    public static PreparedHttpRequest methodRequest(String uri, String method) {
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
    public PreparedHttpRequest header(String name, String value) {
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
    public PreparedHttpRequest headers(String... headers) {
        builder.headers(headers);
        return this;
    }

    /**
     * Sets the given name value pair to the set of headers for this
     * request. This overwrites any previously set values for name.
     *
     * @param name  the header name
     * @param value the header value
     * @return self-reference
     */
    public PreparedHttpRequest setHeader(String name, String value) {
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
    public PreparedHttpRequest timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    /**
     * Sets the preferred {@link HttpClient.Version} for this request.
     *
     * @param version the HTTP protocol version requested
     * @return self-reference
     */
    public PreparedHttpRequest version(HttpClient.Version version) {
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
    public PreparedHttpRequest expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return this;
    }

    /**
     * Adds query parameter to the given URI
     *
     * @param name   parameter name
     * @param values values of the parameter
     * @return self-reference
     */
    public PreparedHttpRequest queryParam(String name, final Object... values) {
        uriBuilder.queryParam(name, values);
        return this;
    }

    public HttpRequest build() {
        builder.uri(uriBuilder.build());
        return builder.build();
    }
}
