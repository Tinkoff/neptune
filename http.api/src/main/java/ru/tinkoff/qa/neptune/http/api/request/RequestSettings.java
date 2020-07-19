package ru.tinkoff.qa.neptune.http.api.request;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpTimeoutException;
import java.time.Duration;

/**
 * This type defines default behaviour of some class that helps to build an {@link HttpRequest}.
 *
 * @param <T> is a type that extends {@link RequestSettings}
 */
public interface RequestSettings<T extends RequestSettings<T>> {

    /**
     * Requests the server to acknowledge the request before sending the
     * body. This is disabled by default. If enabled, the server is
     * requested to send an error response or a {@code 100 Continue}
     * response before the client sends the request body. This means the
     * request publisher for the request will not be invoked until this
     * interim response is received.
     *
     * <p></p>
     * Description was taken from Java documents.
     *
     * @param enable {@code true} if Expect continue to be sent
     * @return instance of {@code T}
     */
    T expectContinue(boolean enable);

    /**
     * Sets the preferred {@link HttpClient.Version} for this request.
     *
     * <p> The corresponding {@link HttpResponse} should be checked for the
     * version that was actually used. If the version is not set in a
     * request, then the version requested will be that of the sending
     * {@link HttpClient}.
     *
     * <p></p>
     * Description was taken from Java documents.
     *
     * @param version the HTTP protocol version requested
     * @return instance of {@code T}
     */
    T version(HttpClient.Version version);

    /**
     * Adds the given name value pair to the set of headers for this request.
     * The given value is added to the list of values for that name.
     *
     * <p></p>
     * Description was taken from Java documents.
     *
     * @param name   the header name
     * @param values the header values
     * @return instance of {@code T}
     * @see <a href="https://tools.ietf.org/html/rfc7230#section-3.2"> RFC 7230 section-3.2</a>
     */
    T header(String name, String... values);

    /**
     * Sets a timeout for this request. If the response is not received
     * within the specified timeout then an {@link HttpTimeoutException} is
     * thrown from {@link HttpClient#send(HttpRequest,
     * HttpResponse.BodyHandler) HttpClient::send} or
     * {@link HttpClient#sendAsync(HttpRequest,
     * HttpResponse.BodyHandler) HttpClient::sendAsync}
     * completes exceptionally with an {@code HttpTimeoutException}. The effect
     * of not setting a timeout is the same as setting an infinite Duration, ie.
     * block forever.
     *
     * <p></p>
     * Description was taken from Java documents.
     *
     * @param duration the timeout duration
     * @return instance of {@code T}
     * @throws IllegalArgumentException if the duration is non-positive
     */
    T timeout(Duration duration);

    /**
     * Appends query parameter. Appends query parameter. It gets value of the parameter joined into one string,
     * where given values are separated by delimiter.
     *
     * @param name      parameter name
     * @param delimiter is a delimiter/separator of values.
     * @param values    array of values of the the parameter. The method is designed to accept
     *                  primitive values, objects of primitive wrappers, strings, arrays and
     *                  collections of mentioned types. Any value may contain non ASCII characters.
     * @return instance of {@code T}
     */
    T queryParam(String name, QueryValueDelimiters delimiter, Object... values);

    /**
     * Appends query parameter. It explodes multiple value of the parameter by default.
     *
     * @param name   parameter name
     * @param values array of values of the the parameter. The method is designed to accept
     *               primitive values, objects of primitive wrappers, strings, arrays and
     *               collections of mentioned types. Any value may contain non ASCII characters.
     * @return instance of {@code T}
     */
    T queryParam(String name, Object... values);
}
