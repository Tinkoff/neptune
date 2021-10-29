package ru.tinkoff.qa.neptune.spring.data.select.querydsl;

import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.spring.data.dictionary.PredicateParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.select.HasRepositoryInfo;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SetsDescription;
import ru.tinkoff.qa.neptune.spring.data.select.querydsl.by.ByPredicateFunction;

import java.util.List;

public final class QueryDSLSelectStepFactory {

    private QueryDSLSelectStepFactory() {
        super();
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectOneStepSupplier<R, ID, T>
    byPredicate(T repository, Predicate predicate) {
        return new QueryDSLSelectOneStepSupplierImpl<>(repository,
                new ByPredicateFunction.SelectOneByPredicate<>(predicate));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate) {
        return new QueryDSLSelectManyStepSupplierImpl<>(repository,
                new ByPredicateFunction.SelectManyByPredicate<>(predicate));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, Sort sort) {
        return new QueryDSLSelectManyStepSupplierImpl<>(repository,
                new ByPredicateFunction.SelectManyByPredicateAndSorting<>(predicate, sort));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, String... properties) {
        return allByPredicate(repository, predicate, Sort.by(properties));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, List<Sort.Order> orders) {
        return allByPredicate(repository, predicate, Sort.by(orders));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, Sort.Order... orders) {
        return allByPredicate(repository, predicate, Sort.by(orders));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, Sort.Direction direction,
                   String... properties) {
        return allByPredicate(repository, predicate, Sort.by(direction, properties));
    }

    private static final class QueryDSLSelectOneStepSupplierImpl<R, ID, T extends QuerydslPredicateExecutor<R> & Repository<R, ID>> extends
            SelectOneStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        private QueryDSLSelectOneStepSupplierImpl(T repository, ByPredicateFunction<R, T, R> f) {
            super(repository, f);
            predicate = f.getPredicate();
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

    private static final class QueryDSLSelectManyStepSupplierImpl<R, ID, T extends QuerydslPredicateExecutor<R> & Repository<R, ID>>
            extends SelectManyStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        @StepParameter(value = "sort", doNotReportNullValues = true)
        final Sort sort;

        private QueryDSLSelectManyStepSupplierImpl(T repository, ByPredicateFunction<R, T, Iterable<R>> select) {
            super(repository, select);
            predicate = select.getPredicate();
            if (select instanceof ByPredicateFunction.SelectManyByPredicateAndSorting) {
                sort = ((ByPredicateFunction.SelectManyByPredicateAndSorting<R, T>) select).getSort();
            } else {
                sort = null;
            }
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
}
