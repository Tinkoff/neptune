package ru.tinkoff.qa.neptune.hibernate.select.common;

import ru.tinkoff.qa.neptune.hibernate.select.HasEntityInfo;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SetsDescription;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectAll;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionAsPage;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByMethod;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByOrder;

import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import java.io.Serializable;
import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByCriteria.getIterableByCriteria;
import static ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByCriteria.getSingleByCriteria;
import static ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByIds.getIterableByIds;
import static ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByIds.getSingleById;
import static ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByQuery.getIterableByQuery;
import static ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionByQuery.getSingleByQuery;

@SuppressWarnings("unchecked")
public final class CommonSelectStepFactory {

    private CommonSelectStepFactory() {
        super();
    }

    public static <R, ID extends Serializable> SelectOneStepSupplier<R> byId(Class<?> entity, ID id) {
        return new CommonSelectOneStepSupplierImpl<>(entity, getSingleById(id));
    }

    public static <R, ID extends Serializable> SelectManyStepSupplier<R> byIds(Class<?> entity, ID... ids) {
        return new CommonSelectManyStepSupplierImpl<>(entity, getIterableByIds(ids));
    }

    public static <R> SelectManyStepSupplier<R> all(Class<?> entity) {
        return new CommonSelectManyStepSupplierImpl<>(entity, new SelectAll<>());
    }

    public static <R> SelectManyStepSupplier<R> allByOrder(Class<?> entity,
                                                           List<Order> orders) {
        return new CommonSelectManyStepSupplierImpl<>(entity, new SelectionByOrder<>(orders));
    }

    public static <R> SelectManyStepSupplier<R> allByOrder(Class<?> entity,
                                                           Order... orders) {
        return allByOrder(entity, List.of(orders));
    }

    public static <R> SelectAsPageStepSupplier<R> asAPage(Class<?> entity) {
        return new CommonSelectAsPageStepSupplier<>(entity, new SelectionAsPage<>());
    }

    public static <R> SelectOneStepSupplier<R> byInvocation(Class<?> entity, Function<Class<?>, R> f) {
        return new CommonSelectOneStepSupplierImpl<>(entity, new SelectionByMethod<>(f));
    }

    public static <R, S extends Iterable<R>> SelectManyStepSupplier<R> allByInvocation(Class<?> entity, Function<Class<?>, S> f) {
        return new CommonSelectManyStepSupplierImpl<>(entity, new SelectionByMethod<>((Function<Class<?>, Iterable<R>>) f));
    }

    public static <R> SelectOneStepSupplier<R> byCriteria(Class<?> entity, CriteriaQuery<R> criteriaQuery) {
        return new CommonSelectOneStepSupplierImpl<>(entity, getSingleByCriteria(criteriaQuery));
    }

    public static <R> SelectManyStepSupplier<R> allByCriteria(Class<?> entity, CriteriaQuery<R> criteriaQuery) {
        return new CommonSelectManyStepSupplierImpl<>(entity, getIterableByCriteria(criteriaQuery));
    }

    public static <R> SelectOneStepSupplier<R> byQuery(Class<?> entity, String query, Object... parameters) {
        return new CommonSelectOneStepSupplierImpl<>(entity, getSingleByQuery(query, parameters));
    }

    public static <R> SelectManyStepSupplier<R> allByQuery(Class<?> entity, String query, Object... parameters) {
        return new CommonSelectManyStepSupplierImpl<>(entity, getIterableByQuery(query, parameters));
    }

    private static final class CommonSelectOneStepSupplierImpl<R> extends
            SelectOneStepSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {

        private CommonSelectOneStepSupplierImpl(Class<?> entity, Function<Class<?>, R> select) {
            super(entity, select);
        }

        @Override
        public Class<?> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectManyStepSupplierImpl<R>
            extends SelectManyStepSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {

        private CommonSelectManyStepSupplierImpl(Class<?> entity, Function<Class<?>, Iterable<R>> select) {
            super(entity, select);
        }

        @Override
        public Class<?> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectAsPageStepSupplier<R>
            extends SelectAsPageStepSupplier<R>
            implements SetsDescription, HasEntityInfo<R> {

        private CommonSelectAsPageStepSupplier(Class<?> entity, SelectionAsPage<R> select) {
            super(entity, select);
        }

        @Override
        public Class<?> getEntity() {
            return HasEntityInfo.super.getEntity();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }
}
