package ru.tinkoff.qa.neptune.http.api.service.mapping.annotations.methods;

import ru.tinkoff.qa.neptune.http.api.request.RequestBuilder;
import ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.request.RequestBuilderFactory.METHOD;

/**
 * Contains commonly used names of http methods and characteristics of these methods.
 */
public enum DefaultHttpMethods {

    NON_DEFINED("") {
        @Override
        RequestBuilder<Void> prepareRequestBuilder(RequestBody<?> body) {
            return null;
        }
    },
    /**
     * Is for GET-method
     */
    GET("GET") {
        @Override
        RequestBuilder<Void> prepareRequestBuilder(RequestBody<?> body) {
            return ofNullable(body)
                .map(b -> METHOD(this.toString(), b))
                .orElseGet(RequestBuilderFactory::GET);
        }
    },
    /**
     * Is for POST-method
     */
    POST("POST") {
        @Override
        RequestBuilder<Void> prepareRequestBuilder(RequestBody<?> body) {
            return ofNullable(body)
                .map(RequestBuilderFactory::POST)
                .orElseGet(RequestBuilderFactory::POST);
        }
    },
    /**
     * Is for PUT-method
     */
    PUT("PUT") {
        @Override
        RequestBuilder<Void> prepareRequestBuilder(RequestBody<?> body) {
            return ofNullable(body)
                .map(RequestBuilderFactory::PUT)
                .orElseGet(RequestBuilderFactory::PUT);
        }
    },
    /**
     * Is for DELETE-method
     */
    DELETE("DELETE") {
        @Override
        RequestBuilder<Void> prepareRequestBuilder(RequestBody<?> body) {
            return ofNullable(body)
                .map(b -> METHOD(this.toString(), b))
                .orElseGet(RequestBuilderFactory::DELETE);
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

    RequestBuilder<Void> prepareRequestBuilder(RequestBody<?> body) {
        return ofNullable(body)
            .map(b -> METHOD(toString(), b))
            .orElseGet(() -> METHOD(toString()));
    }
}
