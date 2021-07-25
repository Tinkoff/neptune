package ru.tinkoff.qa.neptune.retrofit2.dependency.injection;

import ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.ApiService;
import ru.tinkoff.qa.neptune.retrofit2.service.setup.UseRetrofitSettings;

import java.lang.reflect.Field;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.retrofit2.service.setup.HttpServiceCreator.create;
import static ru.tinkoff.qa.neptune.retrofit2.service.setup.UseRetrofitSettings.UseRetrofitSettingsReader.getRetrofit;

public class RetrofitDependencyInjector implements DependencyInjector {

    @Override
    public boolean toSet(Field field) {
        return field.getAnnotation(ApiService.class) != null;
    }

    @Override
    public Object getValueToSet(Field field) {
        var customRetrofit = field.getAnnotation(UseRetrofitSettings.class);

        return ofNullable(customRetrofit)
                .map(r -> create(field.getType(), getRetrofit(r)))
                .orElseGet(() -> create(field.getType()));
    }
}
