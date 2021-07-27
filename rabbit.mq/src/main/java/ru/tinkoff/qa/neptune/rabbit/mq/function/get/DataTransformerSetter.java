package ru.tinkoff.qa.neptune.rabbit.mq.function.get;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

public final class DataTransformerSetter {

    private DataTransformerSetter() {
        super();
    }

    public static <T> RabbitMqBasicGetSupplier<T> setDataTransformer(DataTransformer d,
                                                                     RabbitMqBasicGetSupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T> RabbitMqBasicGetArrayItemSupplier<T> setDataTransformer(DataTransformer d,
                                                                              RabbitMqBasicGetArrayItemSupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T> RabbitMqBasicGetArraySupplier<T> setDataTransformer(DataTransformer d,
                                                                          RabbitMqBasicGetArraySupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T> RabbitMqBasicGetIterableItemSupplier<T> setDataTransformer(DataTransformer d,
                                                                                 RabbitMqBasicGetIterableItemSupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T, S extends Iterable<T>> RabbitMqBasicGetIterableSupplier<T, S> setDataTransformer(
            DataTransformer d,
            RabbitMqBasicGetIterableSupplier<T, S> addTo) {
        return addTo.setDataTransformer(d);
    }
}
