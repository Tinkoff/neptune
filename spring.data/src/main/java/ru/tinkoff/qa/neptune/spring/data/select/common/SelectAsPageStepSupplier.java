package ru.tinkoff.qa.neptune.spring.data.select.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionAsPage;

import java.util.List;

import static java.util.Objects.nonNull;
import static org.springframework.data.domain.Sort.unsorted;

public abstract class SelectAsPageStepSupplier<R, ID, T extends PagingAndSortingRepository<R, ID>> extends SelectManyStepSupplier<R, ID, T> {

    private final SelectionAsPage<R, ID, T> select;
    @StepParameter("number")
    int number;
    @StepParameter("size")
    int size = 1;
    @StepParameter("sort")
    Sort sort = unsorted();

    protected SelectAsPageStepSupplier(T repository, SelectionAsPage<R, ID, T> select) {
        super(repository, select);
        this.select = select;
    }

    public SelectAsPageStepSupplier<R, ID, T> number(int number) {
        this.number = number;
        return this;
    }

    public SelectAsPageStepSupplier<R, ID, T> size(int size) {
        this.size = size;
        return this;
    }

    public SelectAsPageStepSupplier<R, ID, T> sort(Sort sort) {
        this.sort = sort;
        return this;
    }

    public SelectAsPageStepSupplier<R, ID, T> sort(String... properties) {
        return sort(Sort.by(properties));
    }

    public SelectAsPageStepSupplier<R, ID, T> sort(List<Sort.Order> orders) {
        return sort(Sort.by(orders));
    }

    public SelectAsPageStepSupplier<R, ID, T> sort(Sort.Order... orders) {
        return sort(Sort.by(orders));
    }

    public SelectAsPageStepSupplier<R, ID, T> sort(Sort.Direction direction,
                                                   String... properties) {
        return sort(Sort.by(direction, properties));
    }

    @Override
    protected void onStart(T t) {
        if (nonNull(sort)) {
            select.setPageable(PageRequest.of(number, size, sort));
        } else {
            select.setPageable(PageRequest.of(number, size));
        }
        super.onStart(t);
    }

}
