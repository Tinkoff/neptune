package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URL;
import java.net.http.HttpRequest;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;

/**
 * Designed to create a PUT-request
 */
public final class PutRequest extends RequestBuilder<PutRequest> {

    private PutRequest(URI endPoint, HttpRequest.BodyPublisher bodyPublisher) {
        super(endPoint);
        defineBody(bodyPublisher);
    }

    private PutRequest(URL url, HttpRequest.BodyPublisher bodyPublisher) {
        super(url);
        defineBody(bodyPublisher);
    }

    private PutRequest(String uriExpression, HttpRequest.BodyPublisher bodyPublisher) {
        super(uriExpression);
        defineBody(bodyPublisher);
    }

    private void defineBody(HttpRequest.BodyPublisher bodyPublisher) {
        checkArgument(nonNull(bodyPublisher), "Body publisher should not be a null value");
        builder.PUT(bodyPublisher);
    }

    /**
     * Creates an instance that builds a PUT request.
     *
     * @param endPoint is a request end point
     * @param bodyPublisher the body publisher
     * @return new {@link PutRequest}
     */
    public static PutRequest PUT(URI endPoint, HttpRequest.BodyPublisher bodyPublisher) {
        return new PutRequest(endPoint, bodyPublisher);
    }

    /**
     * Creates an instance that builds a PUT request.
     *
     * @param url is a request end point
     * @param bodyPublisher the body publisher
     * @return new {@link PutRequest}
     */
    public static PutRequest PUT(URL url, HttpRequest.BodyPublisher bodyPublisher) {
        return new PutRequest(url, bodyPublisher);
    }

    /**
     * Creates an instance that builds a PUT request.
     *
     * @param uriExpression is a request end point
     * @param bodyPublisher the body publisher
     * @return new {@link PutRequest}
     */
    public static PutRequest PUT(String uriExpression, HttpRequest.BodyPublisher bodyPublisher) {
        return new PutRequest(uriExpression, bodyPublisher);
    }
}
