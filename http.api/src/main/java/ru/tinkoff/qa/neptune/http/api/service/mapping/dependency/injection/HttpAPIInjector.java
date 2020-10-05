package ru.tinkoff.qa.neptune.http.api.service.mapping.dependency.injection;

import ru.tinkoff.qa.neptune.core.api.dependency.injection.DependencyInjector;
import ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI;

import java.lang.reflect.Field;

import static ru.tinkoff.qa.neptune.http.api.service.mapping.HttpAPI.createAPI;

/**
 * Initializes fields of {@link HttpAPI}
 */
@SuppressWarnings({"unchecked", "rawtypes"})
public class HttpAPIInjector implements DependencyInjector {

    @Override
    public boolean toSet(Field field) {
        return HttpAPI.class.isAssignableFrom(field.getType());
    }

    @Override
    public Object getValueToSet(Field field) {
        return createAPI((Class<? extends HttpAPI>) field.getType());
    }
}
