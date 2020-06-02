package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import static java.util.Optional.ofNullable;

interface BaseRequestBodyCaptor<T extends RequestBody<?>> {

    @SuppressWarnings("unchecked")
    default T getCaptured(Object toBeCaptured, Class<T> needed) {
        return ofNullable(toBeCaptured)
                .map(o -> {
                    var cls = toBeCaptured.getClass();
                    if (needed.isAssignableFrom(cls)) {
                        return (T) toBeCaptured;
                    }
                    return null;
                })
                .orElse(null);
    }
}

