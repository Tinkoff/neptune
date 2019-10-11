package ru.tinkoff.qa.neptune.data.base.api.operations;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectListGetSupplier;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectOneGetSupplier;

import java.util.Collection;
import java.util.List;
import java.util.function.BiFunction;

import static java.util.Optional.ofNullable;

@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
@Deprecated
class OperationSequentialGetSupplier<T extends PersistableObject> extends SequentialGetStepSupplier
        .GetIterableChainedStepSupplier<JDOPersistenceManager, List<T>, Transaction, T, OperationSequentialGetSupplier<T>> {

    OperationSequentialGetSupplier(String description, BiFunction<Collection<T>, PersistenceManager, List<T>> getResult,
                                   Collection<T> toBeOperated) {
        super(description, transaction -> {
            var manager = transaction.getPersistenceManager();
            try {
                transaction.begin();
                var toReturn =  getResult.apply(toBeOperated, manager);
                transaction.commit();
                return toReturn;
            } catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
        from(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            transaction.setOptimistic(true);
            return transaction;
        });
    }

    OperationSequentialGetSupplier(String description, BiFunction<Collection<T>, PersistenceManager, List<T>> getResult,
                                   SelectOneGetSupplier<T, ?> toBeOperated) {
        super(description, transaction -> {
            var manager = (JDOPersistenceManager) transaction.getPersistenceManager();
            try {
                transaction.begin();
                var result = toBeOperated.get().apply(manager);
                var toReturn = getResult.apply(ofNullable(result)
                        .map(List::of).orElseGet(List::of),
                        manager);
                transaction.commit();
                return toReturn;
            } catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
        from(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            transaction.setOptimistic(true);
            return transaction;
        });
    }

    OperationSequentialGetSupplier(String description, BiFunction<Collection<T>, PersistenceManager, List<T>> getResult,
                                   SelectListGetSupplier<T, ?> toBeOperated) {
        super(description, transaction -> {
            var manager = (JDOPersistenceManager) transaction.getPersistenceManager();
            try {
                transaction.begin();
                var result = toBeOperated.get().apply(manager);
                var toReturn =  getResult.apply(result, manager);
                transaction.commit();
                return toReturn;
            } catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
        from(jdoPersistenceManager -> {
            var transaction = jdoPersistenceManager.currentTransaction();
            transaction.setOptimistic(true);
            return transaction;
        });
    }
}
