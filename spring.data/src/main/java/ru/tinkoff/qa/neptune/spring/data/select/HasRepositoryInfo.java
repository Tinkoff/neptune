package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;

@SuppressWarnings("unchecked")
public interface HasRepositoryInfo<R, ID, T extends Repository<R, ID>> {

    default T getRepository() {
        if (this instanceof SelectOneStepSupplier) {
            return ((SelectOneStepSupplier<R, ID, T>) this).getRepository();
        }

        if (this instanceof SelectManyStepSupplier) {
            return ((SelectManyStepSupplier<R, ID, T>) this).getRepository();
        }

        return null;
    }
}
