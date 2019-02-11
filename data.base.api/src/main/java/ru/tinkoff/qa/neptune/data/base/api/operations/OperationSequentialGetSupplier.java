package ru.tinkoff.qa.neptune.data.base.api.operations;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

class OperationSequentialGetSupplier<T extends PersistableObject> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<JDOPersistenceManager, List<T>, Transaction, T, OperationSequentialGetSupplier<T>> {

    OperationSequentialGetSupplier(String description, BiFunction<Collection<T>, PersistenceManager, List<T>> getResult,
                                   Collection<T> toBeOperated) {
        super(description, transaction -> {
            var manager = transaction.getPersistenceManager();
            try {
                transaction.setOptimistic(true);
                transaction.begin();
                return getResult.apply(toBeOperated, manager);
            } catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
    }

    OperationSequentialGetSupplier(String description, BiFunction<Collection<T>, PersistenceManager, List<T>> getResult,
                                   SelectOneGetSupplier<T, ?> toBeOperated) {
        super(description, transaction -> {
            var manager = (JDOPersistenceManager) transaction.getPersistenceManager();
            try {
                transaction.setOptimistic(true);
                transaction.begin();
                var result = toBeOperated.get().apply(manager);
                return getResult.apply(ofNullable(result)
                        .map(List::of).orElseGet(List::of),
                        manager);
            } catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
    }

    OperationSequentialGetSupplier(String description, BiFunction<Collection<T>, PersistenceManager, List<T>> getResult,
                                   SelectListGetSupplier<T, ?> toBeOperated) {
        super(description, transaction -> {
            var manager = (JDOPersistenceManager) transaction.getPersistenceManager();
            try {
                transaction.setOptimistic(true);
                transaction.begin();
                var result = toBeOperated.get().apply(manager);
                return getResult.apply(result, manager);
            } catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
    }
}
