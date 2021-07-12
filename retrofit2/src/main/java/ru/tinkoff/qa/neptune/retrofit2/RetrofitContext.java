package ru.tinkoff.qa.neptune.retrofit2;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;

/**
 * This is just entry point to invoke some action on retrofit
 */
public final class RetrofitContext<T> extends Context<RetrofitContext<T>> {

    private final StepInterceptor interceptor = StepInterceptor.getCurrentInterceptor();
    private T service;

    @SuppressWarnings("unchecked")
    public static <T> RetrofitContext<T> from(T service) {
        return getInstance(RetrofitContext.class).setService(service);
    }

    public <R> R receive(SequentialGetStepSupplier<? super RetrofitContext<T>, R, ?, ?, ?> what) {
        return super.get(what);
    }

    public T getService() {
        return service;
    }

    private RetrofitContext<T> setService(T service) {
        this.service = service;
        return this;
    }
}
