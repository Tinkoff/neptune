package ru.tinkoff.qa.neptune.http.api.captors.request;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.CapturedDataInjector;
import ru.tinkoff.qa.neptune.http.api.request.body.RequestBody;

import java.util.List;

import static java.util.Optional.ofNullable;

public abstract class AbstractRequestBodyCaptor<T extends RequestBody<?>, R> extends Captor<T, R> {

    private final List<Class<? extends RequestBody<?>>> needed;

    public AbstractRequestBodyCaptor(List<? extends CapturedDataInjector<R>> capturedDataInjectors,
                                     List<Class<? extends RequestBody<?>>> needed) {
        super(capturedDataInjectors);
        this.needed = needed;
    }

    @Override
    public T getCaptured(Object toBeCaptured) {
        return ofNullable(getCaptured(toBeCaptured, needed))
                .map(this::convertTo)
                .orElse(null);
    }

    private RequestBody<?> getCaptured(Object toBeCaptured, List<Class<? extends RequestBody<?>>> needed) {
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

    abstract T convertTo(RequestBody<?> requestBody);
}
