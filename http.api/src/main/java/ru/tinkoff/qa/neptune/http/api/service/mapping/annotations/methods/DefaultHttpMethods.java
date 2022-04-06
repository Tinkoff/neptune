package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.net.URI;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory.*;

/**
 * Contains commonly used names of http methods and characteristics of these methods.
 */
public enum DefaultHttpMethods {

    NON_DEFINED("") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return null;
        }
    },
    /**
     * Is for GET-method
     */
    GET("GET") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> METHOD(this.toString(), b).endPoint(uri))
                    .orElseGet(() -> GET().endPoint(uri));
        }

        @Override
        RequestBuilder prepareRequestBuilder(String uriOrFragment, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> METHOD(this.toString(), b).endPoint(uriOrFragment))
                    .orElseGet(() -> GET().endPoint(uriOrFragment));
        }
    },
    /**
     * Is for POST-method
     */
    POST("POST") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> POST(b).endPoint(uri))
                    .orElseGet(() -> POST().endPoint(uri));
        }

        @Override
        RequestBuilder prepareRequestBuilder(String uriOrFragment, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> POST(b).endPoint(uriOrFragment))
                    .orElseGet(() -> POST().endPoint(uriOrFragment));
        }
    },
    /**
     * Is for PUT-method
     */
    PUT("PUT") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> PUT(b).endPoint(uri))
                    .orElseGet(() -> PUT().endPoint(uri));
        }

        @Override
        RequestBuilder prepareRequestBuilder(String uriOrFragment, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> PUT(b).endPoint(uriOrFragment))
                    .orElseGet(() -> PUT().endPoint(uriOrFragment));
        }
    },
    /**
     * Is for DELETE-method
     */
    DELETE("DELETE") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> METHOD(this.toString(), b).endPoint(uri))
                    .orElseGet(() -> DELETE().endPoint(uri));
        }

        @Override
        RequestBuilder prepareRequestBuilder(String uriOrFragment, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> METHOD(this.toString(), b).endPoint(uriOrFragment))
                    .orElseGet(() -> DELETE().endPoint(uriOrFragment));
        }
    },

    /**
     * Is for OPTIONS-method
     */
    OPTIONS("OPTIONS"),

    /**
     * Is for HEAD-method
     */
    HEAD("HEAD"),

    /**
     * Is for PATCH-method
     */
    PATCH("PATCH"),

    /**
     * Is for TRACE-method
     */
    TRACE("TRACE");

    private final String name;

    DefaultHttpMethods(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }

    RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
        return ofNullable(body)
                .map(b -> METHOD(toString(), b).endPoint(uri))
                .orElseGet(() -> METHOD(toString()).endPoint(uri));
    }

    RequestBuilder prepareRequestBuilder(String uriOrFragment, RequestBody<?> body) {
        return ofNullable(body)
                .map(b -> METHOD(toString(), b).endPoint(uriOrFragment))
                .orElseGet(() -> METHOD(toString()).endPoint(uriOrFragment));
    }
}
