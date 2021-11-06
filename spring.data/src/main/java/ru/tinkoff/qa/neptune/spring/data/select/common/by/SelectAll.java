package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.data.repository.reactive.RxJava2SortingRepository;
import org.springframework.data.repository.reactive.RxJava3SortingRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
@Description("all")
public final class SelectAll<R, ID, T extends Repository<R, ID>> extends SpringDataFunction<T, Iterable<R>> {

    public SelectAll() {
        super(PagingAndSortingRepository.class,
                ReactiveSortingRepository.class,
                RxJava2SortingRepository.class,
                RxJava3SortingRepository.class);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(T t) {
        if (t instanceof PagingAndSortingRepository) {
            return ((PagingAndSortingRepository<R, ID>) t).findAll();
        }

        if (t instanceof ReactiveSortingRepository) {
            return ((ReactiveSortingRepository<R, ID>) t).findAll().collectList().block();
        }

        if (t instanceof RxJava2SortingRepository) {
            return ((RxJava2SortingRepository<R, ID>) t).findAll().toList().blockingGet();
        }

        if (t instanceof RxJava3SortingRepository) {
            return ((RxJava3SortingRepository<R, ID>) t).findAll().toList().blockingGet();
        }

        throw unsupportedRepository(t);
    }
}
