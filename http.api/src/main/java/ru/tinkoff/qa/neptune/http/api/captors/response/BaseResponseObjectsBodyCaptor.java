package ru.tinkoff.qa.neptune.http.api.captors.response;

import java.net.http.HttpResponse;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;

import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static java.util.stream.StreamSupport.stream;

interface BaseResponseObjectsBodyCaptor<T> {

    @SuppressWarnings("unchecked")
    default List<T> getCaptured(Object toBeCaptured, Class<T> elementClass, Predicate<T> filterPredicate) {
        return ofNullable(toBeCaptured)
                .map(o -> {
                    var clazz = o.getClass();
                    HttpResponse<?> r = null;
                    if (HttpResponse.class.isAssignableFrom(clazz)) {
                        r = (HttpResponse<?>) o;
                    }

                    return ofNullable(r).flatMap(httpResponse -> ofNullable(httpResponse.body())
                            .map(o1 -> {
                                var cls = o1.getClass();
                                var result = new ArrayList<T>();
                                if (Iterable.class.isAssignableFrom(cls)) {
                                    result.addAll(stream(((Iterable<?>) o1).spliterator(), false)
                                            .filter(o2 -> o2 != null
                                                    && elementClass.isAssignableFrom(o2.getClass())
                                                    && filterPredicate.test((T) o2))
                                            .map(o2 -> (T) o2)
                                            .collect(toList()));
                                } else if (cls.isArray()) {
                                    if (elementClass.isAssignableFrom(cls.getComponentType())) {
                                        result.addAll(Arrays.stream((T[]) o1)
                                                .filter(t -> t != null && filterPredicate.test(t))
                                                .collect(toList()));
                                    }
                                }

                                if (result.size() == 0) {
                                    return null;
                                }

                                return result;
                            }))
                            .orElse(null);
                })
                .orElse(null);
    }

    default List<T> getCaptured(Object toBeCaptured, Class<T> elementClass) {
        return getCaptured(toBeCaptured, elementClass, t -> true);
    }
}
