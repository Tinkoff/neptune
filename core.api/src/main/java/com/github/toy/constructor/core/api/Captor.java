package com.github.toy.constructor.core.api;

import java.util.List;

import static java.util.List.of;

/**
 * This class is designed to catch events for the logging/report.
 *
 * @param <T> is a type of an object to be caught for the logging/reporting.
 * @param <S> is a type of produced data.
 */
public abstract class Captor<T, S> {

    private final List<? extends CapturedDataInjector<S>> injectors;

    public Captor(List<? extends CapturedDataInjector<S>> injectors) {
        this.injectors = injectors;
    }

    public Captor() {
        this(of());
    }

    void capture(T caught, String message) {
        S s = getData(caught);
        injectors.forEach(injector -> injector.inject(s, message));
    }

    /**
     * Gets/transforms data from a caught object to inject to a log/report.
     *
     * @param caught is a caught object to get data for the injecting.
     * @return S produced data to be injected to a log/report
     */
    protected abstract S getData(T caught);
}
