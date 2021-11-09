package ru.tinkoff.qa.neptune.spring.data.select;

public interface SetsDescription {

    default void changeDescription(String description) {
        if (this instanceof SelectOneStepSupplier) {
            ((SelectOneStepSupplier<?, ?, ?>) this).setDescription(description);
        }

        if (this instanceof SelectManyStepSupplier) {
            ((SelectManyStepSupplier<?, ?, ?>) this).setDescription(description);
        }
    }
}
