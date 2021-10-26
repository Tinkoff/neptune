package ru.tinkoff.qa.neptune.spring.data.select.by;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.spring.data.IDParameterValueGetter;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

@SuppressWarnings("unchecked")
@Description("id(s) {ids}")
public abstract class SelectionByIds<R, ID, T extends Repository<R, ID>, RESULT> extends SpringDataFunction<T, RESULT> {

    @DescriptionFragment(value = "ids", makeReadableBy = IDParameterValueGetter.class)
    final ID[] ids;

    @SafeVarargs
    private SelectionByIds(ID... ids) {
        super(CrudRepository.class,
                ReactiveCrudRepository.class,
                RxJava2CrudRepository.class,
                RxJava3CrudRepository.class);
        checkNotNull(ids);
        checkArgument(ids.length > 0, "Should be defined at least one id");
        this.ids = ids;
    }

    public static <R, ID, T extends Repository<R, ID>> SelectionByIds<R, ID, T, R> getSingleById(ID id) {
        return new SelectASingleById<>(id);
    }

    public static <R, ID, T extends Repository<R, ID>> SelectionByIds<R, ID, T, Iterable<R>> getIterableByIds(ID... ids) {
        return new SelectIterableById<>(ids);
    }

    @Override
    public String toString() {
        return translate(this);
    }

    private static final class SelectASingleById<R, ID, T
            extends Repository<R, ID>> extends SelectionByIds<R, ID, T, R> {

        private SelectASingleById(ID id) {
            super(id);
        }

        @Override
        public R apply(T t) {
            var id = ids[0];
            if (t instanceof CrudRepository) {
                return ((CrudRepository<R, ID>) t).findById(id).orElse(null);
            }

            if (t instanceof ReactiveCrudRepository) {
                return ((ReactiveCrudRepository<R, ID>) t).findById(id).block();
            }

            if (t instanceof RxJava2CrudRepository) {
                return ((RxJava2CrudRepository<R, ID>) t).findById(id).blockingGet();
            }

            if (t instanceof RxJava3CrudRepository) {
                return ((RxJava3CrudRepository<R, ID>) t).findById(id).blockingGet();
            }

            throw unsupportedRepository(t);
        }
    }

    private static final class SelectIterableById<R, ID, T
            extends Repository<R, ID>> extends SelectionByIds<R, ID, T, Iterable<R>> {

        private SelectIterableById(ID... ids) {
            super(ids);
        }

        @Override
        public Iterable<R> apply(T t) {
            if (t instanceof CrudRepository) {
                return ((CrudRepository<R, ID>) t).findAllById(asList(ids));
            }

            if (t instanceof ReactiveCrudRepository) {
                return ((ReactiveCrudRepository<R, ID>) t).findAllById(asList(ids)).collectList().block();
            }

            if (t instanceof RxJava2CrudRepository) {
                return ((RxJava2CrudRepository<R, ID>) t).findAllById(asList(ids)).toList().blockingGet();
            }

            if (t instanceof RxJava3CrudRepository) {
                return ((RxJava3CrudRepository<R, ID>) t).findAllById(asList(ids)).toList().blockingGet();
            }

            throw unsupportedRepository(t);
        }
    }
}
