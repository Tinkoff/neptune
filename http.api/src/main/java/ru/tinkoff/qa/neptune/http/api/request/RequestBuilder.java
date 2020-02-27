package ru.tinkoff.qa.neptune.http.api.request;

import org.glassfish.jersey.uri.internal.JerseyUriBuilder;

import javax.ws.rs.core.UriBuilder;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.valueOf;
import static java.util.Optional.ofNullable;

abstract class RequestBuilder {
    final HttpRequest.Builder builder;
    private final UriBuilder uriBuilder = new JerseyUriBuilder();

    RequestBuilder(URI endPoint) {
        checkNotNull(endPoint);
        builder = HttpRequest.newBuilder();
        uriBuilder.uri(endPoint);
    }

    public RequestBuilder(URL url) {
        this(ofNullable(url)
                .map(u -> {
                    try {
                        return u.toURI();
                    } catch (URISyntaxException e) {
                        throw new IllegalArgumentException(e.getMessage(), e);
                    }
                })
                .orElseThrow(() -> new IllegalArgumentException("Endpoint url should not be null")));
    }

    public RequestBuilder(String uriExpression) {
        this(ofNullable(uriExpression)
                .map(URI::create)
                .orElseThrow(() -> new IllegalArgumentException("Endpoint string uri should not be null")));
    }

    /**
     * Requests the server to acknowledge the request before sending the
     * body. This is disabled by default. If enabled, the server is
     * requested to send an error response or a {@code 100 Continue}
     * response before the client sends the request body. This means the
     * request publisher for the request will not be invoked until this
     * interim response is received.
     *
     * <p></p>
     * Description was taken from Java 11 documents.
     *
     * @param enable {@code true} if Expect continue to be sent
     * @return this builder
     */
    public RequestBuilder expectContinue(boolean enable) {
        builder.expectContinue(enable);
        return this;
    }

    /**
     * Sets the preferred {@link HttpClient.Version} for this request.
     *
     * <p> The corresponding {@link HttpResponse} should be checked for the
     * version that was actually used. If the version is not set in a
     * request, then the version requested will be that of the sending
     * {@link HttpClient}.
     *
     * <p></p>
     * Description was taken from Java 11 documents.
     *
     * @param version the HTTP protocol version requested
     * @return this builder
     */
    public RequestBuilder version(HttpClient.Version version) {
        builder.version(version);
        return this;
    }

    /**
     * Adds the given name value pair to the set of headers for this request.
     * The given value is added to the list of values for that name.
     *
     * <p></p>
     * Description was taken from Java 11 documents.
     *
     * @param name  the header name
     * @param value the header value
     * @return this builder
     * @throws IllegalArgumentException if the header name or value is not
     *                                  valid, see <a href="https://tools.ietf.org/html/rfc7230#section-3.2">
     *                                  RFC 7230 section-3.2</a>, or the header name or value is restricted
     *                                  by the implementation.
     * @implNote An implementation may choose to restrict some header names
     * or values, as the HTTP Client may determine their value itself.
     * For example, "Content-Length", which will be determined by
     * the request Publisher. In such a case, an implementation of
     * {@code HttpRequest.Builder} may choose to throw an
     * {@code IllegalArgumentException} if such a header is passed
     * to the builder.
     */
    public RequestBuilder header(String name, String value) {
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
     * <p></p>
     * Description was taken from Java 11 documents.
     *
     * @param headers the list of name value pairs
     * @return this builder
     * @throws IllegalArgumentException if there are an odd number of
     *                                  parameters, or if a header name or value is not valid, see
     *                                  <a href="https://tools.ietf.org/html/rfc7230#section-3.2">
     *                                  RFC 7230 section-3.2</a>, or a header name or value is
     *                                  {@linkplain #header(String, String) restricted} by the
     *                                  implementation.
     */
    public RequestBuilder headers(String... headers) {
        builder.headers(headers);
        return this;
    }

    /**
     * Sets a timeout for this request. If the response is not received
     * within the specified timeout then an {@link HttpTimeoutException} is
     * thrown from {@link HttpClient#send(java.net.http.HttpRequest,
     * java.net.http.HttpResponse.BodyHandler) HttpClient::send} or
     * {@link HttpClient#sendAsync(java.net.http.HttpRequest,
     * java.net.http.HttpResponse.BodyHandler) HttpClient::sendAsync}
     * completes exceptionally with an {@code HttpTimeoutException}. The effect
     * of not setting a timeout is the same as setting an infinite Duration, ie.
     * block forever.
     *
     * <p></p>
     * Description was taken from Java 11 documents.
     *
     * @param duration the timeout duration
     * @return this builder
     * @throws IllegalArgumentException if the duration is non-positive
     */
    public RequestBuilder timeout(Duration duration) {
        builder.timeout(duration);
        return this;
    }

    /**
     * Sets the given name value pair to the set of headers for this
     * request. This overwrites any previously set values for name.
     *
     * <p></p>
     * Description was taken from Java 11 documents.
     *
     * @param name  the header name
     * @param value the header value
     * @return this builder
     * @throws IllegalArgumentException if the header name or value is not valid,
     *                                  see <a href="https://tools.ietf.org/html/rfc7230#section-3.2">
     *                                  RFC 7230 section-3.2</a>, or the header name or value is
     *                                  {@linkplain #header(String, String) restricted} by the
     *                                  implementation.
     */
    public RequestBuilder setHeader(String name, String value) {
        builder.setHeader(name, value);
        return this;
    }

    /**
     * Adds query parameter to the given URI
     *
     * @param name   parameter name
     * @param values values of the parameter
     * @return self-reference
     */
    public RequestBuilder queryParam(String name, final Object... values) {
        uriBuilder.queryParam(name, values);
        return this;
    }

    public HttpRequest build() {
        return builder.uri(uriBuilder.build()).build();
    }


    @Override
    public String toString() {
        return valueOf(builder.build());
    }
}
