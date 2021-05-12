package ru.tinkoff.qa.neptune.http.api.captors.response;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;

import java.net.http.HttpResponse;
import java.util.List;

import static java.util.Optional.ofNullable;

public abstract class AbstractResponseBodyObjectCaptor<T, R> extends Captor<T, R> {

    private final Class<T> needed;

    public AbstractResponseBodyObjectCaptor(List<? extends CapturedDataInjector<R>> capturedDataInjectors, Class<T> needed) {
        super(capturedDataInjectors);
        this.needed = needed;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T getCaptured(Object toBeCaptured) {
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
                                        if (!needed.isAssignableFrom(o1.getClass())) {
                                            return null;
                                        }
                                        return (T) o1;
                                    }))
                            .orElse(null);

                })
                .orElse(null);
    }
}
