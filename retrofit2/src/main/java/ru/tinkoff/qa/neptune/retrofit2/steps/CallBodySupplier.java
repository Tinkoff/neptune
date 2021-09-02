package ru.tinkoff.qa.neptune.retrofit2.steps;

import retrofit2.Call;

import java.io.IOException;
import java.util.function.Supplier;

class CallBodySupplier<M> implements Supplier<M> {

    private final Supplier<Call<M>> callSupplier;

    CallBodySupplier(Supplier<Call<M>> callSupplier) {
        this.callSupplier = callSupplier;
    }

    @Override
    public M get() {
        try {
            return callSupplier.get().execute().body();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
