package ru.tinkoff.qa.neptune.hibernate.select.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.OrderSpec;
import ru.tinkoff.qa.neptune.database.abstractions.dictionary.PredicateParameterValueGetter;
import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.querydsl.by.SelectByOrderedFunction;
import ru.tinkoff.qa.neptune.hibernate.select.querydsl.by.SelectByPredicateFunction;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ArrayUtils.add;
import static ru.tinkoff.qa.neptune.database.abstractions.data.serializer.DataSerializer.serializeObject;

@SuppressWarnings("unchecked")
public abstract class SelectManyByOrderSpecifiersSupplier<R, S extends SelectManyByOrderSpecifiersSupplier<R, S>>
        extends SelectManyStepSupplier<R> {

    final Function<HibernateContext, Iterable<R>> f;
    OrderSpecifier<?>[] orderSpecifiers = new OrderSpecifier[]{};

    protected SelectManyByOrderSpecifiersSupplier(Function<HibernateContext, Iterable<R>> f) {
        super(f);
        this.f = f;
    }

    public S orderSpecifier(OrderSpecifier<?> orderSpecifier) {
        checkNotNull(orderSpecifier);
        this.orderSpecifiers = add(orderSpecifiers, orderSpecifier);
        return (S) this;
    }

    public <C extends Comparable<?>> S
    orderSpecifier(Order order, Expression<C> target, OrderSpecifier.NullHandling handling) {
        return orderSpecifier(new OrderSpecifier<>(order, target, handling));
    }

    public <C extends Comparable<?>> S
    orderSpecifier(Order order, Expression<C> target) {
        return orderSpecifier(new OrderSpecifier<>(order, target));
    }

    @Override
    public Map<String, String> getParameters() {
        var result = new LinkedHashMap<>(super.getParameters());
        int i = 0;
        for (var o : orderSpecifiers) {
            i++;
            result.put(new OrderSpec() + " " + i, serializeObject(NON_NULL, o));
        }
        return result;
    }

    public static abstract class SelectManyByPredicateAndOrderSpecifiersSupplier<R>
            extends SelectManyByOrderSpecifiersSupplier<R, SelectManyByPredicateAndOrderSpecifiersSupplier<R>> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class, doNotReportNullValues = true)
        private Predicate predicate;

        protected SelectManyByPredicateAndOrderSpecifiersSupplier(SelectByOrderedFunction<R> f) {
            super(f);
        }

        public SelectManyByPredicateAndOrderSpecifiersSupplier<R> predicate(Predicate predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        protected void onStart(HibernateContext context) {
            if (predicate != null) {
                ((SelectByOrderedFunction<R>) f).setPredicate(predicate);
            }
            ((SelectByOrderedFunction<R>) f).setOrderSpecifiers(orderSpecifiers);
        }
    }

    public static abstract class SelectManyByPredicateAndPageableSupplier<R>
            extends SelectManyByOrderSpecifiersSupplier<R, SelectManyByPredicateAndPageableSupplier<R>> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class, doNotReportNullValues = true)
        final Predicate predicate;

        @StepParameter("limit")
        int limit = 1;
        @StepParameter("offset")
        int offset;

        protected SelectManyByPredicateAndPageableSupplier(SelectByPredicateFunction.SelectManyByPredicateAndPagination<R> f) {
            super(f);
            this.predicate = f.getPredicate();
        }

        public SelectManyByPredicateAndPageableSupplier<R> limit(int limit) {
            this.limit = limit;
            return this;
        }

        public SelectManyByPredicateAndPageableSupplier<R> offset(int offset) {
            this.offset = offset;
            return this;
        }

        @Override
        protected void onStart(HibernateContext context) {
            ((SelectByPredicateFunction.SelectManyByPredicateAndPagination<R>) f)
                    .setLimitOffset(limit, offset);
        }
    }
}


