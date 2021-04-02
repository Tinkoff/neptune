package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.ResultPersistentManager;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;

/**
 * This class is designed to builds step-functions that perform the deleting and return a result
 *
 * @param <T> is a type of objects to be deleted
 * @param <R> is a type of subclass of {@link DeleteOperation}
 */
@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
@SuppressWarnings("unchecked")
public abstract class DeleteOperation<T extends PersistableObject, R extends DeleteOperation<T, R>> extends DataOperation<T, R> {

    DeleteOperation() {
        super(jdoPersistenceManagerListMap -> {
            var managerSet = jdoPersistenceManagerListMap.keySet();
            openTransaction(managerSet);

            try {
                var result = new ListOfPersistentObjects<T>() {
                    public String toString() {
                        return format("%s deleted object/objects", size());
                    }
                };

                jdoPersistenceManagerListMap.forEach((manager, ts) -> {
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
        });
    }

    static final class DeleteSelected<T extends PersistableObject> extends DeleteOperation<T, DeleteOperation.DeleteSelected<T>> {

        @StepParameter("Objects to be deleted")
        private final List<T> toDelete;

        DeleteSelected(Collection<T> toBeDeleted) {
            super();
            checkArgument(nonNull(toBeDeleted),
                    "Collection of objects to be deleted should be defined as a value that differs from null");

            toDelete = toBeDeleted
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(toList());
            from(context -> getMap(context, toDelete));
        }
    }

    static final class DeleteBySelection<T extends PersistableObject> extends DeleteOperation<T, DeleteOperation.DeleteBySelection<T>> {

        private static final ResultPersistentManager RESULT_PERSISTENT_MANAGER = new ResultPersistentManager() {
        };

        final SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?> howToSelect;

        private DeleteBySelection(SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?> howToSelect) {
            super();
            checkNotNull(howToSelect);
            this.howToSelect = howToSelect;
        }

        DeleteBySelection(SelectList<?, List<T>> howToSelect) {
            this((SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?>) howToSelect);
            from(context -> {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                return getMap(context, context.select(howToSelect));
            });
        }

        DeleteBySelection(SelectASingle<T> howToSelect) {
            this((SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?>) howToSelect);
            from(context -> {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                var result = context.select(howToSelect);
                var list = ofNullable(result).map(List::of).orElseGet(List::of);
                return getMap(context, list);
            });
        }
    }
}
