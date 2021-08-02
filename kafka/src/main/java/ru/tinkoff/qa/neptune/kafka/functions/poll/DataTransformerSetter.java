package ru.tinkoff.qa.neptune.kafka.functions.poll;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;

public final class DataTransformerSetter {

    private DataTransformerSetter() {
        super();
    }

    public static <T> KafkaPollIterableSupplier<T> setDataTransformer(DataTransformer d,
                                                                      KafkaPollIterableSupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T> KafkaPollIterableItemSupplier<T> setDataTransformer(DataTransformer d,
                                                                          KafkaPollIterableItemSupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T> KafkaPollArraySupplier<T> setDataTransformer(DataTransformer d,
                                                                   KafkaPollArraySupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }

    public static <T> KafkaPollArrayItemSupplier<T> setDataTransformer(DataTransformer d,
                                                                       KafkaPollArrayItemSupplier<T> addTo) {
        return addTo.setDataTransformer(d);
    }
}
