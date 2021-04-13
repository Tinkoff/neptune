package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.List.copyOf;
import static java.util.stream.Collectors.toList;

/**
 * This class is designed to builds step-functions that perform the inserting and return a result
 *
 * @param <T> is a type of objects to be inserted
 */
public final class InsertOperation<T extends PersistableObject> extends DataOperation<T, Collection<T>, InsertOperation<T>> {

    @StepParameter("To be inserted")
    private final List<T> toInsert;

    InsertOperation(PersistenceMapWrapper<T> mapWrapper, Collection<T> toBeInserted) {
        super(collection -> {
            var jdoPersistenceManagerListMap = mapWrapper.get();
            var managerSet = jdoPersistenceManagerListMap.keySet();
            openTransaction(managerSet);

            try {
                var result = new ArrayList<T>();
                var idSetter = new IdSetter() {
                };

                jdoPersistenceManagerListMap.forEach((manager, ts) -> {
                    var persistent = manager.makePersistentAll(ts);
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

        }, mapWrapper);

        var toInsert = toBeInserted
                .stream()
                .filter(Objects::nonNull)
                .collect(toList());
        checkArgument(toInsert.size() > 0,
                "At least one object to be inserted should be defined");

        this.toInsert = toInsert;
        from(this.toInsert);
    }
}
