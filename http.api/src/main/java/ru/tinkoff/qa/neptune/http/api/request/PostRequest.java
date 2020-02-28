package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * Designed to create a POST-request
 */
public final class PostRequest extends RequestBuilder {

    private PostRequest(URI endPoint, HttpRequest.BodyPublisher bodyPublisher) {
        super(endPoint);
        defineBody(bodyPublisher);
    }

    private PostRequest(URL url, HttpRequest.BodyPublisher bodyPublisher) {
        super(url);
        defineBody(bodyPublisher);
    }

    private PostRequest(String uriExpression, HttpRequest.BodyPublisher bodyPublisher) {
        super(uriExpression);
        defineBody(bodyPublisher);
    }

    private void defineBody(HttpRequest.BodyPublisher bodyPublisher) {
        checkArgument(nonNull(bodyPublisher), "Body publisher should not be a null value");
        builder.POST(bodyPublisher);
    }

    /**
     * Creates an instance that builds a POST request.
     *
     * @param endPoint is a request end point
     * @param bodyPublisher the body publisher
     * @return new {@link PostRequest}
     */
    public static PostRequest POST(URI endPoint, HttpRequest.BodyPublisher bodyPublisher) {
        return new PostRequest(endPoint, bodyPublisher);
    }

    /**
     * Creates an instance that builds a POST request.
     *
     * @param url is a request end point
     * @param bodyPublisher the body publisher
     * @return new {@link PostRequest}
     */
    public static PostRequest METHOD(URL url, HttpRequest.BodyPublisher bodyPublisher) {
        return new PostRequest(url, bodyPublisher);
    }

    /**
     * Creates an instance that builds a POST request.
     *
     * @param uriExpression is a request end point
     * @param bodyPublisher the body publisher
     * @return new {@link PostRequest}
     */
    public static PostRequest METHOD(String uriExpression, HttpRequest.BodyPublisher bodyPublisher) {
        return new PostRequest(uriExpression, bodyPublisher);
    }
}
