package ru.tinkoff.qa.neptune.data.base.api.data.operations;

import org.apache.commons.lang3.StringUtils;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeStringCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import java.util.Collection;
import java.util.List;
import java.util.Objects;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.List.copyOf;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.data.base.api.PersistableObject.getTable;

/**
 * This class is designed to builds step-functions that perform the inserting and return a result
 *
 * @param <T> is a type of objects to be inserted
 */
@MakeFileCapturesOnFinishing
@MakeStringCapturesOnFinishing
public final class InsertOperation<T extends PersistableObject> extends DataOperation<T, InsertOperation<T>> {

    @StepParameter("Objects to be inserted")
    private final List<T> toInsert;

    InsertOperation(Collection<T> toBeInserted) {
        super("List of inserted objects", jdoPersistenceManagerListMap -> {
            var managerSet = jdoPersistenceManagerListMap.keySet();
            openTransaction(managerSet);

            try {
                var result = new ListOfPersistentObjects<T>() {
                    public String toString() {
                        var resultStr = format("%s inserted object/objects", size());
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
        });

        var toInsert = toBeInserted
                .stream()
                .filter(Objects::nonNull)
                .collect(toList());
        checkArgument(toInsert.size() > 0,
                "At least one object to be inserted should be defined");

        this.toInsert = toInsert;
        from(context -> getMap(context, this.toInsert));
    }
}
