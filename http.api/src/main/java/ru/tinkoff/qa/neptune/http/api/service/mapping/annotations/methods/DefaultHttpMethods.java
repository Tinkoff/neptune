package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.net.URI;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilder.*;

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
                    .map(b -> METHOD(this.toString(), uri, b))
                    .orElseGet(() -> GET(uri));
        }
    },
    /**
     * Is for POST-method
     */
    POST("POST") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> POST(uri, b))
                    .orElseGet(() -> POST(uri));
        }
    },
    /**
     * Is for PUT-method
     */
    PUT("PUT") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> PUT(uri, b))
                    .orElseGet(() -> PUT(uri));
        }
    },
    /**
     * Is for DELETE-method
     */
    DELETE("DELETE") {
        @Override
        RequestBuilder prepareRequestBuilder(URI uri, RequestBody<?> body) {
            return ofNullable(body)
                    .map(b -> METHOD(this.toString(), uri, b))
                    .orElseGet(() -> DELETE(uri));
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
                .map(b -> METHOD(toString(), uri, b))
                .orElseGet(() -> METHOD(toString(), uri));
    }
}
