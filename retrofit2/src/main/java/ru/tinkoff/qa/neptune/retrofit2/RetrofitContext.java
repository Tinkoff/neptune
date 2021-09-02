package ru.tinkoff.qa.neptune.retrofit2;


import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.retrofit2.steps.*;

/**
 * This is just entry point to invoke some action on retrofit
 */
public class RetrofitContext extends Context<RetrofitContext> {

    private static final RetrofitContext context = getInstance(RetrofitContext.class);

    public static RetrofitContext retrofit() {
        return context;
    }

    public <M, R> R get(GetObjectSupplier<M, R, ?> what) {
        return super.get(what);
    }

    public <M, R> R[] get(GetArraySupplier<M, R, ?> what) {
        return super.get(what);
    }

    public <M, R, S extends Iterable<R>> S get(GetIterableSupplier<M, R, S, ?> what) {
        return super.get(what);
    }

    public <M, R> R get(GetObjectFromArraySupplier<M, R, ?> what) {
        return super.get(what);
    }

    public <M, R> R get(GetObjectFromIterableSupplier<M, R, ?> what) {
        return super.get(what);
    }
}
