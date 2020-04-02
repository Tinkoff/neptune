package ru.tinkoff.qa.neptune.http.api.request;

import java.net.URI;
import java.net.URL;

public final class DeleteRequest extends RequestBuilder<DeleteRequest> {

    private DeleteRequest(URI endPoint) {
        super(endPoint);
        builder.DELETE();
    }

    private DeleteRequest(URL url) {
        super(url);
        builder.DELETE();
    }

    private DeleteRequest(String uriExpression) {
        super(uriExpression);
        builder.DELETE();
    }

    /**
     * Creates an instance that builds a DELETE request.
     *
     * @param uriExpression is a request end point
     * @return new {@link DeleteRequest}
     */
    public static DeleteRequest DELETE(String uriExpression) {
        return new DeleteRequest(uriExpression);
    }

    /**
     * Creates an instance that builds a DELETE request.
     *
     * @param uri is a request end point
     * @return new {@link DeleteRequest}
     */
    public static DeleteRequest DELETE(URI uri) {
        return new DeleteRequest(uri);
    }

    /**
     * Creates an instance that builds a DELETE request.
     *
     * @param url is a request end point
     * @return new {@link DeleteRequest}
     */
    public static DeleteRequest DELETE(URL url) {
        return new DeleteRequest(url);
    }
}
