package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.Context;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.InnerJDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;

import java.time.Duration;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.synchronizedSet;
import static java.util.List.of;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.data.operations.DataOperation.*;

@SuppressWarnings("unchecked")
public class DataBaseStepContext extends Context<DataBaseStepContext> implements Stoppable {

    private static final DataBaseStepContext context = getInstance(DataBaseStepContext.class);
    private final Set<JDOPersistenceManager> jdoPersistenceManagerSet = synchronizedSet(new HashSet<>());

    private static  <T> T returnSingleWhenNecessary(List<T> ts) {
        if (ts == null || ts.size() == 0) {
            return null;
        }
        return ts.get(0);
    }

    public static DataBaseStepContext inDataBase() {
        return context;
    }

    public JDOPersistenceManager getManager(DBConnection connection) {
        checkArgument(nonNull(connection), "DB connection should not be null-value");
        var manager = jdoPersistenceManagerSet
                .stream()
                .filter(jdoPersistenceManager -> !jdoPersistenceManager.isClosed()
                        && ((InnerJDOPersistenceManagerFactory) jdoPersistenceManager.getPersistenceManagerFactory())
                        .getConnection() == connection)
                .findFirst()
                .orElse(null);

        return  ofNullable(manager).orElseGet(() -> {
            var newManager = (JDOPersistenceManager) connection.getPersistenceManager();
            jdoPersistenceManagerSet.add(newManager);
            return newManager;
        });
    }

    @Override
    public void stop() {
        jdoPersistenceManagerSet.forEach(JDOPersistenceManager::close);
    }

    public final <T, R extends List<T>> R select(SelectList<?, R> selectList) {
        return get(selectList);
    }

    public final <T> T select(SelectASingle<T> selectOne) {
        return get(selectOne);
    }

    @SafeVarargs
    public final <T extends PersistableObject> T update(SelectASingle<T> howToSelect, UpdateExpression<T>... set) {
        return (T) returnSingleWhenNecessary(get(updated(howToSelect, set)));
    }

    @SafeVarargs
    public final <T extends PersistableObject> List<T> update(SelectList<?, List<T>> howToSelect, UpdateExpression<T>... set) {
        return (List<T>) get(updated(howToSelect, set));
    }

    @SafeVarargs
    public final <T extends PersistableObject> List<T> update(Collection<T> toUpdate, UpdateExpression<T>... set) {
        return (List<T>) get(updated(toUpdate, set));
    }

    @SafeVarargs
    public final <T extends PersistableObject> T update(T t, UpdateExpression<T>... set) {
        return returnSingleWhenNecessary(update(ofNullable(t).map(List::of).orElse(null), set));
    }

    public final <T extends PersistableObject> T delete(SelectASingle<T> howToSelect) {
        return (T) returnSingleWhenNecessary(get(deleted(howToSelect)));
    }

    public final <T extends PersistableObject> List<T> delete(SelectList<T, List<T>> howToSelect) {
        return (List<T>) get(deleted(howToSelect));
    }

    public final <T extends PersistableObject> List<T> delete(Collection<T> toDelete) {
        return (List<T>) get(deleted(toDelete));
    }

    @SafeVarargs
    public final <T extends PersistableObject> List<T> delete(T... toDelete) {
        return delete(ofNullable(toDelete).map(List::of).orElse(null));
    }

    public final <T extends PersistableObject> T delete(T toDelete) {
        checkArgument(nonNull(toDelete), "Object to be deleted should be defined as a value that differs from null");
        return returnSingleWhenNecessary(delete(of(toDelete)));
    }

    public final <T extends PersistableObject> List<T> insert(Collection<T> toInsert) {
        return get(inserted(toInsert));
    }

    @SafeVarargs
    public final <T extends PersistableObject> List<T> insert(T... toInsert) {
        return insert(ofNullable(toInsert).map(List::of).orElse(null));
    }

    public final <T extends PersistableObject> T insert(T toInsert) {
        checkArgument(nonNull(toInsert), "Object to be inserted should be defined as a value that differs from null");
        return returnSingleWhenNecessary(insert(of(toInsert)));
    }

    public final boolean presenceOf(SelectList<?, ?> selectList) {
        return super.presenceOf(selectList);
    }

    public final boolean presenceOf(SelectASingle<?> selectOne) {
        return super.presenceOf(selectOne);
    }

    public final boolean presenceOfOrThrow(SelectList<?, ?> selectList) {
        return super.presenceOfOrThrow(selectList);
    }

    public final boolean presenceOfOrThrow(SelectASingle<?> selectOne) {
        return super.presenceOfOrThrow(selectOne);
    }

    public final boolean absenceOf(SelectList<?, ?> selectList, Duration timeOut) {
        return super.absenceOf(selectList, timeOut);
    }

    public final boolean absenceOfOrThrow(SelectList<?, ?> selectList, Duration timeOut) {
        return super.absenceOfOrThrow(selectList, timeOut);
    }

    public final boolean absenceOf(SelectASingle<?> selectOne, Duration timeOut) {
        return super.absenceOf(selectOne, timeOut);
    }

    public final boolean absenceOfOrThrow(SelectASingle<?> selectOne, Duration timeOut) {
        return super.absenceOfOrThrow(selectOne, timeOut);
    }
}
