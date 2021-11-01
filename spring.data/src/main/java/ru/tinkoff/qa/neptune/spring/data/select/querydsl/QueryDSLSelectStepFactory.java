package ru.tinkoff.qa.neptune.spring.data.select.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.domain.Sort;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.querydsl.ReactiveQuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.spring.data.dictionary.PredicateParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.select.HasRepositoryInfo;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SetsDescription;
import ru.tinkoff.qa.neptune.spring.data.select.querydsl.by.SelectByOrderedFunction;
import ru.tinkoff.qa.neptune.spring.data.select.querydsl.by.SelectByPredicateFunction;

import java.util.List;

public final class QueryDSLSelectStepFactory {

    private QueryDSLSelectStepFactory() {
        super();
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectOneStepSupplier<R, ID, T>
    byPredicate(T repository, Predicate predicate) {
        return new QueryDSLSelectOneStepSupplierImpl<>(repository,
                new SelectByPredicateFunction.SelectOneByPredicate<>(predicate));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate) {
        return new QueryDSLSelectManyStepSupplierImpl<>(repository,
                new SelectByPredicateFunction.SelectManyByPredicate<>(predicate));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectOneStepSupplier<R, ID, T>
    byPredicate(T repository, Predicate predicate) {
        return new QueryDSLSelectOneStepSupplierImpl<>(repository,
                new SelectByPredicateFunction.SelectOneByPredicate<>(predicate));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate) {
        return new QueryDSLSelectManyStepSupplierImpl<>(repository,
                new SelectByPredicateFunction.SelectManyByPredicate<>(predicate));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, Sort sort) {
        return new QueryDSLSelectManyStepSupplierImpl<>(repository,
                new SelectByPredicateFunction.SelectManyByPredicateAndSorting<>(predicate, sort));
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
    allByPredicate(T repository, Predicate predicate,
                   Sort.Direction direction,
                   String... properties) {
        return allByPredicate(repository, predicate, Sort.by(direction, properties));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, Sort sort) {
        return new QueryDSLSelectManyStepSupplierImpl<>(repository,
                new SelectByPredicateFunction.SelectManyByPredicateAndSorting<>(predicate, sort));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, String... properties) {
        return allByPredicate(repository, predicate, Sort.by(properties));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, List<Sort.Order> orders) {
        return allByPredicate(repository, predicate, Sort.by(orders));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate, Sort.Order... orders) {
        return allByPredicate(repository, predicate, Sort.by(orders));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T>
    allByPredicate(T repository, Predicate predicate,
                   Sort.Direction direction,
                   String... properties) {
        return allByPredicate(repository, predicate, Sort.by(direction, properties));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
    allOrdered(T repository, OrderSpecifier<?> orderSpecifier) {
        return new SelectManyByPredicateAndOrderSpecifiersSupplierImpl<>(repository).orderSpecifier(orderSpecifier);
    }

    public static <C extends Comparable<?>, R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
    allOrdered(T repository, Order order, Expression<C> target, OrderSpecifier.NullHandling handling) {
        return allOrdered(repository, new OrderSpecifier<>(order, target, handling));
    }

    public static <C extends Comparable<?>, R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
    allOrdered(T repository, Order order, Expression<C> target) {
        return allOrdered(repository, new OrderSpecifier<>(order, target));
    }

    public static <R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
    allOrdered(T repository, OrderSpecifier<?> orderSpecifier) {
        return new SelectManyByPredicateAndOrderSpecifiersSupplierImpl<>(repository).orderSpecifier(orderSpecifier);
    }

    public static <C extends Comparable<?>, R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
    allOrdered(T repository, Order order, Expression<C> target, OrderSpecifier.NullHandling handling) {
        return allOrdered(repository, new OrderSpecifier<>(order, target, handling));
    }

    public static <C extends Comparable<?>, R, ID, T extends ReactiveQuerydslPredicateExecutor<R> & Repository<R, ID>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
    allOrdered(T repository, Order order, Expression<C> target) {
        return allOrdered(repository, new OrderSpecifier<>(order, target));
    }

    public static <R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndPageableSupplier<R, ID, T>
    asAPageByPredicate(T repository, Predicate predicate) {
        return new SelectManyByPredicateAndPageableSupplierImpl<>(repository, new SelectByPredicateFunction.SelectManyByPredicateAndPageable<>(predicate));
    }

    private static final class QueryDSLSelectOneStepSupplierImpl<R, ID, T extends Repository<R, ID>> extends
            SelectOneStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        private QueryDSLSelectOneStepSupplierImpl(T repository, SelectByPredicateFunction<R, ID, T, R> f) {
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

    private static final class QueryDSLSelectManyStepSupplierImpl<R, ID, T extends Repository<R, ID>>
            extends SelectManyStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        @StepParameter(value = "sort", doNotReportNullValues = true)
        final Sort sort;

        private QueryDSLSelectManyStepSupplierImpl(T repository, SelectByPredicateFunction<R, ID, T, Iterable<R>> select) {
            super(repository, select);
            predicate = select.getPredicate();
            if (select instanceof SelectByPredicateFunction.SelectManyByPredicateAndSorting) {
                sort = ((SelectByPredicateFunction.SelectManyByPredicateAndSorting<R, ID, T>) select).getSort();
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

    private static class SelectManyByPredicateAndOrderSpecifiersSupplierImpl<R, ID, T extends Repository<R, ID>>
            extends SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {


        protected SelectManyByPredicateAndOrderSpecifiersSupplierImpl(T repository) {
            super(repository, new SelectByOrderedFunction<>());
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

    private static class SelectManyByPredicateAndPageableSupplierImpl<R, ID, T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
            extends SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndPageableSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {


        protected SelectManyByPredicateAndPageableSupplierImpl(T repository, SelectByPredicateFunction.SelectManyByPredicateAndPageable<R, ID, T> f) {
            super(repository, f);
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
