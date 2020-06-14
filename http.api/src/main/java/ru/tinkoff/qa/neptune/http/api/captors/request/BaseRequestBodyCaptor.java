package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.util.List;

import static java.util.Optional.ofNullable;

interface BaseRequestBodyCaptor {

    default RequestBody<?> getCaptured(Object toBeCaptured, List<Class<? extends RequestBody<?>>> needed) {
        return ofNullable(toBeCaptured)
                .map(o -> {
                    var cls = toBeCaptured.getClass();
                    if (needed.stream().anyMatch(aClass -> aClass.isAssignableFrom(cls))) {
                        return (RequestBody<?>) toBeCaptured;
                    }
                    return null;
                })
                .orElse(null);
    }
}

