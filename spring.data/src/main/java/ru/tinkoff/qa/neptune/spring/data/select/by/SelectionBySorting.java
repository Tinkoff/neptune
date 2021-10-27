package ru.tinkoff.qa.neptune.spring.data.select.by;

import com.google.common.collect.Lists;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.data.repository.reactive.RxJava2SortingRepository;
import org.springframework.data.repository.reactive.RxJava3SortingRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Lists.newArrayList;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
@Description("all by sorting '{sort}'")
public final class SelectionBySorting<R, ID, T extends Repository<R, ID>> extends SpringDataFunction<T, Iterable<R>> {

    @DescriptionFragment("sort")
    final Sort sort;

    public SelectionBySorting(Sort sort) {
        super(PagingAndSortingRepository.class,
                ReactiveSortingRepository.class,
                RxJava2SortingRepository.class,
                RxJava3SortingRepository.class);
        checkNotNull(sort);
        this.sort = sort;
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(T t) {
        if (t instanceof PagingAndSortingRepository) {
            return newArrayList(((PagingAndSortingRepository<R, ID>) t).findAll(sort));
        }

        if (t instanceof ReactiveSortingRepository) {
            return ofNullable(((ReactiveSortingRepository<R, ID>) t).findAll(sort).collectList().block())
                    .map(Lists::newArrayList)
                    .orElse(null);
        }

        if (t instanceof RxJava2SortingRepository) {
            return ofNullable(((RxJava2SortingRepository<R, ID>) t).findAll(sort).toList().blockingGet())
                    .map(Lists::newArrayList)
                    .orElse(null);
        }

        if (t instanceof RxJava3SortingRepository) {
            return ofNullable(((RxJava3SortingRepository<R, ID>) t).findAll(sort).toList().blockingGet())
                    .map(Lists::newArrayList)
                    .orElse(null);
        }

        throw unsupportedRepository(t);
    }
}
