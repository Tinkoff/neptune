package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DBSequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectSequentialGetStepSupplier;

import javax.jdo.PersistenceManager;
import javax.jdo.Transaction;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.core.api.steps.StoryWriter.toGet;

/**
 * This is the basic class that builds and supplies functions to return results of INSERT, UPDATE and DELETE operations.
 * Transactions are used to keep data of the data store valid when something is going wrong. INSERT, UPDATE and DELETE
 * operations are supposed to return lists of updated, inserted and deleted objects.
 *
 * @param <T> is a subclass of {@link PersistableObject}. It defines the type of an item of a resulted list
 * @param <R> is a subclass of {@link OperationSequentialGetSupplier}. Requirement of the {@link SequentialGetStepSupplier}
 *           specification. It is needed for the chaining.
 */
abstract class OperationSequentialGetSupplier<T extends PersistableObject, R extends OperationSequentialGetSupplier<T, R>>
        extends DBSequentialGetStepSupplier<List<T>, Transaction, R> {

    private final String description;
    private final Object forTransaction;
    private DataBaseStepContext context;

    private OperationSequentialGetSupplier(String description, Object forTransaction) {
        this.description = description;
        this.forTransaction = forTransaction;
        from(dataBaseStepContext -> {
            context = dataBaseStepContext;
            return dataBaseStepContext.getCurrentPersistenceManager().currentTransaction();
        });
    }

    OperationSequentialGetSupplier(String description, List<T> toBeTransacted) {
        this(description, (Object) toBeTransacted);
    }

    OperationSequentialGetSupplier(String description, SelectSequentialGetStepSupplier<?,?,?> select) {
        this(description, (Object) select);
    }

    @SuppressWarnings("unchecked")
    static <T extends PersistableObject> List<T> getOperationObjects(Collection<T> toBeOperated) {
        checkArgument(nonNull(toBeOperated), "Collection of db objects  should not be null");
        checkArgument(toBeOperated
                .stream()
                .filter(Objects::isNull)
                .collect(toList())
                .size() == 0, format("There are null-values. The collection: %s", toBeOperated));

        return new ArrayList<>(toBeOperated);
    }

    static <T extends PersistableObject> String getDescription(String description, Collection<T> toBeInserted) {
        return description + ofNullable(toBeInserted)
                .map(ts -> ts.stream().map(PersistableObject::toString).collect(joining(",")))
                .orElse(EMPTY);
    }

    abstract List<T> getResult(PersistenceManager persistenceManager, List<T> listOfObjectsForTransaction);

    @Override
    @SuppressWarnings("unchecked")
    protected Function<Transaction, List<T>> getEndFunction() {
        return toGet(description, transaction -> {
            try {
                List<T> transactionList = ofNullable(forTransaction)
                        .map(o -> {
                            var clazz = o.getClass();
                            if (SelectSequentialGetStepSupplier.class.isAssignableFrom(clazz)) {
                                var result = context.get((SelectSequentialGetStepSupplier) o);
                                return ofNullable(result)
                                        .map(resulted -> {
                                            var resultClazz = resulted.getClass();
                                            if (List.class.isAssignableFrom(resultClazz)) {
                                                return (List<T>) resulted;
                                            }

                                            return of(resulted)
                                                    .stream()
                                                    .map(o1 -> (T) o1)
                                                    .collect(toList());
                                        })
                                        .orElse(of());
                            }

                            if (List.class.isAssignableFrom(clazz)) {
                                return (List<T>) o;
                            }

                            return of()
                                    .stream()
                                    .map(o1 -> (T) o1)
                                    .collect(toList());
                        })
                        .orElse(of());

                transaction.setOptimistic(true);
                transaction.begin();
                var toBeReturned = getResult(context.getCurrentPersistenceManager(), transactionList);
                transaction.commit();
                return toBeReturned;
            }
            catch (Throwable t) {
                if (transaction.isActive()) {
                    transaction.rollback();
                }
                throw t;
            }
        });
    }
}
