package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isDeleted;
import static javax.jdo.JDOHelper.isPersistent;

@Deprecated
public class DBGetDeletedFunction<T extends PersistableObject> extends DBGetOperationFunction<T, DBGetDeletedFunction<T>> {

    private DBGetDeletedFunction(OperationSequentialGetSupplier<T> innerSupplier) {
        super(innerSupplier);
    }

    /**
     * Creates an instance of {@link DBGetDeletedFunction} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeDeleted a collection of objects to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetDeletedFunction}
     */
    public static <T extends PersistableObject> DBGetDeletedFunction<T> deleted(Collection<T> toBeDeleted) {
        var listToBeDeleted = getObjectsToBeDeleted(toBeDeleted);
        String description = format("%s Deleted objects from tables %s", listToBeDeleted.size(),
                listToBeDeleted
                        .stream()
                        .map(PersistableObject::fromTable)
                        .distinct()
                        .collect(joining(",")));
        return new DBGetDeletedFunction<>(new OperationSequentialGetSupplier<>(description, getDeleted(), listToBeDeleted));
    }

    /**
     * Creates an instance of {@link DBGetDeletedFunction} to build and supply a function that returns a
     * resulted list of DELETE operation.
     *
     * @param toBeDeleted a an array of objects to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetDeletedFunction}
     */
    @SafeVarargs
    public static <T extends PersistableObject> DBGetDeletedFunction<T> deleted(T... toBeDeleted) {
        return deleted(asList(toBeDeleted));
    }

    /**
     * Creates an instance of {@link DBGetDeletedFunction} to build and supply a function that returns a
     * resulted list of DELETE operation.
     *
     * @param toBeDeleted a an object to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetDeletedFunction}
     */
    public static <T extends PersistableObject> DBGetDeletedFunction<T> deleted(T toBeDeleted) {
        return deleted(of(toBeDeleted));
    }

    /**
     * Creates an instance of {@link DBGetDeletedFunction} to build and supply a function that returns a
     * resulted list of DELETE operation.
     *
     * @param select is how to find objects to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetDeletedFunction}
     */
    public static <T extends PersistableObject> DBGetDeletedFunction<T> deleted(SelectListGetSupplier<T, ?> select) {
        return new DBGetDeletedFunction<>(new OperationSequentialGetSupplier<>("Deleted objects",
                getDeleted(),
                select));
    }

    /**
     * Creates an instance of {@link DBGetDeletedFunction} to build and supply a function that returns a
     * resulted list of DELETE operation.
     *
     * @param select is how to find an object to be deleted.
     * @param <T> is a subclass of {@link DBGetDeletedFunction}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DBGetDeletedFunction}
     */
    public static <T extends PersistableObject> DBGetDeletedFunction<T> deleted(SelectOneGetSupplier<T, ?> select) {
        return new DBGetDeletedFunction<>(new OperationSequentialGetSupplier<>("Deleted objects",
                getDeleted(),
                select));
    }

    private static <T extends PersistableObject> List<T> getObjectsToBeDeleted(Collection<T> toBeInserted) {
        var toBeDeleted = getOperationObjects(toBeInserted);
        return toBeDeleted
                .stream()
                .filter(t -> isPersistent(t) && !isDeleted(t))
                .collect(toList());
    }

    @SuppressWarnings("unchecked")
    private static <T extends PersistableObject> BiFunction<Collection<T>, PersistenceManager, List<T>> getDeleted() {
        return (ts, persistenceManager) -> {
            persistenceManager.deletePersistentAll(ts);
            var result = new ArrayList<T>();
            ts.forEach(o -> result.add((T) o.clone()));
            return new OperationResultList<>("List of deleted data store objects", result);
        };
    }
}
