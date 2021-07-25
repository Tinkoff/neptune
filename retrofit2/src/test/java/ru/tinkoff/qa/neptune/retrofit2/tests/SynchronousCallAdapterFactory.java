package ru.tinkoff.qa.neptune.retrofit2.tests;

import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

public class SynchronousCallAdapterFactory extends CallAdapter.Factory {

    public static CallAdapter.Factory create() {
        return new SynchronousCallAdapterFactory();
    }

    @Override
    public CallAdapter<Object, Object> get(final Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (returnType.toString().contains("retrofit2.Call")) {
            return null;
        }

        return new CallAdapter<>() {
            @Override
            public Type responseType() {
                return returnType;
            }

            @Override
            public Object adapt(Call<Object> call) {
                try {
                    return call.execute().body();
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        };
    }
}
