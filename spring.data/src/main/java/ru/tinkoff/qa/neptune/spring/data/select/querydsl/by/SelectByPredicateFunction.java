package ru.tinkoff.qa.neptune.spring.data.select.querydsl.by;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;

public abstract class SelectByPredicateFunction<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>, RESULT> extends SpringDataFunction<T, RESULT> {

    final Predicate predicate;

    public SelectByPredicateFunction(Predicate predicate) {
        super(QuerydslPredicateExecutor.class);
        checkNotNull(predicate);
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    @Description("By predicate")
    public static final class SelectOneByPredicate<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> extends SelectByPredicateFunction<R, ID, T, R> {

        public SelectOneByPredicate(Predicate predicate) {
            super(predicate);
        }

        @Override
        public R apply(T t) {
            return t.findOne(predicate).orElse(null);
        }
    }

    @Description("By predicate")
    public static final class SelectManyByPredicate<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> extends SelectByPredicateFunction<R, ID, T, Iterable<R>> {

        public SelectManyByPredicate(Predicate predicate) {
            super(predicate);
        }

        @Override
        public Iterable<R> apply(T t) {
            return newArrayList(t.findAll(predicate));
        }
    }

    @Description("By predicate and sorting")
    public static final class SelectManyByPredicateAndSorting<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> extends SelectByPredicateFunction<R, ID, T, Iterable<R>> {

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

    @Description("By predicate as page")
    public static final class SelectManyByPredicateAndPageable<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> extends SelectByPredicateFunction<R, ID, T, Iterable<R>> {

        private Pageable pageable;

        public SelectManyByPredicateAndPageable(Predicate predicate) {
            super(predicate);
        }

        @Override
        public Iterable<R> apply(T t) {
            return newArrayList(t.findAll(predicate, pageable));
        }


        public void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }
    }
}
