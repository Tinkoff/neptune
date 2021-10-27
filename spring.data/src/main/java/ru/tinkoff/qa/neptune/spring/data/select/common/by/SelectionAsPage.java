package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import java.util.ArrayList;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@Description("As page. Page number: {number}. Page size: {size}. Offset: {offset}. Sorting: {sort}")
public final class SelectionAsPage<R, ID, T extends PagingAndSortingRepository<R, ID>> extends SpringDataFunction<T, Iterable<R>> {

    @DescriptionFragment("number")
    final int number;

    @DescriptionFragment("size")
    final int size;

    @DescriptionFragment("offset")
    final long offset;

    @DescriptionFragment("sort")
    final Sort sort;

    private final Pageable pageable;

    public SelectionAsPage(Pageable pageable) {
        super(PagingAndSortingRepository.class);
        checkNotNull(pageable);
        this.number = pageable.getPageNumber();
        this.size = pageable.getPageSize();
        this.offset = pageable.getOffset();
        this.sort = pageable.getSort();
        this.pageable = pageable;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(T t) {
        return new ArrayList<>(t.findAll(pageable).getContent());
    }
}
