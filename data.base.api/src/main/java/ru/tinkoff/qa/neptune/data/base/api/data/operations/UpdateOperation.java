package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.ResultPersistentManager;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.List.copyOf;
import static javax.jdo.JDOHelper.isPersistent;
import static javax.jdo.JDOHelper.isTransactional;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;

/**
 * This class is designed to builds step-functions that perform the updating and return a result
 *
 * @param <T> is a type of objects to be updated
 * @param <R> is a type of subclass of {@link UpdateOperation}
 */
@Deprecated(forRemoval = true)
public abstract class UpdateOperation<T extends PersistableObject, M, R extends UpdateOperation<T, M, R>> extends DataOperation<T, M, R> {

    private final UpdateExpression<T>[] updates;

    @SafeVarargs
    UpdateOperation(PersistenceMapWrapper<T> mapWrapper, UpdateExpression<T>... set) {
        super(m -> {
            var jdoPersistenceManagerListMap = mapWrapper.get();
            var managerSet = jdoPersistenceManagerListMap.keySet();
            openTransaction(managerSet);

            try {
                var result = new ArrayList<T>();
                var idSetter = new IdSetter() {
                };

                stream(set).forEach(setAction ->
                        $(setAction.toString(), () -> {
                            for (var entry : jdoPersistenceManagerListMap.entrySet()) {
                                entry.setValue(makeEveryThingTransientIfNecessary(entry.getKey(), entry.getValue()));
                                setAction.performUpdate(entry.getValue());
                                preCommit(Set.of(entry.getKey()));
                            }
                        }));

                commitTransaction(managerSet);
                jdoPersistenceManagerListMap.forEach((manager, ts) -> {
                    var detached = manager.detachCopyAll(ts);
                    idSetter.setRealIds(copyOf(ts), copyOf(detached));
                    result.addAll(detached);
                });
                return result;
            } catch (Throwable t) {
                rollbackTransaction(managerSet);
                throw t;
            }
        }, mapWrapper);

        checkNotNull(set);
        checkArgument(set.length > 0, "At least one update-action should be defined");
        this.updates = set;
    }

    private static <T extends PersistableObject> List<T> makeEveryThingTransientIfNecessary(JDOPersistenceManager manager, List<T> persistable) {
        var result = new ArrayList<T>();

        persistable.forEach(t -> {
            T t2;
            if (!isPersistent(t)) {
                t2 = manager.makePersistent(t);
            } else {
                t2 = t;
            }

            if (!isTransactional(t2)) {
                manager.makeTransactional(t2);
            }
            result.add(t2);
        });
        return result;
    }

    @Override
    public Map<String, String> getParameters() {
        var result = super.getParameters();
        int i = 0;
        for (UpdateExpression<?> u : updates) {
            var name = i == 0 ? "Update" : "Update" + " " + (i + 1);
            result.put(name, u.toString());
            i++;
        }
        return result;
    }

    static final class UpdateSelected<T extends PersistableObject, M> extends UpdateOperation<T, M, UpdateSelected<T, M>> {

        @StepParameter("To be updated")
        private final M toUpdate;

        @SafeVarargs
        UpdateSelected(PersistenceMapWrapper<T> mapWrapper, M toBeUpdated, UpdateExpression<T>... set) {
            super(mapWrapper, set);
            from(toBeUpdated);
            this.toUpdate = toBeUpdated;
        }
    }

    @IncludeParamsOfInnerGetterStep
    static final class UpdateBySelection<T extends PersistableObject, M> extends UpdateOperation<T, M, UpdateBySelection<T, M>> {

        private static final ResultPersistentManager RESULT_PERSISTENT_MANAGER = new ResultPersistentManager() {
        };

        @SafeVarargs
        UpdateBySelection(PersistenceMapWrapper<T> mapWrapper, SequentialGetStepSupplier<DataBaseStepContext, M, ?, ?, ?> howToSelect, UpdateExpression<T>... set) {
            super(mapWrapper, set);
            from(howToSelect);

            if (howToSelect instanceof SelectList) {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent((SelectList<?, ?>) howToSelect);
            }

            if (howToSelect instanceof SelectASingle) {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent((SelectASingle<?>) howToSelect);
            }
        }
    }
}
