package ru.tinkoff.qa.neptune.spring.data.select.querydsl.by;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

public abstract class ByPredicateFunction<R, T extends QuerydslPredicateExecutor<R>, RESULT> extends SpringDataFunction<T, RESULT> {

    final Predicate predicate;

    public ByPredicateFunction(Predicate predicate) {
        super(QuerydslPredicateExecutor.class);
        checkNotNull(predicate);
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Description("By predicate")
    public static final class SelectOneByPredicate<R, T extends QuerydslPredicateExecutor<R>> extends ByPredicateFunction<R, T, R> {

        public SelectOneByPredicate(Predicate predicate) {
            super(predicate);
        }

        @Override
        public R apply(T t) {
            return t.findOne(predicate).orElse(null);
        }
    }

    @Description("By predicate")
    public static final class SelectManyByPredicate<R, T extends QuerydslPredicateExecutor<R>> extends ByPredicateFunction<R, T, Iterable<R>> {

        public SelectManyByPredicate(Predicate predicate) {
            super(predicate);
        }

        @Override
        public Iterable<R> apply(T t) {
            return newArrayList(t.findAll(predicate));
        }
    }

    @Description("By predicate and sorting")
    public static final class SelectManyByPredicateAndSorting<R, T extends QuerydslPredicateExecutor<R>> extends ByPredicateFunction<R, T, Iterable<R>> {

        private final Sort sort;

        public SelectManyByPredicateAndSorting(Predicate predicate, Sort sort) {
            super(predicate);
            checkNotNull(sort);
            this.sort = sort;
        }

        public Sort getSort() {
            return sort;
        }

        @Override
        public Iterable<R> apply(T t) {
            return newArrayList(t.findAll(predicate, sort));
        }
    }
}
