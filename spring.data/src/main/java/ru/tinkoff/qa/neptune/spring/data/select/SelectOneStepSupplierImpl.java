package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.repository.Repository;

import java.util.function.Function;

final class SelectOneStepSupplierImpl<R, ID, T extends Repository<R, ID>> extends
        SelectOneStepSupplier<R, ID, T>
        implements SetsDescription, HasRepositoryInfo<R, ID, T> {

    SelectOneStepSupplierImpl(T repository, Function<T, R> select) {
        super(repository, select);
    }

    @Override
    public T getRepository() {
        return HasRepositoryInfo.super.getRepository();
    }

    @Override
    public void changeDescription(String description) {
        SetsDescription.super.changeDescription(description);
    }
}
