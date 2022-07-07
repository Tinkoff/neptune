package ru.tinkoff.qa.neptune.hibernate.select.common;

import ru.tinkoff.qa.neptune.hibernate.HibernateContext;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.SetsDescription;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectAll;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionAsPage;
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

    public static <R, ID extends Serializable> SelectOneStepSupplier<R> byId(Class<R> entity, ID id) {
        return new CommonSelectOneStepSupplierImpl<>(getSingleById(entity, id));
    }

    public static <R, ID extends Serializable> SelectManyStepSupplier<R> byIds(Class<R> entity, ID... ids) {
        return new CommonSelectManyStepSupplierImpl<>(getIterableByIds(entity, ids));
    }

    public static <R> SelectManyStepSupplier<R> all(Class<R> entity) {
        return new CommonSelectManyStepSupplierImpl<>(new SelectAll<>(entity));
    }

    public static <R> SelectManyStepSupplier<R> allByOrder(Class<R> entity,
                                                           List<Order> orders) {
        return new CommonSelectManyStepSupplierImpl<>(new SelectionByOrder<>(entity, orders));
    }

    public static <R> SelectManyStepSupplier<R> allByOrder(Class<R> entity,
                                                           Order... orders) {
        return allByOrder(entity, List.of(orders));
    }

    public static <R> SelectAsPageStepSupplier<R> asAPage(Class<R> entity) {
        return new CommonSelectAsPageStepSupplier<>(new SelectionAsPage<>(entity));
    }

    public static <R> SelectOneStepSupplier<R> byCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
        return new CommonSelectOneStepSupplierImpl<>(getSingleByCriteria(entity, criteriaQuery));
    }

    public static <R> SelectManyStepSupplier<R> allByCriteria(Class<R> entity, CriteriaQuery<R> criteriaQuery) {
        return new CommonSelectManyStepSupplierImpl<>(getIterableByCriteria(entity, criteriaQuery));
    }

    public static <R> SelectOneStepSupplier<R> byQuery(Class<R> entity, String query, Object... parameters) {
        return new CommonSelectOneStepSupplierImpl<>(getSingleByQuery(entity, query, parameters));
    }

    public static <R> SelectManyStepSupplier<R> allByQuery(Class<R> entity, String query, Object... parameters) {
        return new CommonSelectManyStepSupplierImpl<>(getIterableByQuery(entity, query, parameters));
    }

    private static final class CommonSelectOneStepSupplierImpl<R> extends
            SelectOneStepSupplier<R>
            implements SetsDescription {

        private CommonSelectOneStepSupplierImpl(Function<HibernateContext, R> select) {
            super(select);
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectManyStepSupplierImpl<R>
            extends SelectManyStepSupplier<R>
            implements SetsDescription {

        private CommonSelectManyStepSupplierImpl(Function<HibernateContext, Iterable<R>> select) {
            super(select);
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectAsPageStepSupplier<R>
            extends SelectAsPageStepSupplier<R>
            implements SetsDescription {

        private CommonSelectAsPageStepSupplier(SelectionAsPage<R> select) {
            super(select);
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }
}
