package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.retrofit2.StepInterceptor.getCurrentInterceptor;

/**
 * Objects of subclasses are supposed to prepare and return instances of {@link Retrofit}
 */
public abstract class RetrofitSupplier implements Supplier<Retrofit> {

    /**
     * This method should return prepared instances of {@link Retrofit}
     *
     * @return a new or beforehand prepared {@link Retrofit}
     */
    protected abstract Retrofit prepare();

    @Override
    public Retrofit get() {
        var r = prepare();

        var retrofitBuilder = r.newBuilder();
        var factory = (OkHttpClient) r.callFactory();
        var clientBuilder = factory.newBuilder();

        var interceptors = clientBuilder.interceptors();
        interceptors.add(0, getCurrentInterceptor());

        return retrofitBuilder.client(clientBuilder.build()).build();
    }

    public static final class DefaultRetrofitSupplier extends RetrofitSupplier {

        @Override
        protected Retrofit prepare() {
            return new Retrofit.Builder().build();
        }
    }
}
