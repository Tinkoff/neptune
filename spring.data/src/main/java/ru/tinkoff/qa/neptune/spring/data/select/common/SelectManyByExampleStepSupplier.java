package ru.tinkoff.qa.neptune.spring.data.select.common;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByExample;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class SelectManyByExampleStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SelectManyStepSupplier<R, ID, T> {

    private final ProbePojo<R> probePojo;
    private final SelectionByExample.SelectIterableByExample<R, ID, T> select;
    @StepParameter(value = "sort", doNotReportNullValues = true)
    private Sort sort;

    protected SelectManyByExampleStepSupplier(R probe, T repository, SelectionByExample.SelectIterableByExample<R, ID, T> select) {
        super(repository, select);
        checkNotNull(probe);
        this.probePojo = new ProbePojo<>(probe);
        this.select = select;
    }

    public SelectManyByExampleStepSupplier<R, ID, T> initialMatcher(ExampleMatcher exampleMatcher) {
        probePojo.initialMatcher(exampleMatcher);
        return this;
    }

    public SelectManyByExampleStepSupplier<R, ID, T> matcher(Function<ExampleMatcher, ExampleMatcher> exampleMatcher) {
        probePojo.addMatcher(exampleMatcher);
        return this;
    }

    @Override
    protected void onStart(T t) {
        select.setMatcher(probePojo.getExampleMatcher());
        select.setProbe(probePojo.getProbe());
        select.setSort(sort);
    }

    public SelectManyByExampleStepSupplier<R, ID, T> sorting(Sort sort) {
        this.sort = sort;
        return this;
    }

    public SelectManyByExampleStepSupplier<R, ID, T> sorting(String... properties) {
        return sorting(Sort.by(properties));
    }

    public SelectManyByExampleStepSupplier<R, ID, T> sorting(List<Sort.Order> orders) {
        return sorting(Sort.by(orders));
    }

    public SelectManyByExampleStepSupplier<R, ID, T> sorting(Sort.Order... orders) {
        return sorting(Sort.by(orders));
    }

    public SelectManyByExampleStepSupplier<R, ID, T> sorting(Sort.Direction direction,
                                                             String... properties) {
        return sorting(Sort.by(direction, properties));
    }
}
