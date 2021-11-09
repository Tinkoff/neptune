package ru.tinkoff.qa.neptune.spring.data.select.common;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.spring.data.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByExample;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;

public abstract class SelectOneByExampleStepSupplier<R, ID, T extends Repository<R, ID>>
        extends SelectOneStepSupplier<R, ID, T> {

    private final ProbePojo<R> probePojo;
    private final SelectionByExample.SelectASingleByExample<R, ID, T> select;

    protected SelectOneByExampleStepSupplier(R probe, T repository, SelectionByExample.SelectASingleByExample<R, ID, T> select) {
        super(repository, select);
        checkNotNull(probe);
        this.probePojo = new ProbePojo<>(probe);
        this.select = select;
    }

    public SelectOneByExampleStepSupplier<R, ID, T> initialMatcher(ExampleMatcher exampleMatcher) {
        probePojo.initialMatcher(exampleMatcher);
        return this;
    }

    public SelectOneByExampleStepSupplier<R, ID, T> matcher(Function<ExampleMatcher, ExampleMatcher> exampleMatcher) {
        probePojo.addMatcher(exampleMatcher);
        return this;
    }

    @Override
    protected void onStart(T t) {
        select.setMatcher(probePojo.getExampleMatcher());
        select.setProbe(probePojo.getProbe());
    }
}
