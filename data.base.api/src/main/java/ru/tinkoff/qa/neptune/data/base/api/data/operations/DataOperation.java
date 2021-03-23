package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.datanucleus.ExecutionContextImpl;
import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.enhancement.Persistable;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;

import java.util.*;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static javax.jdo.JDOHelper.isPersistent;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionDataReader.getConnection;

/**
 * Is the most abstract class for everything that describes operations on {@link PersistableObject} such
 * as inserting, deleting and updating.
 *
 * @param <T> is a type of objects to be operated and returned
 * @param <R> is a type of subclass of {@link DataOperation}
 */
public abstract class DataOperation<T extends PersistableObject, R extends DataOperation<T, R>>
        extends SequentialGetStepSupplier.GetIterableChainedStepSupplier<DataBaseStepContext, List<T>, Map<JDOPersistenceManager, List<T>>, T, R> {

    DataOperation(Function<Map<JDOPersistenceManager, List<T>>, List<T>> originalFunction) {
        super(originalFunction);
    }

    static <T extends PersistableObject> Map<JDOPersistenceManager, List<T>> getMap(DataBaseStepContext context,
                                                                                    Collection<T> toBeOperated) {
        var result = new LinkedHashMap<JDOPersistenceManager, List<T>>();

        toBeOperated.forEach(t -> {
            JDOPersistenceManager manager;
            if (isPersistent(t)) {
                manager = (JDOPersistenceManager) ((Persistable) t).dnGetStateManager().getExecutionContextReference().getOwner();
            } else {
                manager = context.getManager(getConnection(t.getClass()));
            }

            ofNullable(result.get(manager))
                    .ifPresentOrElse(ts -> ts.add(t),
                            () -> result.put(manager, new ArrayList<>(List.of(t))));
        });
        return result;
    }

    static void openTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            transaction.setOptimistic(true);
            transaction.begin();
        });
    }

    static void commitTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager ->
                jdoPersistenceManager
                        .currentTransaction()
                        .commit());
    }

    static void rollbackTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        });
    }

    static void preCommit(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager ->
                ((ExecutionContextImpl) jdoPersistenceManager
                        .getExecutionContext())
                        .preCommit());
    }

    /**
     * Updating a single stored record. The record to be updated is selected by query and then updated.
     *
     * @param howToSelect is a description of query how to select the record
     * @param set         are instances of {@link UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link UpdateOperation}
     */
    @SafeVarargs
    @Description("List of updated objects")
    public static <T extends PersistableObject> UpdateOperation<T, ?> updated(SelectASingle<T> howToSelect, UpdateExpression<T>... set) {
        return new UpdateOperation.UpdateBySelection<>(howToSelect, set);
    }

    /**
     * Updating a list of stored records. Records to be updated are selected by query and then updated.
     *
     * @param howToSelect is a description of query how to select records
     * @param set         are instances of {@link ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link UpdateOperation}
     */
    @SafeVarargs
    @Description("List of updated objects")
    public static <T extends PersistableObject> UpdateOperation<T, ?> updated(SelectList<?, List<T>> howToSelect, UpdateExpression<T>... set) {
        return new UpdateOperation.UpdateBySelection<>(howToSelect, set);
    }

    /**
     * Updating a list of stored records.
     *
     * @param toBeUpdated is a list of stored records that is selected firstly
     * @param set         are instances of {@link ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link UpdateOperation}
     */
    @SafeVarargs
    @Description("List of updated objects")
    public static <T extends PersistableObject> UpdateOperation<T, ?> updated(Collection<T> toBeUpdated, UpdateExpression<T>... set) {
        return new UpdateOperation.UpdateSelected<>(toBeUpdated, set);
    }

    /**
     * Deleting a single stored record. The record to be deleted is selected by query and then deleted.
     *
     * @param howToSelect is a description of query how to select the record
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DeleteOperation}
     */
    @Description("List of deleted objects")
    public static <T extends PersistableObject> DeleteOperation<T, ?> deleted(SelectASingle<T> howToSelect) {
        return new DeleteOperation.DeleteBySelection<>(howToSelect);
    }

    /**
     * Deleting a list of stored records. Records to be deleted are selected by query and then deleted.
     *
     * @param howToSelect is a description of query how to select records
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DeleteOperation}
     */
    @Description("List of deleted objects")
    public static <T extends PersistableObject> DeleteOperation<T, ?> deleted(SelectList<?, List<T>> howToSelect) {
        return new DeleteOperation.DeleteBySelection<>(howToSelect);
    }

    /**
     * Deleting a list of stored records.
     *
     * @param toBeDeleted is a list of stored records that is selected firstly
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DeleteOperation}
     */
    @Description("List of deleted objects")
    public static <T extends PersistableObject> DeleteOperation<T, ?> deleted(Collection<T> toBeDeleted) {
        return new DeleteOperation.DeleteSelected<>(toBeDeleted);
    }

    /**
     * Inserting a list of stored records.
     *
     * @param toBeInserted is a list of records to be inserted
     * @param <T>          is a type of {@link PersistableObject} to be inserted
     * @return an instance of {@link InsertOperation}
     */
    @Description("List of inserted objects")
    public static <T extends PersistableObject> InsertOperation<T> inserted(Collection<T> toBeInserted) {
        return new InsertOperation<>(toBeInserted);
    }
}
