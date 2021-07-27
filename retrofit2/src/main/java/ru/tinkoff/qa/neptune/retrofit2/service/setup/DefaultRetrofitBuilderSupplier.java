package ru.tinkoff.qa.neptune.retrofit2.service.setup;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;

public final class DefaultRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

    @Override
    protected Retrofit.Builder prepareRetrofitBuilder() {
        return new Retrofit.Builder();
    }

    @Override
    protected OkHttpClient.Builder prepareClientBuilder() {
        return new OkHttpClient().newBuilder();
    }
}
