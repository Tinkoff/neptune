package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * This class is designed to read value of the property {@code 'default.http.executor'} and convert it to an instance of
 * {@link ExecutorSupplier}
 */
public final class DefaultHttpExecutorProperty implements ObjectPropertySupplier<DefaultHttpExecutorProperty.ExecutorSupplier> {

    private static final String PROPERTY = "default.http.executor";

    /**
     * This instance reads value of the property {@code 'default.http.executor'} and returns a {@link Supplier}
     * of {@link Executor}. Invocation of the {@link Supplier#get()} returns actual value of the property. The value
     * provided must be fully qualified name of a {@link ExecutorSupplier} subclass.
     */
    public static final DefaultHttpExecutorProperty DEFAULT_HTTP_EXECUTOR_PROPERTY =
            new DefaultHttpExecutorProperty();

    private DefaultHttpExecutorProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY;
    }

    public static abstract class ExecutorSupplier implements Supplier<Executor> {
    }
}
