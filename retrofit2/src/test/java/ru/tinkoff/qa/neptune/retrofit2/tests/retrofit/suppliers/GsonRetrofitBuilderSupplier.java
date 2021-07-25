package ru.tinkoff.qa.neptune.retrofit2.tests.retrofit.suppliers;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;

public class GsonRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

    @Override
    protected Retrofit.Builder prepareRetrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create());
    }

    @Override
    protected OkHttpClient.Builder prepareClientBuilder() {
        return new OkHttpClient().newBuilder();
    }
}
