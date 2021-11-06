package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.datanucleus.ExecutionContextImpl;
import org.datanucleus.api.jdo.JDOPersistenceManager;
import org.datanucleus.enhancement.Persistable;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.captors.DBCaptor;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;

import java.util.*;
import java.util.function.Function;

import static java.util.List.of;
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
@Deprecated(forRemoval = true)
@CaptureOnSuccess(by = DBCaptor.class)
@CaptureOnFailure(by = DBCaptor.class)
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Perform in database:")
public abstract class DataOperation<T extends PersistableObject, M, R extends DataOperation<T, M, R>>
        extends SequentialGetStepSupplier.GetListChainedStepSupplier<DataBaseStepContext, List<T>, M, T, R> {

    protected final PersistenceMapWrapper<T> mapWrapper;
    private DataBaseStepContext context;

    DataOperation(Function<M, List<T>> originalFunction, PersistenceMapWrapper<T> mapWrapper) {
        super(originalFunction);
        this.mapWrapper = mapWrapper;
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
    @Description("Update {toUpdate}")
    public static <T extends PersistableObject> UpdateOperation<T, T, ?> updated(
            @DescriptionFragment("toUpdate") SelectASingle<T> howToSelect,
            UpdateExpression<T>... set) {
        return new UpdateOperation.UpdateBySelection<>(new PersistenceMapWrapper<>(), howToSelect, set);
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
    @Description("Update {toUpdate}")
    public static <T extends PersistableObject> UpdateOperation<T, List<T>, ?> updated(
            @DescriptionFragment("toUpdate") SelectList<T, List<T>> howToSelect,
            UpdateExpression<T>... set) {
        return new UpdateOperation.UpdateBySelection<>(new PersistenceMapWrapper<>(), howToSelect, set);
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
     * Updating a list of stored records.
     *
     * @param toBeUpdated is a list of stored records that is selected firstly
     * @param set         are instances of {@link ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link UpdateOperation}
     */
    @SafeVarargs
    @Description("Update records of {of}")
    public static <T extends PersistableObject> UpdateOperation<T, Collection<T>, ?> updated(
            @DescriptionFragment(
                    value = "of",
                    makeReadableBy = TableNameGetterFromCollection.class) Collection<T> toBeUpdated,
            UpdateExpression<T>... set) {
        return new UpdateOperation.UpdateSelected<>(new PersistenceMapWrapper<>(), toBeUpdated, set);
    }

    /**
     * Deleting a single stored record. The record to be deleted is selected by query and then deleted.
     *
     * @param howToSelect is a description of query how to select the record
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DeleteOperation}
     */
    @Description("Delete {toDelete}")
    public static <T extends PersistableObject> DeleteOperation<T, T, ?> deleted(
            @DescriptionFragment("toDelete") SelectASingle<T> howToSelect) {
        return new DeleteOperation.DeleteBySelection<>(new PersistenceMapWrapper<>(), howToSelect);
    }

    /**
     * Deleting a list of stored records. Records to be deleted are selected by query and then deleted.
     *
     * @param howToSelect is a description of query how to select records
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DeleteOperation}
     */
    @Description("Delete {toDelete}")
    public static <T extends PersistableObject> DeleteOperation<T, List<T>, ?> deleted(
            @DescriptionFragment("toDelete") SelectList<T, List<T>> howToSelect) {
        return new DeleteOperation.DeleteBySelection<>(new PersistenceMapWrapper<>(), howToSelect);
    }

    /**
     * Deleting a list of stored records.
     *
     * @param toBeDeleted is a list of stored records that is selected firstly
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DeleteOperation}
     */
    @Description("Delete records from {from}")
    public static <T extends PersistableObject> DeleteOperation<T, Collection<T>, ?> deleted(
            @DescriptionFragment(
                    value = "from",
                    makeReadableBy = TableNameGetterFromCollection.class) Collection<T> toBeDeleted) {
        return new DeleteOperation.DeleteSelected<>(new PersistenceMapWrapper<>(), toBeDeleted);
    }

    /**
     * Inserting a list of stored records.
     *
     * @param toBeInserted is a list of records to be inserted
     * @param <T>          is a type of {@link PersistableObject} to be inserted
     * @return an instance of {@link InsertOperation}
     */
    @Description("Insert new records to {to}")
    public static <T extends PersistableObject> InsertOperation<T> inserted(@DescriptionFragment(
            value = "to",
            makeReadableBy = TableNameGetterFromCollection.class) Collection<T> toBeInserted) {
        return new InsertOperation<>(new PersistenceMapWrapper<>(), toBeInserted);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onStart(M m) {
        List<T> persistenceList;
        if (m instanceof PersistableObject) {
            persistenceList = of((T) m);
        } else {
            persistenceList = (List<T>) m;
        }

        mapWrapper.clear();
        var persistenceMap = mapWrapper.get();
        persistenceList.forEach(t -> {
            JDOPersistenceManager manager;
            if (isPersistent(t)) {
                manager = (JDOPersistenceManager) ((Persistable) t).dnGetStateManager().getExecutionContextReference().getOwner();
            } else {
                manager = context.getManager(getConnection(t.getClass()));
            }

            ofNullable(persistenceMap.get(manager))
                    .ifPresentOrElse(ts -> ts.add(t),
                            () -> persistenceMap.put(manager, new ArrayList<>(of(t))));
        });
    }

    @Override
    public Function<DataBaseStepContext, List<T>> get() {
        return context -> {
            this.context = context;
            return super.get().apply(context);
        };
    }

    static class PersistenceMapWrapper<T extends PersistableObject> {

        private final Map<JDOPersistenceManager, List<T>> persistenceMap = new LinkedHashMap<>();

        Map<JDOPersistenceManager, List<T>> get() {
            return persistenceMap;
        }

        void clear() {
            persistenceMap.clear();
        }
    }
}
