package ru.tinkoff.qa.neptune.data.base.api.queries.ids;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.IdSetter;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;
import ru.tinkoff.qa.neptune.data.base.api.queries.KeepResultPersistent;
import ru.tinkoff.qa.neptune.data.base.api.result.ListOfPersistentObjects;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;

/**
 * This class constructs a query to select stored objects by known ids.
 * @param <T> is a type of {@link PersistableObject} to be selected
 */
final class IdQuery<T extends PersistableObject> implements Function<JDOPersistenceManager, List<T>>, IdSetter {

    private final Class<T> classOfRequestedValue;
    private final Object[] ids;
    private final KeepResultPersistent keepResultPersistent;

    IdQuery(Class<T> classOfRequestedValue, Object[] ids, KeepResultPersistent keepResultPersistent) {
        this.keepResultPersistent = keepResultPersistent;
        checkNotNull(classOfRequestedValue, "A class of selected objects should be defined");
        checkNotNull(ids, "Ids should be defined as a value that differs from null");
        checkArgument(ids.length > 0, "At least one object Id should be defined");
        this.classOfRequestedValue = classOfRequestedValue;
        this.ids = ids;
    }

    @Override
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {

        var list = new ArrayList<T>();
        stream(ids).forEach(id -> {
            try {
                var p = jdoPersistenceManager.getObjectById(classOfRequestedValue, id);
                list.add(p);
            }
            catch (RuntimeException ignored) {
            }
        });

        ListOfPersistentObjects<T> found;

        if (!keepResultPersistent.toKeepOnPersistent()) {
            found = new ListOfPersistentObjects<>((jdoPersistenceManager.detachCopyAll(list))) {
            };
            setRealIds(list, found);
        }
        else {
            found = new ListOfPersistentObjects<>(list) {
            };
        }

        return found;
    }
}
