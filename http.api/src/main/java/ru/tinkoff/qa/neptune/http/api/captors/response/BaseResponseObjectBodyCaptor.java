package ru.tinkoff.qa.neptune.http.api.captors.response;

import java.net.http.HttpResponse;

import static java.util.Optional.ofNullable;

interface BaseResponseObjectBodyCaptor<T> {

    @SuppressWarnings("unchecked")
    default HttpResponse<T> getCaptured(Object toBeCaptured, Class<T> cls) {
        return ofNullable(toBeCaptured)
                .map(o -> {
                    var clazz = o.getClass();
                    HttpResponse<?> r = null;
                    if (HttpResponse.class.isAssignableFrom(clazz)) {
                        r = (HttpResponse<?>) o;
                    }

                    return ofNullable(r)
                            .flatMap(httpResponse -> ofNullable(httpResponse.body())
                                    .map(o1 -> {
                                        if (!cls.isAssignableFrom(o1.getClass())) {
                                            return null;
                                        }

                                        return (HttpResponse<T>) httpResponse;
                                    }))
                            .orElse(null);

                })
                .orElse(null);
    }
}
