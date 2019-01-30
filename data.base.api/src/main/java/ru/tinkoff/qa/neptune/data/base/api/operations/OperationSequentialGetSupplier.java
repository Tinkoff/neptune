package ru.tinkoff.qa.neptune.data.base.api.operations;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DBSequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.query.SelectSequentialGetStepSupplier;

import javax.jdo.PersistenceManager;
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
        extends DBSequentialGetStepSupplier<List<T>, List<T>, R> {

    private final Function<DataBaseStepContext, DataBaseStepContext> precondition = new Function<>() {
        @Override
        public DataBaseStepContext apply(DataBaseStepContext dataBaseStepContext) {
            manager = dataBaseStepContext.getCurrentPersistenceManager();
            return dataBaseStepContext;
        }
    };

    private final String description;
    PersistenceManager manager;

    OperationSequentialGetSupplier(String description, List<T> toBeOperated) {
        this.description = description;
        from(precondition.andThen(dataBaseStepContext -> toBeOperated));
    }

    @SuppressWarnings("unchecked")
    OperationSequentialGetSupplier(String description, SelectSequentialGetStepSupplier<?,?,?> select) {
        this.description = description;
        from(precondition.andThen(dataBaseStepContext -> {
            var result = dataBaseStepContext.get(select);
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
        }));
    }

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
    protected Function<List<T>, List<T>> getEndFunction() {
        return toGet(description, list -> {
            var transaction = manager.currentTransaction();
            try {
                transaction.setOptimistic(true);
                transaction.begin();
                var toBeReturned = getResult(manager, list);
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
