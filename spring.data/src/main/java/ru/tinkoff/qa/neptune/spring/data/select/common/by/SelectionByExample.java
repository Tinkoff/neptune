package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static java.util.Objects.isNull;

@SuppressWarnings("unchecked")
@Description("By example")
public abstract class SelectionByExample<R, ID, T extends Repository<R, ID>, RESULT> extends SpringDataFunction<T, RESULT> {

    private R probe;

    private ExampleMatcher matcher;

    private SelectionByExample() {
        super(QueryByExampleExecutor.class, ReactiveQueryByExampleExecutor.class);
    }

    public static <R, ID, T extends Repository<R, ID>> SelectASingleByExample<R, ID, T> getSingleByExample() {
        return new SelectASingleByExample<>();
    }

    public static <R, ID, T extends Repository<R, ID>> SelectIterableByExample<R, ID, T> getIterableByExample() {
        return new SelectIterableByExample<>();
    }

    protected final Example<R> getExample() {
        return Example.of(probe, matcher);
    }

    public void setProbe(R probe) {
        this.probe = probe;
    }

    public void setMatcher(ExampleMatcher matcher) {
        this.matcher = matcher;
    }

    public static final class SelectASingleByExample<R, ID, T
            extends Repository<R, ID>> extends SelectionByExample<R, ID, T, R> {

        @Override
        public R apply(T t) {
            if (t instanceof QueryByExampleExecutor) {
                return ((QueryByExampleExecutor<R>) t).findOne(getExample()).orElse(null);
            }

            if (t instanceof ReactiveQueryByExampleExecutor) {
                return ((ReactiveQueryByExampleExecutor<R>) t).findOne(getExample()).block();
            }

            throw unsupportedRepository(t);
        }
    }

    public static final class SelectIterableByExample<R, ID, T
            extends Repository<R, ID>> extends SelectionByExample<R, ID, T, Iterable<R>> {

        private Sort sort;

        @Override
        public Iterable<R> apply(T t) {
            if (t instanceof QueryByExampleExecutor) {
                var repo = (QueryByExampleExecutor<R>) t;
                return isNull(sort) ? repo.findAll(getExample()) : repo.findAll(getExample(), sort);
            }

            if (t instanceof ReactiveQueryByExampleExecutor) {
                var repo = (ReactiveQueryByExampleExecutor<R>) t;
                return isNull(sort) ? repo.findAll(getExample()).collectList().block()
                        : repo.findAll(getExample(), sort).collectList().block();
            }

            throw unsupportedRepository(t);
        }

        public void setSort(Sort sort) {
            this.sort = sort;
        }
    }
}
