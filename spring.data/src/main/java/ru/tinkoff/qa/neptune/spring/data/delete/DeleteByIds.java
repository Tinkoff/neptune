package ru.tinkoff.qa.neptune.spring.data.delete;

import com.google.common.collect.Iterables;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;

@SuppressWarnings("unchecked")
abstract class DeleteByIds<R, ID, T extends Repository<R, ID>> extends SpringDataFunction<T, Void> {

    private DeleteByIds() {
        super(CrudRepository.class,
                ReactiveCrudRepository.class,
                RxJava2CrudRepository.class,
                RxJava3CrudRepository.class);
    }

    static class DeleteOneById<R, ID, T extends Repository<R, ID>> extends DeleteByIds<R, ID, T> {

        private final ID id;

        DeleteOneById(ID id) {
            super();
            checkNotNull(id);
            this.id = id;
        }

        @Override
        public Void apply(T t) {
            if (t instanceof CrudRepository) {
                ((CrudRepository<R, ID>) t).deleteById(id);
                return null;
            }

            if (t instanceof ReactiveCrudRepository) {
                return ((ReactiveCrudRepository<R, ID>) t).deleteById(id).block();
            }

            if (t instanceof RxJava2CrudRepository) {
                var thrown = ((RxJava2CrudRepository<R, ID>) t).deleteById(id).blockingGet();
                if (nonNull(thrown)) {
                    throw new RuntimeException(thrown);
                }
                return null;
            }

            if (t instanceof RxJava3CrudRepository) {
                ((RxJava3CrudRepository<R, ID>) t).deleteById(id).blockingAwait();
                return null;
            }

            throw unsupportedRepository(t);
        }
    }

    static class DeleteIterableById<R, ID, T extends Repository<R, ID>> extends DeleteByIds<R, ID, T> {

        private final Iterable<ID> ids;

        DeleteIterableById(Iterable<ID> ids) {
            super();
            checkNotNull(ids);
            checkArgument(Iterables.size(ids) > 0, "Should be defined at least one item to delete");
            this.ids = ids;
        }

        @Override
        public Void apply(T t) {
            if (t instanceof CrudRepository) {
                ((CrudRepository<R, ID>) t).deleteAllById(ids);
                return null;
            }

            if (t instanceof ReactiveCrudRepository) {
                return ((ReactiveCrudRepository<R, ID>) t).deleteAllById(ids).block();
            }

            if (t instanceof RxJava2CrudRepository) {
                var thrown = ((RxJava2CrudRepository<R, ID>) t).deleteAllById(ids).blockingGet();
                if (nonNull(thrown)) {
                    throw new RuntimeException(thrown);
                }
                return null;
            }

            if (t instanceof RxJava3CrudRepository) {
                ((RxJava3CrudRepository<R, ID>) t).deleteAllById(ids).blockingAwait();
                return null;
            }

            throw unsupportedRepository(t);
        }
    }
}
