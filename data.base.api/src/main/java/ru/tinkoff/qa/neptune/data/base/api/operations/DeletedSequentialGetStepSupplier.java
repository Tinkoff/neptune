package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.*;

import javax.jdo.PersistenceManager;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static java.lang.String.format;
import static java.util.Arrays.asList;
import static java.util.List.of;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isDeleted;
import static javax.jdo.JDOHelper.isPersistent;

/**
 * This is the class that builds and supplies functions to return results of DELETED operation.
 * Transactions are used to keep data of the data store valid when something is going wrong. DELETED
 * operations are supposed to return lists of deleted objects.
 *
 * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
 */
public class DeletedSequentialGetStepSupplier<T extends PersistableObject>
        extends OperationSequentialGetSupplier<T, DeletedSequentialGetStepSupplier<T>> {

    private static final String DESCRIPTION = "Deleted objects";

    private DeletedSequentialGetStepSupplier(Collection<T> toBeDeleted) {
        super(getDescription(format("%s:", DESCRIPTION), toBeDeleted), getObjectsToBeDeleted(toBeDeleted));
    }

    private DeletedSequentialGetStepSupplier(SelectSequentialGetStepSupplier<?, ?, ?> select) {
        super(DESCRIPTION, select);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeDeleted a collection of objects to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DeletedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(Collection<T> toBeDeleted) {
        return new DeletedSequentialGetStepSupplier<>(toBeDeleted);
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeDeleted a an array of objects to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DeletedSequentialGetStepSupplier}
     */
    @SafeVarargs
    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(T... toBeDeleted) {
        return new DeletedSequentialGetStepSupplier<>(asList(toBeDeleted));
    }

    /**
     * Creates an instance of {@link UpdatedSequentialGetStepSupplier} to build and supply a function that returns a
     * resulted list of UPDATE operation.
     *
     * @param toBeDeleted a an object to be deleted.
     * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
     * @return is an instance of {@link DeletedSequentialGetStepSupplier}
     */
    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(T toBeDeleted) {
        return new DeletedSequentialGetStepSupplier<>(of(toBeDeleted));
    }

    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(SelectListByIdsSupplier<T> selectId) {
        return new DeletedSequentialGetStepSupplier<>(selectId);
    }

    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(SelectSingleObjectByIdSupplier<T> selectId) {
        return new DeletedSequentialGetStepSupplier<>(selectId);
    }

    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(SelectListByQuerySupplier<T, ?> selectByQuery) {
        return new DeletedSequentialGetStepSupplier<>(selectByQuery);
    }

    public static <T extends PersistableObject> DeletedSequentialGetStepSupplier<T> deleted(SelectSingleObjectByQuerySupplier<T, ?> selectByQuery) {
        return new DeletedSequentialGetStepSupplier<>(selectByQuery);
    }

    private static <T extends PersistableObject> List<T> getObjectsToBeDeleted(Collection<T> toBeInserted) {
        var toBeDeleted = getOperationObjects(toBeInserted);
        return toBeDeleted
                .stream()
                .filter(t -> isPersistent(t) && !isDeleted(t))
                .collect(toList());
    }

    @Override
    @SuppressWarnings("unchecked")
    List<T> getResult(PersistenceManager persistenceManager, List<T> listOfObjectsForTransaction) {
        persistenceManager.deletePersistentAll(listOfObjectsForTransaction);
        var result = new ArrayList<T>();
        listOfObjectsForTransaction.forEach(o -> result.add((T) o.clone()));
        return new OperationResultList<>("List of deleted data store objects", result);
    }
}
