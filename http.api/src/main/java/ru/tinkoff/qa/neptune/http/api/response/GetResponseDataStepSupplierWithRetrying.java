package ru.tinkoff.qa.neptune.http.api.response;

import java.lang.reflect.InvocationTargetException;
import java.time.Duration;

import static java.util.Optional.ofNullable;

public final class GetResponseDataStepSupplierWithRetrying<T, R> extends GetResponseDataStepSupplier<T, R, GetResponseDataStepSupplierWithRetrying<T, R>> {

    GetResponseDataStepSupplierWithRetrying(String description) {
        super(description);
    }

    @Override
    public GetResponseDataStepSupplierWithRetrying<T, R> timeOut(Duration timeOut) {
        ofNullable(getFrom()).ifPresent(tSequentialGetStepSupplier -> {
            var clazz = tSequentialGetStepSupplier.getClass();
            try {
                var m = clazz.getDeclaredMethod("timeOut", Duration.class);
                m.setAccessible(true);
                m.invoke(tSequentialGetStepSupplier, timeOut);
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
                throw new RuntimeException(e);
            }
        });
        return this;
    }
}
