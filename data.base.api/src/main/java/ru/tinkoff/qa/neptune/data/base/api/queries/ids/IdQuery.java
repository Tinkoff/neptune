package ru.tinkoff.qa.neptune.data.base.api.queries.ids;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;

public final class IdQuery<T extends PersistableObject> implements Function<JDOPersistenceManager, List<T>> {

    private final Class<T> classOfRequestedValue;
    private final Object[] ids;

    public static <T extends PersistableObject> IdQuery<T> byIds(Class<T> classOfRequestedValue, Object... ids) {
        return new IdQuery<>(classOfRequestedValue, ids);
    }

    private IdQuery(Class<T> classOfRequestedValue, Object[] ids) {
        checkNotNull(classOfRequestedValue);
        checkNotNull(ids);
        checkArgument(ids.length > 0, "Should be defined at least one object Id");
        this.classOfRequestedValue = classOfRequestedValue;
        this.ids = ids;
    }

    @Override
    public List<T> apply(JDOPersistenceManager jdoPersistenceManager) {
        var found = new ArrayList<T>();
        stream(ids).forEach(id -> {
            try {
                var p = jdoPersistenceManager.getObjectById(classOfRequestedValue, id);
                found.add(p);
            }
            catch (RuntimeException ignored) {
            }
        });
        return found;
    }
}
