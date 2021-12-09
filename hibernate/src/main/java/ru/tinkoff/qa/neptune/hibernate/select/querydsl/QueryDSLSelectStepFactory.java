package ru.tinkoff.qa.neptune.hibernate.select.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.PredicateParameterValueGetter;
import ru.tinkoff.qa.neptune.hibernate.select.HasEntityInfo;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SetsDescription;
import ru.tinkoff.qa.neptune.hibernate.select.querydsl.by.SelectByOrderedFunction;
import ru.tinkoff.qa.neptune.hibernate.select.querydsl.by.SelectByPredicateFunction;

public final class QueryDSLSelectStepFactory {

    private QueryDSLSelectStepFactory() {
        super();
    }

    public static <R> SelectOneStepSupplier<R> byPredicate(Class<R> entity, Predicate predicate) {
        return new QueryDSLSelectOneStepSupplierImpl<>(entity,
                new SelectByPredicateFunction.SelectOneByPredicate<>(predicate));
    }

    public static <R> SelectManyStepSupplier<R> allByPredicate(Class<R> entity, Predicate predicate) {
        return new QueryDSLSelectManyStepSupplierImpl<>(entity,
                new SelectByPredicateFunction.SelectManyByPredicate<>(predicate));
    }

    public static <R> SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
    allOrdered(Class<R> entity, OrderSpecifier<?> orderSpecifier) {
        return new SelectManyByPredicateAndOrderSpecifiersSupplierImpl<R>(entity).orderSpecifier(orderSpecifier);
    }

    public static <C extends Comparable<?>, R>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
    allOrdered(Class<R> entity, Order order, Expression<C> target, OrderSpecifier.NullHandling handling) {
        return allOrdered(entity, new OrderSpecifier<>(order, target, handling));
    }

    public static <C extends Comparable<?>, R>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
    allOrdered(Class<R> entity, Order order, Expression<C> target) {
        return allOrdered(entity, new OrderSpecifier<>(order, target));
    }

    public static <R>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndPageableSupplier<R>
    asAPageByPredicate(Class<R> entity, Predicate predicate) {
        return new SelectManyByPredicateAndPageableSupplierImpl<>(entity,
                new SelectByPredicateFunction.SelectManyByPredicateAndPagination<>(predicate));
    }

    private static final class QueryDSLSelectOneStepSupplierImpl<R> extends
            SelectOneStepSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        private QueryDSLSelectOneStepSupplierImpl(Class<R> entity, SelectByPredicateFunction<R, R> f) {
            super(entity, f);
            predicate = f.getPredicate();
        }

        @Override
        public Class<R> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class QueryDSLSelectManyStepSupplierImpl<R>
            extends SelectManyStepSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        private QueryDSLSelectManyStepSupplierImpl(Class<R> entity, SelectByPredicateFunction<R, Iterable<R>> select) {
            super(entity, select);
            predicate = select.getPredicate();
        }

        @Override
        public Class<R> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static class SelectManyByPredicateAndOrderSpecifiersSupplierImpl<R>
            extends SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {


        protected SelectManyByPredicateAndOrderSpecifiersSupplierImpl(Class<R> entity) {
            super(entity, new SelectByOrderedFunction<>());
        }

        @Override
        public Class<R> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static class SelectManyByPredicateAndPageableSupplierImpl<R>
            extends SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndPageableSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {


        protected SelectManyByPredicateAndPageableSupplierImpl(Class<R> entity,
                                                               SelectByPredicateFunction.SelectManyByPredicateAndPagination<R> f) {
            super(entity, f);
        }

        @Override
        public Class<R> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }
}
