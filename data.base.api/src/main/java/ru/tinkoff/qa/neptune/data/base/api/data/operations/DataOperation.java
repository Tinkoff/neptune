package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.ListOfDataBaseObjects;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.*;
import java.util.function.Function;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionToUse.ConnectionDataReader.getConnection;

public final class DataOperation<T extends PersistableObject>  extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<DataBaseStepContext, List<T>, Map<JDOPersistenceManager, List<T>>, T, DataOperation<T>> {

    private DataOperation(String description, Function<Map<JDOPersistenceManager, List<T>>, List<T>> originalFunction) {
        super(description, originalFunction);
    }

    private static <T extends PersistableObject> List<T> update(Map<JDOPersistenceManager, List<T>> connectionMap, UpdateExpression<T> set) {
        var managerSet = connectionMap.keySet();
        openTransaction(managerSet);

        try {
            var result = new ListOfDataBaseObjects<T>() {
                public String toString() {
                    return format("%s updated object/objects", size());
                }
            };

            var toBeUpdated = new ArrayList<T>();
            connectionMap.forEach((ignored, value) -> toBeUpdated.addAll(value));
            set.getUpdateAction().forEach(setAction -> setAction.accept(toBeUpdated));
            result.addAll(toBeUpdated);
            commitTransaction(managerSet);

            return result;
        }
        catch (Throwable t) {
            rollbackTransaction(managerSet);
            throw t;
        }
    }

    private <T extends PersistableObject> List<T> insert(Map<JDOPersistenceManager, List<T>> connectionMap) {
        var managerSet = connectionMap.keySet();
        openTransaction(managerSet);

        try {
            var result = new ListOfDataBaseObjects<T>() {
                public String toString() {
                    return format("%s inserted object/objects", size());
                }
            };

            connectionMap.forEach((manager, toBeInserted) ->
                    result.addAll(manager.makePersistentAll(toBeInserted)));
            commitTransaction(managerSet);
            return result;
        }
        catch (Throwable t) {
            rollbackTransaction(managerSet);
            throw t;
        }
    }

    private static <T extends PersistableObject> Map<JDOPersistenceManager, List<T>> getMap(DataBaseStepContext context, List<T> toBeOperated) {
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
        jdoPersistenceManagers.forEach(jdoPersistenceManager -> {
            jdoPersistenceManager.currentTransaction().commit();
        });
    }

    private static void rollbackTransaction(Set<JDOPersistenceManager> jdoPersistenceManagers) {
        jdoPersistenceManagers.forEach(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            if (transaction.isActive()) {
                transaction.rollback();
            }
        });
    }
}
