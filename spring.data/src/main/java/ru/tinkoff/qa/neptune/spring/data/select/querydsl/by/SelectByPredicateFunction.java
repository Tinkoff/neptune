package ru.tinkoff.qa.neptune.spring.data.select.querydsl.by;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkNotNull;

@SuppressWarnings("unchecked")
@Description("By predicate")
public abstract class SelectByPredicateFunction<R, ID, T extends Repository<R, ID>, RESULT>
    extends SpringDataFunction<T, RESULT> {

    final Predicate predicate;

    public SelectByPredicateFunction(Predicate predicate, Class<?>... classes) {
        super(classes);
        checkNotNull(predicate);
        this.predicate = predicate;
    }

    public Predicate getPredicate() {
        return predicate;
    }

    public static final class SelectOneByPredicate<R, ID, T extends Repository<R, ID>> extends SelectByPredicateFunction<R, ID, T, R> {

        public SelectOneByPredicate(Predicate predicate) {
            super(predicate, QuerydslPredicateExecutor.class, ReactiveQuerydslPredicateExecutor.class);
        }

        @Override
        public R apply(T t) {
            if (t instanceof QuerydslPredicateExecutor) {
                return ((QuerydslPredicateExecutor<R>) t).findOne(predicate).orElse(null);
            }

            if (t instanceof ReactiveQuerydslPredicateExecutor) {
                return ((ReactiveQuerydslPredicateExecutor<R>) t).findOne(predicate).block();
            }

            throw unsupportedRepository(t);
        }
    }

    public static final class SelectManyByPredicate<R, ID, T extends Repository<R, ID>> extends SelectByPredicateFunction<R, ID, T, Iterable<R>> {

        public SelectManyByPredicate(Predicate predicate) {
            super(predicate, QuerydslPredicateExecutor.class, ReactiveQuerydslPredicateExecutor.class);
        }

        @Override
        public Iterable<R> apply(T t) {
            if (t instanceof QuerydslPredicateExecutor) {
                return ((QuerydslPredicateExecutor<R>) t).findAll(predicate);
            }

            if (t instanceof ReactiveQuerydslPredicateExecutor) {
                return ((ReactiveQuerydslPredicateExecutor<R>) t).findAll(predicate).collectList().block();
            }

            throw unsupportedRepository(t);
        }
    }

    @Description("By predicate and sorting")
    public static final class SelectManyByPredicateAndSorting<R, ID, T extends Repository<R, ID>> extends SelectByPredicateFunction<R, ID, T, Iterable<R>> {

        private final Sort sort;

        public SelectManyByPredicateAndSorting(Predicate predicate, Sort sort) {
            super(predicate, QuerydslPredicateExecutor.class, ReactiveQuerydslPredicateExecutor.class);
            checkNotNull(sort);
            this.sort = sort;
        }

        public Sort getSort() {
            return sort;
        }

        @Override
        public Iterable<R> apply(T t) {
            if (t instanceof QuerydslPredicateExecutor) {
                return ((QuerydslPredicateExecutor<R>) t).findAll(predicate, sort);
            }

            if (t instanceof ReactiveQuerydslPredicateExecutor) {
                return ((ReactiveQuerydslPredicateExecutor<R>) t).findAll(predicate, sort).collectList().block();
            }

            throw unsupportedRepository(t);
        }
    }

    @Description("By predicate as page")
    public static final class SelectManyByPredicateAndPageable<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> extends SelectByPredicateFunction<R, ID, T, Iterable<R>> {

        private Pageable pageable;

        public SelectManyByPredicateAndPageable(Predicate predicate) {
            super(predicate, QuerydslPredicateExecutor.class);
        }

        @Override
        public Iterable<R> apply(T t) {
            return t.findAll(predicate, pageable).getContent();
        }


        public void setPageable(Pageable pageable) {
            this.pageable = pageable;
        }
    }
}
