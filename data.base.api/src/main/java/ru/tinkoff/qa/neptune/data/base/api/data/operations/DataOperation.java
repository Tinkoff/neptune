package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.*;
import java.util.function.Function;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionToUse.ConnectionDataReader.getConnection;

public final class DataOperation<T extends PersistableObject>  extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<DataBaseStepContext, List<T>, Map<JDOPersistenceManager, List<T>>, T, DataOperation<T>> {

    private DataOperation(String description, Function<Map<JDOPersistenceManager, List<T>>, List<T>> originalFunction) {
        super(description, originalFunction);
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
