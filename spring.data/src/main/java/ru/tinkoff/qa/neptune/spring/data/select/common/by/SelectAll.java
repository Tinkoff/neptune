package ru.tinkoff.qa.neptune.spring.data.select.common.by;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
@Description("all")
public final class SelectAll<R, ID, T extends Repository<R, ID>> extends SpringDataFunction<T, Iterable<R>> {

    public SelectAll() {
        super(CrudRepository.class,
                ReactiveCrudRepository.class,
                RxJava2CrudRepository.class,
                RxJava3CrudRepository.class);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    @Override
    public Iterable<R> apply(T t) {
        if (t instanceof CrudRepository) {
            return ((CrudRepository<R, ID>) t).findAll();
        }

        if (t instanceof ReactiveCrudRepository) {
            return ((ReactiveCrudRepository<R, ID>) t).findAll().collectList().block();
        }

        if (t instanceof RxJava2CrudRepository) {
            return ((RxJava2CrudRepository<R, ID>) t).findAll().toList().blockingGet();
        }

        if (t instanceof RxJava3CrudRepository) {
            return ((RxJava3CrudRepository<R, ID>) t).findAll().toList().blockingGet();
        }

        throw unsupportedRepository(t);
    }
}