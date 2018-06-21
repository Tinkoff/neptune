package com.github.toy.constructor.core.api;

public interface CapturedDataInjector<T> {

    /**
     * Injects data to a lag/report.
     *
     * @param toBeInjected data to be injected.
     * @param message message to be injected.
     */
    void inject(T toBeInjected, String message);
}
