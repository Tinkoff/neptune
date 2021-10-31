package ru.tinkoff.qa.neptune.spring.data.select.querydsl;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import org.springframework.data.querydsl.QPageRequest;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;
import ru.tinkoff.qa.neptune.spring.data.dictionary.OrderSpec;
import ru.tinkoff.qa.neptune.spring.data.dictionary.PredicateParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.querydsl.by.SelectByOrderedFunction;
import ru.tinkoff.qa.neptune.spring.data.select.querydsl.by.SelectByPredicateFunction;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ArrayUtils.add;
import static ru.tinkoff.qa.neptune.spring.data.data.serializer.DataSerializer.serializeObject;

@SuppressWarnings("unchecked")
public abstract class SelectManyByOrderSpecifiersSupplier<R, ID,
        T extends Repository<R, ID> & QuerydslPredicateExecutor<R>,
        S extends SelectManyByOrderSpecifiersSupplier<R, ID, T, S>>
        extends SelectManyStepSupplier<R, ID, T> {

    final SpringDataFunction<T, Iterable<R>> f;
    OrderSpecifier<?>[] orderSpecifiers = new OrderSpecifier[]{};

    protected SelectManyByOrderSpecifiersSupplier(T repository, SpringDataFunction<T, Iterable<R>> f) {
        super(repository, f);
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

    public static abstract class SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID,
            T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
            extends SelectManyByOrderSpecifiersSupplier<R, ID, T, SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T>> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class, doNotReportNullValues = true)
        private Predicate predicate;

        protected SelectManyByPredicateAndOrderSpecifiersSupplier(T repository, SelectByOrderedFunction<R, T> f) {
            super(repository, f);
        }

        public SelectManyByPredicateAndOrderSpecifiersSupplier<R, ID, T> predicate(Predicate predicate) {
            this.predicate = predicate;
            return this;
        }

        @Override
        protected void onStart(T t) {
            ((SelectByOrderedFunction<R, T>) f).setPredicate(predicate);
            ((SelectByOrderedFunction<R, T>) f).setOrderSpecifiers(orderSpecifiers);
        }
    }

    public static abstract class SelectManyByPredicateAndPageableSupplier<R, ID,
            T extends Repository<R, ID> & QuerydslPredicateExecutor<R>>
            extends SelectManyByOrderSpecifiersSupplier<R, ID, T, SelectManyByPredicateAndPageableSupplier<R, ID, T>> {

        @StepParameter(value = "Predicate", makeReadableBy = PredicateParameterValueGetter.class, doNotReportNullValues = true)
        final Predicate predicate;

        @StepParameter("number")
        int number;
        @StepParameter("size")
        int size = 1;

        protected SelectManyByPredicateAndPageableSupplier(T repository, SelectByPredicateFunction.SelectManyByPredicateAndPageable<R, ID, T> f) {
            super(repository, f);
            this.predicate = f.getPredicate();
        }

        public SelectManyByPredicateAndPageableSupplier<R, ID, T> number(int number) {
            this.number = number;
            return this;
        }

        public SelectManyByPredicateAndPageableSupplier<R, ID, T> size(int size) {
            this.size = size;
            return this;
        }

        @Override
        protected void onStart(T t) {
            if (orderSpecifiers.length == 0) {
                ((SelectByPredicateFunction.SelectManyByPredicateAndPageable<R, ID, T>) f)
                        .setPageable(QPageRequest.of(number, size));
            } else {
                ((SelectByPredicateFunction.SelectManyByPredicateAndPageable<R, ID, T>) f)
                        .setPageable(QPageRequest.of(number, size, orderSpecifiers));
            }
        }
    }
}
