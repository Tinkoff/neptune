package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URL;

/**
 * Designed to create a GET-request
 */
public final class GetRequest extends RequestBuilder<GetRequest> {


    private GetRequest(URI endPoint) {
        super(endPoint);
        builder.GET();
    }

    private GetRequest(URL url) {
        super(url);
        builder.GET();
    }

    private GetRequest(String uriExpression) {
        super(uriExpression);
        builder.GET();
    }

    /**
     * Creates an instance that builds a GET request.
     *
     * @param uriExpression is a request end point
     * @return new {@link GetRequest}
     */
    public static GetRequest GET(String uriExpression) {
        return new GetRequest(uriExpression);
    }

    /**
     * Creates an instance that builds a GET request.
     *
     * @param uri is a request end point
     * @return new {@link GetRequest}
     */
    public static GetRequest GET(URI uri) {
        return new GetRequest(uri);
    }

    /**
     * Creates an instance that builds a GET request.
     *
     * @param url is a request end point
     * @return new {@link GetRequest}
     */
    public static GetRequest GET(URL url) {
        return new GetRequest(url);
    }
}
