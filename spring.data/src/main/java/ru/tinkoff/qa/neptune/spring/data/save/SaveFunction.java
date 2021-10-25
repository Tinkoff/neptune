package ru.tinkoff.qa.neptune.spring.data.save;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.spring.data.SpringDataFunction;


@SuppressWarnings("unchecked")
abstract class SaveFunction<R, ID, T extends Repository<R, ID>, INPUT, RESULT> extends SpringDataFunction<INPUT, RESULT> {

    final T repo;

    private SaveFunction(T repo) {
        super(CrudRepository.class,
                ReactiveCrudRepository.class,
                RxJava2CrudRepository.class,
                RxJava3CrudRepository.class);
        this.repo = repo;
    }

    static class SaveOne<R, ID, T extends Repository<R, ID>> extends SaveFunction<R, ID, T, R, R> {

        SaveOne(T repo) {
            super(repo);
        }

        @Override
        public R apply(R toSave) {
            if (repo instanceof CrudRepository) {
                return ((CrudRepository<R, ID>) repo).save(toSave);
            }

            if (repo instanceof ReactiveCrudRepository) {
                return ((ReactiveCrudRepository<R, ID>) repo).save(toSave).block();
            }

            if (repo instanceof RxJava2CrudRepository) {
                return ((RxJava2CrudRepository<R, ID>) repo).save(toSave).blockingGet();
            }

            if (repo instanceof RxJava3CrudRepository) {
                return ((RxJava3CrudRepository<R, ID>) repo).save(toSave).blockingGet();
            }

            throw unsupportedRepository(repo);
        }
    }

    static class SaveMany<R, ID, T extends Repository<R, ID>> extends SaveFunction<R, ID, T, Iterable<R>, Iterable<R>> {

        SaveMany(T repo) {
            super(repo);
        }

        @Override
        public Iterable<R> apply(Iterable<R> toSave) {
            if (repo instanceof CrudRepository) {
                return ((CrudRepository<R, ID>) repo).saveAll(toSave);
            }

            if (repo instanceof ReactiveCrudRepository) {
                return ((ReactiveCrudRepository<R, ID>) repo).saveAll(toSave).toIterable();
            }

            if (repo instanceof RxJava2CrudRepository) {
                return ((RxJava2CrudRepository<R, ID>) repo).saveAll(toSave).blockingIterable();
            }

            if (repo instanceof RxJava3CrudRepository) {
                return ((RxJava3CrudRepository<R, ID>) repo).saveAll(toSave).blockingIterable();
            }

            throw unsupportedRepository(repo);
        }
    }
}
