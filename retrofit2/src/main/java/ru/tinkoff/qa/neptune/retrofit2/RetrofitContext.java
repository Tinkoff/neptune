package ru.tinkoff.qa.neptune.retrofit2;


import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.retrofit2.steps.*;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;

/**
 * This is just entry point to invoke some action on retrofit
 */
public class RetrofitContext extends Context<RetrofitContext> {

    public static RetrofitContext retrofit() {
        return getCreatedContextOrCreate(RetrofitContext.class);
    }

    public <M, R> R get(GetObjectSupplier<M, R, ?> what) {
        return super.get(what);
    }

    public <M, R> R[] get(GetArraySupplier<M, R, ?> what) {
        return super.get(what);
    }

    public <M, R, S extends Iterable<R>> List<R> get(GetIterableSupplier<M, R, S, ?> what) {
        return super.get(what);
    }

    public <M, R> R get(GetObjectFromArraySupplier<M, R, ?> what) {
        return super.get(what);
    }

    public <M, R> R get(GetObjectFromIterableSupplier<M, R, ?> what) {
        return super.get(what);
    }
}
