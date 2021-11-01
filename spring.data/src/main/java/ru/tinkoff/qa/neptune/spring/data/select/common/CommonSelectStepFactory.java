package ru.tinkoff.qa.neptune.spring.data.select.common;

import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.spring.data.select.HasRepositoryInfo;
import ru.tinkoff.qa.neptune.spring.data.select.SelectManyStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SelectOneStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.select.SetsDescription;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.*;

import java.util.List;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByExample.getIterableByExample;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByExample.getSingleByExample;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByIds.getIterableByIds;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByIds.getSingleById;

@SuppressWarnings("unchecked")
public final class CommonSelectStepFactory {

    private CommonSelectStepFactory() {
        super();
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new CommonSelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new CommonSelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new CommonSelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new CommonSelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }


    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                  ID... ids) {
        return new CommonSelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                          ID... ids) {
        return new CommonSelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                         ID... ids) {
        return new CommonSelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                         ID... ids) {
        return new CommonSelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> all(T repository) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectAll<>());
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> all(T repository) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectAll<>());
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> all(T repository) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectAll<>());
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> all(T repository) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectAll<>());
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                         Sort sort) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                         String... properties) {
        return allBySorting(repository, Sort.by(properties));
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                         List<Sort.Order> orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                         Sort.Order... orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                         Sort.Direction direction,
                                                                                                         String... properties) {
        return allBySorting(repository, Sort.by(direction, properties));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                 Sort sort) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                 String... properties) {
        return allBySorting(repository, Sort.by(properties));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                 List<Sort.Order> orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                 Sort.Order... orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                 Sort.Direction direction,
                                                                                                                 String... properties) {
        return allBySorting(repository, Sort.by(direction, properties));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                Sort sort) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                String... properties) {
        return allBySorting(repository, Sort.by(properties));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                List<Sort.Order> orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                Sort.Order... orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                Sort.Direction direction,
                                                                                                                String... properties) {
        return allBySorting(repository, Sort.by(direction, properties));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                Sort sort) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                String... properties) {
        return allBySorting(repository, Sort.by(properties));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                List<Sort.Order> orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                Sort.Order... orders) {
        return allBySorting(repository, Sort.by(orders));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                                Sort.Direction direction,
                                                                                                                String... properties) {
        return allBySorting(repository, Sort.by(direction, properties));
    }


    public static <R, ID, T extends Repository<R, ID> & QueryByExampleExecutor<R>> SelectOneByExampleStepSupplier<R, ID, T> byExample(T repository,
                                                                                                                                      R probe) {
        return new CommonSelectOneByExampleStepSupplier<>(probe, repository, getSingleByExample());
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectOneByExampleStepSupplier<R, ID, T> byExample(T repository,
                                                                                                                                              R probe) {
        return new CommonSelectOneByExampleStepSupplier<>(probe, repository, getSingleByExample());
    }


    public static <R, ID, T extends QueryByExampleExecutor<R> & Repository<R, ID>> SelectManyByExampleStepSupplier<R, ID, T> allByExample(T repository,
                                                                                                                                          R probe) {
        return new CommonSelectManyByExampleStepSupplier<>(probe, repository, getIterableByExample());
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyByExampleStepSupplier<R, ID, T> allByExample(T repository,
                                                                                                                                                  R probe) {
        return new CommonSelectManyByExampleStepSupplier<>(probe, repository, getIterableByExample());
    }


    public static <R, ID, T extends PagingAndSortingRepository<R, ID>> SelectAsPageStepSupplier<R, ID, T> asAPage(T repository) {
        return new CommonSelectAsPageStepSupplier<>(repository, new SelectionAsPage<>());
    }


    public static <R, ID, T extends Repository<R, ID> & QueryByExampleExecutor<R>> SelectOneStepSupplier<R, ID, T> byInvocation(T repository,
                                                                                                                                Function<T, R> f) {
        return new CommonSelectOneStepSupplierImpl<>(repository, new SelectionByMethod<>(f));
    }

    public static <R, S extends Iterable<R>, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByInvocation(T repository,
                                                                                                                               Function<T, S> f) {
        return new CommonSelectManyStepSupplierImpl<>(repository, new SelectionByMethod<>((Function<T, Iterable<R>>) f));
    }

    private static final class CommonSelectOneStepSupplierImpl<R, ID, T extends Repository<R, ID>> extends
            SelectOneStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        private CommonSelectOneStepSupplierImpl(T repository, Function<T, R> select) {
            super(repository, select);
        }

        @Override
        public T getRepository() {
            return HasRepositoryInfo.super.getRepository();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectManyStepSupplierImpl<R, ID, T extends Repository<R, ID>>
            extends SelectManyStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        private CommonSelectManyStepSupplierImpl(T repository, Function<T, Iterable<R>> select) {
            super(repository, select);
        }

        @Override
        public T getRepository() {
            return HasRepositoryInfo.super.getRepository();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectAsPageStepSupplier<R, ID, T extends PagingAndSortingRepository<R, ID>>
            extends SelectAsPageStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        private CommonSelectAsPageStepSupplier(T repository, SelectionAsPage<R, ID, T> select) {
            super(repository, select);
        }

        @Override
        public T getRepository() {
            return HasRepositoryInfo.super.getRepository();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectOneByExampleStepSupplier<R, ID, T extends Repository<R, ID>> extends SelectOneByExampleStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {

        private CommonSelectOneByExampleStepSupplier(R probe, T repository, SelectionByExample.SelectASingleByExample<R, ID, T> select) {
            super(probe, repository, select);
        }

        @Override
        public T getRepository() {
            return HasRepositoryInfo.super.getRepository();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }

    private static final class CommonSelectManyByExampleStepSupplier<R, ID, T extends Repository<R, ID>> extends SelectManyByExampleStepSupplier<R, ID, T>
            implements SetsDescription, HasRepositoryInfo<R, ID, T> {


        private CommonSelectManyByExampleStepSupplier(R probe, T repository, SelectionByExample.SelectIterableByExample<R, ID, T> select) {
            super(probe, repository, select);
        }

        @Override
        public T getRepository() {
            return HasRepositoryInfo.super.getRepository();
        }

        @Override
        public void changeDescription(String description) {
            SetsDescription.super.changeDescription(description);
        }
    }
}
