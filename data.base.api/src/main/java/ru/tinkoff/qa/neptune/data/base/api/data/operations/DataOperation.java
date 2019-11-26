package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.apache.commons.lang3.StringUtils;
import org.datanucleus.ExecutionContextImpl;
import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.ResultPersistentManager;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.List.copyOf;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isPersistent;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.action;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionDataReader.getConnection;

/**
 * This class is designed to perform available operations on stored data such as the inserting/updating/deleting
 * and return results.
 *
 * @param <T> is a type of {@link PersistableObject} to be operated (e.g. inserted, updated or deleted)
 */
@SuppressWarnings("unchecked")
@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public final class DataOperation<T extends PersistableObject> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<DataBaseStepContext, List<T>, Map<JDOPersistenceManager, List<T>>, T, DataOperation<T>> {

    private static final ResultPersistentManager RESULT_PERSISTENT_MANAGER = new ResultPersistentManager() {
    };

    private DataOperation(String description, Function<Map<JDOPersistenceManager, List<T>>, List<T>> originalFunction) {
        super(description, originalFunction);
    }

    /**
     * Updating a single stored record. The record to be updated is selected by query and then updated.
     *
     * @param howToSelect is a description of query how to select the record
     * @param set         are instances of {@link UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> updated(SelectASingle<T, ?, ?> howToSelect, UpdateExpression<T>... set) {
        checkArgument(nonNull(howToSelect), "A strategy that describes how to select an object to be updated " +
                "should be defined as a value that differs from null");
        checkArgument(nonNull(set), "Update-action should be defined");
        checkArgument(set.length > 0, "At least one update-action should be defined");
        return new DataOperation<T>(format("Updated %s", howToSelect),
                jdoPersistenceManagerListMap -> update(jdoPersistenceManagerListMap, set))
                .from(context -> {
                    RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                    var result = context.select(howToSelect);
                    var list = ofNullable(result).map(List::of).orElseGet(List::of);
                    return getMap(context, list);
                });
    }

    /**
     * Updating a list of stored records. Records to be updated are selected by query and then updated.
     *
     * @param howToSelect is a description of query how to select records
     * @param set         are instances of {@link UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> updated(SelectList<?, List<T>, ?> howToSelect, UpdateExpression<T>... set) {
        checkArgument(nonNull(howToSelect), "A strategy that describes how to select objects to be updated " +
                "should be defined as a value that differs from null");
        checkArgument(nonNull(set), "Update-action should be defined");
        checkArgument(set.length > 0, "At least one update-action should be defined");
        return new DataOperation<T>(format("Updated %s", howToSelect),
                jdoPersistenceManagerListMap -> update(jdoPersistenceManagerListMap, set))
                .from(context -> {
                    RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                    return getMap(context, context.select(howToSelect));
                });
    }

    /**
     * Updating a list of stored records.
     *
     * @param toBeUpdated is a list of stored records that is selected firstly
     * @param set         are instances of {@link UpdateExpression} that describe how to update the record
     * @param <T>         is a type of {@link PersistableObject} to be updated
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> updated(Collection<T> toBeUpdated, UpdateExpression<T>... set) {
        checkArgument(nonNull(toBeUpdated),
                "Collection of objects to be updated should be defined as a value that differs from null");
        checkArgument(nonNull(set), "Update-action should be defined");
        checkArgument(set.length > 0, "At least one update-action should be defined");

        var toUpdate = toBeUpdated
                .stream()
                .filter(Objects::nonNull)
                .collect(toList());

        checkArgument(toUpdate.size() > 0,
                "At least one object to be updated should be defined");

        return new DataOperation<T>(format("Updated %s object/objects from table/tables %s",
                toUpdate.size(),
                toUpdate.stream()
                        .map(PersistableObject::fromTable)
                        .distinct()
                        .collect(toList())),
                jdoPersistenceManagerListMap -> update(jdoPersistenceManagerListMap, set))
                .from(context -> getMap(context, toUpdate));
    }

    /**
     * Deleting a single stored record. The record to be deleted is selected by query and then deleted.
     *
     * @param howToSelect is a description of query how to select the record
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> deleted(SelectASingle<T, ?, ?> howToSelect) {
        checkArgument(nonNull(howToSelect), "A strategy that describes how to select and object to be deleted " +
                "should be defined as a value that differs from null");
        return new DataOperation<T>(format("Deleted %s", howToSelect),
                DataOperation::delete)
                .from(context -> {
                    RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                    var result = context.select(howToSelect);
                    var list = ofNullable(result).map(List::of).orElseGet(List::of);
                    return getMap(context, list);
                });
    }

    /**
     * Deleting a list of stored records. Records to be deleted are selected by query and then deleted.
     *
     * @param howToSelect is a description of query how to select records
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> deleted(SelectList<?, List<T>, ?> howToSelect) {
        checkArgument(nonNull(howToSelect), "A strategy that describes how to select objects to be deleted " +
                "should be defined as a value that differs from null");
        return new DataOperation<T>(format("Deleted %s", howToSelect),
                DataOperation::delete)
                .from(context -> {
                    RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                    return getMap(context, context.select(howToSelect));
                });
    }

    /**
     * Deleting a list of stored records.
     *
     * @param toBeDeleted is a list of stored records that is selected firstly
     * @param <T>         is a type of {@link PersistableObject} to be deleted
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> deleted(Collection<T> toBeDeleted) {
        checkArgument(nonNull(toBeDeleted),
                "Collection of objects to be deleted should be defined as a value that differs from null");

        var toDelete = toBeDeleted
                .stream()
                .filter(Objects::nonNull)
                .collect(toList());

        return new DataOperation<T>(format("Deleted %s object/objects from table/tables %s",
                toDelete.size(),
                toDelete.stream()
                        .map(PersistableObject::fromTable)
                        .distinct()
                        .collect(toList())),
                DataOperation::delete)
                .from(context -> getMap(context, toDelete));
    }

    /**
     * Inserting a list of stored records.
     *
     * @param toBeInserted is a list of records to be inserted
     * @param <T>          is a type of {@link PersistableObject} to be inserted
     * @return an instance of {@link DataOperation}
     */
    public static <T extends PersistableObject> DataOperation<T> inserted(Collection<T> toBeInserted) {
        checkArgument(nonNull(toBeInserted),
                "Collection of objects to be inserted should be defined as a value that differs from null");

        var toInsert = toBeInserted
                .stream()
                .filter(Objects::nonNull)
                .collect(toList());

        checkArgument(toInsert.size() > 0,
                "At least one object to be inserted should be defined");

        return new DataOperation<T>(format("Inserted %s object/objects",
                toInsert.size()),
                DataOperation::insert)
                .from(context -> getMap(context, toInsert));
    }

    private static <T extends PersistableObject> List<T> makeEveryThingPersistentIfNecessary(JDOPersistenceManager manager, List<T> persistable) {
        var result = new ArrayList<T>();

        persistable.forEach(t ->  {
           T t2;
           if (!isPersistent(t)) {
               t2 = manager.makePersistent(t);
           }
           else {
               t2 = t;
           }
           result.add(t2);
        });
        return result;
    }

    private static <T extends PersistableObject> List<T> update(Map<JDOPersistenceManager, List<T>> connectionMap, UpdateExpression<T>... set) {
        var managerSet = connectionMap.keySet();
        openTransaction(managerSet);

        try {
            var result = new ListOfPersistentObjects<T>() {
                public String toString() {
                    var resultStr = format("%s updated object/objects", size());
                    var tableList = stream().map(PersistableObject::fromTable)
                            .filter(StringUtils::isNotBlank)
                            .distinct()
                            .collect(toList());

                    if (tableList.size() > 0) {
                        resultStr = format("%s of table/tables %s", resultStr, join(",", tableList));
                    }
                    return resultStr;
                }
            };

            var idSetter = new IdSetter() {
            };

            stream(set).forEach(setAction -> {
                var consumer = setAction.getUpdateAction();
                action(consumer.toString(), (Consumer<Map<JDOPersistenceManager, List<T>>>) map -> {
                    for (var entry : map.entrySet()) {
                        consumer.accept(entry.getValue());
                        entry.setValue(makeEveryThingPersistentIfNecessary(entry.getKey(), entry.getValue()));
                        preCommit(Set.of(entry.getKey()));
                    }
                }).accept(connectionMap);
            });

            connectionMap.forEach((manager, ts) -> {
                var detached = manager.detachCopyAll(ts);
                idSetter.setRealIds(copyOf(ts), copyOf(detached));
                result.addAll(detached);
            });

            commitTransaction(managerSet);
            return result;
        } catch (Throwable t) {
            rollbackTransaction(managerSet);
            throw t;
        }
    }

    private static <T extends PersistableObject> List<T> insert(Map<JDOPersistenceManager, List<T>> connectionMap) {
        var managerSet = connectionMap.keySet();
        openTransaction(managerSet);

        try {
            var result = new ListOfPersistentObjects<T>() {
                public String toString() {
                    var resultStr = format("%s inserted object/objects", size());
                    var tableList = stream().map(PersistableObject::fromTable)
                            .filter(StringUtils::isNotBlank)
                            .distinct()
                            .collect(toList());

                    if (tableList.size() > 0) {
                        resultStr = format("%s of table/tables %s", resultStr, join(",", tableList));
                    }
                    return resultStr;
                }
            };

            var idSetter = new IdSetter() {
            };
            connectionMap.forEach((manager, toBeInserted) -> {
                var persistent = manager.makePersistentAll(toBeInserted);
                var detached = manager.detachCopyAll(persistent);
                idSetter.setRealIds(copyOf(persistent), copyOf(detached));
                result.addAll(detached);
            });

            if (managerSet.size() > 1) {
                preCommit(managerSet);
            }
            commitTransaction(managerSet);
            return result;
        } catch (Throwable t) {
            rollbackTransaction(managerSet);
            throw t;
        }
    }

    private static <T extends PersistableObject> List<T> delete(Map<JDOPersistenceManager, List<T>> connectionMap) {
        var managerSet = connectionMap.keySet();
        openTransaction(managerSet);

        try {
            var result = new ListOfPersistentObjects<T>() {
                public String toString() {
                    return format("%s deleted object/objects", size());
                }
            };

            connectionMap.forEach((manager, ts) -> {
                manager.deletePersistentAll(ts);
                ts.forEach(o -> result.add((T) o.clone()));
            });

            if (managerSet.size() > 1) {
                preCommit(managerSet);
            }
            commitTransaction(managerSet);
            return result;
        } catch (Throwable t) {
            rollbackTransaction(managerSet);
            throw t;
        }
    }

    private static <T extends PersistableObject> Map<JDOPersistenceManager, List<T>> getMap(DataBaseStepContext context,
                                                                                            Collection<T> toBeOperated) {
        var result = new LinkedHashMap<JDOPersistenceManager, List<T>>();

        toBeOperated.forEach(t -> {
            var manager = context.getManager(getConnection(t.getClass()));
            ofNullable(result.get(manager))
                    .ifPresentOrElse(ts -> ts.add(t),
                            () -> result.put(manager, new ArrayList<>(List.of(t))));
        });
        return result;
    }

    private static void openTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            transaction.setOptimistic(true);
            transaction.begin();
        });
    }

    private static void commitTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager ->
                jdoPersistenceManager
                        .currentTransaction()
                        .commit());
    }

    private static void rollbackTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        });
    }

    private static void preCommit(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager ->
                ((ExecutionContextImpl) jdoPersistenceManager
                        .getExecutionContext())
                        .preCommit());
    }
}
