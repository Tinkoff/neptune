package ru.tinkoff.qa.neptune.spring.data.delete;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;

import static java.util.Objects.nonNull;

@SuppressWarnings("unchecked")
abstract class DeleteEntities<INPUT, R, ID, T extends Repository<R, ID>> extends SpringDataFunction<INPUT, Void> {

    final T repo;

    private DeleteEntities(T repo) {
        super(CrudRepository.class,
                ReactiveCrudRepository.class,
                RxJava2CrudRepository.class,
                RxJava3CrudRepository.class);
        this.repo = repo;
    }

    static class DeleteOne<R, ID, T extends Repository<R, ID>> extends DeleteEntities<R, R, ID, T> {

        DeleteOne(T repo) {
            super(repo);
        }

        @Override
        public Void apply(R r) {
            if (repo instanceof CrudRepository) {
                ((CrudRepository<R, ID>) repo).delete(r);
                return null;
            }

            if (repo instanceof ReactiveCrudRepository) {
                ((ReactiveCrudRepository<R, ID>) repo).delete(r).block();
                return null;
            }

            if (repo instanceof RxJava2CrudRepository) {
                var thrown = ((RxJava2CrudRepository<R, ID>) repo).delete(r).blockingGet();
                if (nonNull(thrown)) {
                    throw new RuntimeException(thrown);
                }
                return null;
            }

            if (repo instanceof RxJava3CrudRepository) {
                ((RxJava3CrudRepository<R, ID>) repo).delete(r).blockingAwait();
                return null;
            }

            throw unsupportedRepository(repo);
        }
    }

    static class DeleteMany<R, ID, T extends Repository<R, ID>> extends DeleteEntities<Iterable<R>, R, ID, T> {

        DeleteMany(T repo) {
            super(repo);
        }

        @Override
        public Void apply(Iterable<R> rs) {
            if (repo instanceof CrudRepository) {
                ((CrudRepository<R, ID>) repo).deleteAll(rs);
                return null;
            }

            if (repo instanceof ReactiveCrudRepository) {
                ((ReactiveCrudRepository<R, ID>) repo).deleteAll(rs).block();
                return null;
            }

            if (repo instanceof RxJava2CrudRepository) {
                var thrown = ((RxJava2CrudRepository<R, ID>) repo).deleteAll(rs).blockingGet();
                if (nonNull(thrown)) {
                    throw new RuntimeException(thrown);
                }
                return null;
            }

            if (repo instanceof RxJava3CrudRepository) {
                ((RxJava3CrudRepository<R, ID>) repo).deleteAll(rs).blockingAwait();
                return null;
            }

            throw unsupportedRepository(repo);
        }
    }
}
