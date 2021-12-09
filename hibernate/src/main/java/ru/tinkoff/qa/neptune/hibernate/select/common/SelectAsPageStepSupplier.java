package ru.tinkoff.qa.neptune.hibernate.select.common;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.hibernate.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.hibernate.select.common.by.SelectionAsPage;

import javax.persistence.criteria.Order;
import java.util.List;

import static java.util.Objects.nonNull;

public abstract class SelectAsPageStepSupplier<R> extends SelectManyStepSupplier<R> {

    private final SelectionAsPage<R> select;
    @StepParameter("limit")
    int limit = 1;
    @StepParameter("offset")
    int offset;
    @StepParameter("orders")
    List<Order> orders;

    protected SelectAsPageStepSupplier(Class<R> entity, SelectionAsPage<R> select) {
        super(entity, select);
        this.select = select;
    }

    public SelectAsPageStepSupplier<R> limit(int limit) {
        this.limit = limit;
        return this;
    }

    public SelectAsPageStepSupplier<R> offset(int offset) {
        this.offset = offset;
        return this;
    }

    public SelectAsPageStepSupplier<R> order(List<Order> orders) {
        this.orders = orders;
        return this;
    }

    public SelectAsPageStepSupplier<R> order(Order... orders) {
        this.orders = List.of(orders);
        return this;
    }

    @Override
    protected void onStart(Class<R> t) {
        if (nonNull(orders)) {
            select.setLimitOffset(limit, offset, orders);
        } else {
            select.setLimitOffset(limit, offset);
        }
        super.onStart(t);
    }

}
