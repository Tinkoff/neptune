package ru.tinkoff.qa.neptune.retrofit2.tests.retrofit.suppliers;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.tinkoff.qa.neptune.core.api.binding.Bind;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.RetrofitBuilderSupplier;
import ru.tinkoff.qa.neptune.retrofit2.tests.SynchronousCallAdapterFactory;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService2;

@Bind(to = CustomService2.class, withSubclasses = true)
public class SyncAdaptorRetrofitBuilderSupplier extends RetrofitBuilderSupplier {

    @Override
    protected Retrofit.Builder prepareRetrofitBuilder() {
        return new Retrofit.Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(SynchronousCallAdapterFactory.create());
    }

    @Override
    protected OkHttpClient.Builder prepareClientBuilder() {
        return new OkHttpClient().newBuilder();
    }
}
