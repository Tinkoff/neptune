package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.apache.commons.lang3.StringUtils;
import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.DefaultReportStepParameterFactory;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.ResultPersistentManager;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import java.util.*;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Arrays.stream;
import static java.util.List.copyOf;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static javax.jdo.JDOHelper.isPersistent;
import static javax.jdo.JDOHelper.isTransactional;
import static ru.tinkoff.qa.neptune.core.api.steps.Step.$;
import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.getTable;

/**
 * This class is designed to builds step-functions that perform the updating and return a result
 *
 * @param <T> is a type of objects to be updated
 * @param <R> is a type of subclass of {@link UpdateOperation}
 */
@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public abstract class UpdateOperation<T extends PersistableObject, R extends UpdateOperation<T, R>> extends DataOperation<T, R> {

    private final UpdateExpression<T>[] updates;

    @SafeVarargs
    UpdateOperation(UpdateExpression<T>... set) {
        super("List of updated objects", jdoPersistenceManagerListMap -> {
            var managerSet = jdoPersistenceManagerListMap.keySet();
            openTransaction(managerSet);

            try {
                var result = new ListOfPersistentObjects<T>() {
                    public String toString() {
                        var resultStr = format("%s updated object/objects", size());
                        var tableList = stream().map(p -> getTable(p.getClass()))
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
        });

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
    protected Map<String, String> getParameters() {
        var result = super.getParameters();
        int i = 0;
        for (UpdateExpression<?> u : updates) {
            var name = i == 0 ? "Update" : "Update" + " " + (i + 1);
            result.put(name, u.toString());
            i++;
        }
        return result;
    }

    static final class UpdateSelected<T extends PersistableObject> extends UpdateOperation<T, UpdateSelected<T>> {

        @StepParameter("Objects to be updated")
        private final List<T> toUpdate;

        @SafeVarargs
        UpdateSelected(Collection<T> toBeUpdated, UpdateExpression<T>... set) {
            super(set);
            var toUpdate = toBeUpdated
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(toList());
            checkArgument(toUpdate.size() > 0,
                    "At least one object to be updated should be defined");

            this.toUpdate = toUpdate;
            from(context -> getMap(context, this.toUpdate));
        }
    }

    static final class UpdateBySelection<T extends PersistableObject> extends UpdateOperation<T, UpdateBySelection<T>> {

        private static final ResultPersistentManager RESULT_PERSISTENT_MANAGER = new ResultPersistentManager() {
        };

        private final SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?> howToSelect;

        @SafeVarargs
        private UpdateBySelection(SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?> howToSelect, UpdateExpression<T>... set) {
            super(set);
            checkNotNull(howToSelect);
            this.howToSelect = howToSelect;
        }

        @SafeVarargs
        UpdateBySelection(SelectList<?, List<T>> howToSelect, UpdateExpression<T>... set) {
            this((SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?>) howToSelect, set);
            from(dataBaseStepContext -> {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                return getMap(dataBaseStepContext, dataBaseStepContext.select(howToSelect));
            });
        }

        @SafeVarargs
        UpdateBySelection(SelectASingle<T> howToSelect, UpdateExpression<T>... set) {
            this((SequentialGetStepSupplier<DataBaseStepContext, ?, ?, ?, ?>) howToSelect, set);
            from(dataBaseStepContext -> {
                RESULT_PERSISTENT_MANAGER.keepResultPersistent(howToSelect);
                var result = dataBaseStepContext.select(howToSelect);
                var list = ofNullable(result).map(List::of).orElseGet(List::of);
                return getMap(dataBaseStepContext, list);
            });
        }

        @Override
        protected Map<String, String> getParameters() {
            var result = super.getParameters();
            result.putAll(DefaultReportStepParameterFactory.getParameters(howToSelect));
            return result;
        }
    }
}
