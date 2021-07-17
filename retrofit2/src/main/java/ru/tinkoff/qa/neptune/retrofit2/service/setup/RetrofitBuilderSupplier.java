package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.retrofit2.StepInterceptor.getCurrentInterceptor;

/**
 * Objects of subclasses are supposed to prepare and return instances of {@link Retrofit.Builder}
 */
public abstract class RetrofitBuilderSupplier implements Supplier<Retrofit.Builder> {

    /**
     * This method should return prepared instances of {@link Retrofit.Builder}
     *
     * @return a new or beforehand prepared {@link Retrofit.Builder}
     */
    protected abstract Retrofit.Builder prepareRetrofitBuilder();

    protected abstract OkHttpClient.Builder prepareClientBuilder();

    @Override
    public Retrofit.Builder get() {
        var r = prepareRetrofitBuilder();
        var c = prepareClientBuilder();

        var interceptors = c.interceptors();
        interceptors.add(0, getCurrentInterceptor());

        return r.client(c.build());
    }

    public static final class DefaultRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

        @Override
        protected Retrofit.Builder prepareRetrofitBuilder() {
            return new Retrofit.Builder();
        }

        @Override
        protected OkHttpClient.Builder prepareClientBuilder() {
            return new OkHttpClient().newBuilder();
        }
    }
}
