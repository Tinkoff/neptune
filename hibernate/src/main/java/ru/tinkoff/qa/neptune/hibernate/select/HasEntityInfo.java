package ru.tinkoff.qa.neptune.hibernate.select;

@SuppressWarnings("unchecked")
public interface HasEntityInfo<R> {

    default Class<?> getEntity() {
        if (this instanceof SelectOneStepSupplier) {
            return ((SelectOneStepSupplier<R>) this).getEntity();
        }

        if (this instanceof SelectManyStepSupplier) {
            return ((SelectManyStepSupplier<R>) this).getEntity();
        }

        return null;
    }
}
