package ru.tinkoff.qa.neptune.spring.data.select;

import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.Repository;
import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.repository.query.ReactiveQueryByExampleExecutor;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.data.repository.reactive.RxJava2CrudRepository;
import org.springframework.data.repository.reactive.RxJava3CrudRepository;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionAsPage;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByMethod;
import ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionBySorting;

import java.util.List;
import java.util.function.Function;

import static org.springframework.data.domain.ExampleMatcher.matching;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByExample.getIterableByExample;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByExample.getSingleByExample;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByIds.getIterableByIds;
import static ru.tinkoff.qa.neptune.spring.data.select.common.by.SelectionByIds.getSingleById;

@SuppressWarnings("unchecked")
public final class SelectStepFactoryCommon {

    private SelectStepFactoryCommon() {
        super();
    }

    public static <R, ID, T extends CrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new SelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new SelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new SelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectOneStepSupplier<R, ID, T> byId(T repository, ID id) {
        return new SelectOneStepSupplierImpl<>(repository, getSingleById(id));
    }


    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                  ID... ids) {
        return new SelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends ReactiveCrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                          ID... ids) {
        return new SelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends RxJava2CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                         ID... ids) {
        return new SelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }

    public static <R, ID, T extends RxJava3CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> byIds(T repository,
                                                                                                         ID... ids) {
        return new SelectManyStepSupplierImpl<>(repository, getIterableByIds(ids));
    }


    public static <R, ID, T extends CrudRepository<R, ID>> SelectManyStepSupplier<R, ID, T> allBySorting(T repository,
                                                                                                         Sort sort) {
        return new SelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
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
        return new SelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
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
        return new SelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
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
        return new SelectManyStepSupplierImpl<>(repository, new SelectionBySorting<>(sort));
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


    public static <R, ID, T extends Repository<R, ID> & QueryByExampleExecutor<R>> SelectOneStepSupplier<R, ID, T> byExample(T repository,
                                                                                                                             R probe,
                                                                                                                             ExampleMatcher matcher) {
        return new SelectOneStepSupplierImpl<>(repository, getSingleByExample(probe, matcher));
    }

    public static <R, ID, T extends Repository<R, ID> & QueryByExampleExecutor<R>> SelectOneStepSupplier<R, ID, T> byExample(T repository,
                                                                                                                             R probe) {
        return byExample(repository, probe, matching());
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectOneStepSupplier<R, ID, T> byExample(T repository,
                                                                                                                                     R probe,
                                                                                                                                     ExampleMatcher matcher) {
        return new SelectOneStepSupplierImpl<>(repository, getSingleByExample(probe, matcher));
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectOneStepSupplier<R, ID, T> byExample(T repository,
                                                                                                                                     R probe) {
        return byExample(repository, probe, matching());
    }


    public static <R, ID, T extends QueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(T repository,
                                                                                                                                 R probe,
                                                                                                                                 ExampleMatcher matcher,
                                                                                                                                 Sort sort) {
        return new SelectManyStepSupplierImpl<>(repository, getIterableByExample(probe, matcher, sort));
    }

    public static <R, ID, T extends QueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(T repository,
                                                                                                                                 R probe,
                                                                                                                                 Sort sort) {
        return allByExample(repository, probe, matching(), sort);
    }

    public static <R, ID, T extends QueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(
            T repository,
            R probe) {
        return allByExample(repository, probe, matching(), null);
    }

    public static <R, ID, T extends QueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(
            T repository,
            R probe,
            ExampleMatcher matcher) {
        return allByExample(repository, probe, matcher, null);
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(T repository,
                                                                                                                                         R probe,
                                                                                                                                         ExampleMatcher matcher,
                                                                                                                                         Sort sort) {
        return new SelectManyStepSupplierImpl<>(repository, getIterableByExample(probe, matcher, sort));
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(T repository,
                                                                                                                                         R probe,
                                                                                                                                         Sort sort) {
        return allByExample(repository, probe, matching(), sort);
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(
            T repository,
            R probe) {
        return allByExample(repository, probe, matching(), null);
    }

    public static <R, ID, T extends ReactiveQueryByExampleExecutor<R> & Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allByExample(
            T repository,
            R probe,
            ExampleMatcher matcher) {
        return allByExample(repository, probe, matcher, null);
    }


    public static <R, ID, T extends PagingAndSortingRepository<R, ID>> SelectManyStepSupplier<R, ID, T> asAPage(T repository,
                                                                                                                Pageable pageable) {
        return new SelectManyStepSupplierImpl<>(repository, new SelectionAsPage<>(pageable));
    }


    public static <R, ID, T extends Repository<R, ID> & QueryByExampleExecutor<R>> SelectOneStepSupplier<R, ID, T> by(T repository,
                                                                                                                      Function<T, R> f) {
        return new SelectOneStepSupplierImpl<>(repository, new SelectionByMethod<>(f));
    }

    public static <R, S extends Iterable<R>, ID, T extends Repository<R, ID>> SelectManyStepSupplier<R, ID, T> allBy(T repository,
                                                                                                                     Function<T, S> f) {
        return new SelectManyStepSupplierImpl<>(repository, new SelectionByMethod<>((Function<T, Iterable<R>>) f));
    }
}
