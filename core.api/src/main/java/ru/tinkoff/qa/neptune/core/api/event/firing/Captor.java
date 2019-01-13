package ru.tinkoff.qa.neptune.core.api.event.firing;

import java.util.List;

import static java.util.Optional.ofNullable;

/**
 * This class is designed to catch different objects for the logging/report.
 *
 * @param <T> is a type of an object to be caught for the logging/reporting.
 * @param <S> is a type of produced data.
 */
public abstract class Captor<T, S> {

    private final String message;
    protected final List<? extends CapturedDataInjector<S>> injectors;

    public Captor(String message, List<? extends CapturedDataInjector<S>> injectors) {
        this.message = message;
        this.injectors = injectors;
    }

    public void capture(T caught) {
        S s = getData(caught);
        ofNullable(s).ifPresent(s1 -> injectors.forEach(injector -> injector.inject(s1, message)));
    }

    /**
     * Gets/transforms data from a caught object to inject into log/report.
     *
     * @param caught is a caught object to get data for the injecting.
     * @return S produced data to be injected to a log/report
     */
    public abstract S getData(T caught);

    /**
     * This method is supposed to be used to convert an object to a value that can be logged and reported.
     * When it is not possible then it should return {@code null}.
     *
     * @param toBeCaptured is an object to get value that is supposed to be captured and logged.
     * @return a value to be captured and logged or {@code null}
     */
    public abstract T getCaptured(Object toBeCaptured);
}
