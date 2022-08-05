package ru.tinkoff.qa.neptune.hibernate.select.querydsl;

import com.querydsl.core.types.*;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.PredicateParameterValueGetter;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SetsDescription;
import ru.tinkoff.qa.neptune.hibernate.select.querydsl.by.SelectByOrderedFunction;
import ru.tinkoff.qa.neptune.hibernate.select.querydsl.by.SelectByPredicateFunction;

public final class QueryDSLSelectStepFactory {

    private QueryDSLSelectStepFactory() {
        super();
    }

    public static <R> SelectOneStepSupplier<R> byPredicate(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
        return new QueryDSLSelectOneStepSupplierImpl<>(new SelectByPredicateFunction.SelectOneByPredicate<>(entity, entityPath, predicate));
    }

    public static <R> SelectManyStepSupplier<R> allByPredicate(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
        return new QueryDSLSelectManyStepSupplierImpl<>(new SelectByPredicateFunction.SelectManyByPredicate<>(entity, entityPath, predicate));
    }

    public static <R> SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
    allOrdered(Class<R> entity, EntityPath<?> entityPath, OrderSpecifier<?> orderSpecifier) {
        return new SelectManyByPredicateAndOrderSpecifiersSupplierImpl<>(entity, entityPath).orderSpecifier(orderSpecifier);
    }

    public static <C extends Comparable<?>, R>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
    allOrdered(Class<R> entity, EntityPath<?> entityPath, Order order, Expression<C> target, OrderSpecifier.NullHandling handling) {
        return allOrdered(entity, entityPath, new OrderSpecifier<>(order, target, handling));
    }

    public static <C extends Comparable<?>, R>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
    allOrdered(Class<R> entity, EntityPath<?> entityPath, Order order, Expression<C> target) {
        return allOrdered(entity, entityPath, new OrderSpecifier<>(order, target));
    }

    public static <R>
    SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndPageableSupplier<R>
    asAPageByPredicate(Class<R> entity, EntityPath<?> entityPath, Predicate predicate) {
        return new SelectManyByPredicateAndPageableSupplierImpl<>(
                new SelectByPredicateFunction.SelectManyByPredicateAndPagination<>(entity, entityPath, predicate));
    }

    private static final class QueryDSLSelectOneStepSupplierImpl<R> extends
            SelectOneStepSupplier<R>
            implements SetsDescription {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        private QueryDSLSelectOneStepSupplierImpl(SelectByPredicateFunction<R, R> f) {
            super(f);
            predicate = f.getPredicate();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class QueryDSLSelectManyStepSupplierImpl<R>
            extends SelectManyStepSupplier<R>
            implements SetsDescription {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class)
        final Predicate predicate;

        private QueryDSLSelectManyStepSupplierImpl(SelectByPredicateFunction<R, Iterable<R>> select) {
            super(select);
            predicate = select.getPredicate();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static class SelectManyByPredicateAndOrderSpecifiersSupplierImpl<R>
            extends SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndOrderSpecifiersSupplier<R>
            implements SetsDescription {


        protected SelectManyByPredicateAndOrderSpecifiersSupplierImpl(Class<R> entity, EntityPath<?> entityPath) {
            super(new SelectByOrderedFunction<>(entity, entityPath));
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static class SelectManyByPredicateAndPageableSupplierImpl<R>
            extends SelectManyByOrderSpecifiersSupplier.SelectManyByPredicateAndPageableSupplier<R>
            implements SetsDescription {


        protected SelectManyByPredicateAndPageableSupplierImpl(SelectByPredicateFunction.SelectManyByPredicateAndPagination<R> f) {
            super(f);
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }
}
