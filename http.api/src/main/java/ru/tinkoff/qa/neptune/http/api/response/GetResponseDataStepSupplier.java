package ru.tinkoff.qa.neptune.http.api.response;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.http.api.ResponseHasNoDesiredDataException;

import java.lang.reflect.InvocationTargetException;
import java.util.function.Supplier;

import static java.util.Optional.ofNullable;

@SuppressWarnings("unchecked")
public abstract class GetResponseDataStepSupplier<T, R, S extends GetResponseDataStepSupplier<T, R, S>> extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<T, R, R, S> {

    private SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> from;

    GetResponseDataStepSupplier(String description) {
        super(description, r -> r);
    }

    @Override
    protected S from(SequentialGetStepSupplier<T, ? extends R, ?, ?, ?> from) {
        super.from(from);
        this.from = from;
        return (S) this;
    }

    public S throwWhenNothing(String exceptionMessage) {
        ofNullable(from).ifPresent(tSequentialGetStepSupplier -> {
            var clazz = tSequentialGetStepSupplier.getClass();
            try {
                var m = clazz.getDeclaredMethod("throwOnEmptyResult", Supplier.class);
                m.setAccessible(true);
                m.invoke(tSequentialGetStepSupplier,
                        (Supplier<ResponseHasNoDesiredDataException>) () -> new ResponseHasNoDesiredDataException(exceptionMessage));
            } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
               throw new RuntimeException(e);
            }
        });

        return (S) this;
    }
}
