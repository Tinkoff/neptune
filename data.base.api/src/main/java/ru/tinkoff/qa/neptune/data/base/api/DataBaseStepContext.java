package ru.tinkoff.qa.neptune.data.base.api;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.core.api.cleaning.Stoppable;
import ru.tinkoff.qa.neptune.core.api.steps.context.CreateWith;
import ru.tinkoff.qa.neptune.core.api.steps.context.GetStepContext;
import ru.tinkoff.qa.neptune.core.api.steps.context.ProviderOfEmptyParameters;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnection;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.InnerJDOPersistenceManagerFactory;
import ru.tinkoff.qa.neptune.data.base.api.data.operations.UpdateExpression;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectASingle;
import ru.tinkoff.qa.neptune.data.base.api.queries.SelectList;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Collections.synchronizedSet;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.data.base.api.data.operations.DataOperation.*;

@CreateWith(provider = ProviderOfEmptyParameters.class)
public class DataBaseStepContext implements GetStepContext<DataBaseStepContext>, Stoppable {

    private final Set<JDOPersistenceManager> jdoPersistenceManagerSet = synchronizedSet(new HashSet<>());

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
            var newManager = (JDOPersistenceManager) connection.getConnectionFactory().getPersistenceManager();
            newManager.getFetchPlan().setMaxFetchDepth(100000000);
            jdoPersistenceManagerSet.add(newManager);
            return newManager;
        });
    }

    @Override
    public void stop() {
        jdoPersistenceManagerSet.forEach(jdoPersistenceManager -> {
            jdoPersistenceManager.getPersistenceManagerFactory().close();
            jdoPersistenceManager.close();
        });
    }

    public <T> List<T> select(SelectList<T, ?> selectList) {
        return get(selectList);
    }

    public <T> T select(SelectASingle<T, ?> selectOne) {
        return get(selectOne);
    }

    public <T extends PersistableObject> List<T> update(SelectASingle<T, ?> howToSelect, UpdateExpression<T> set) {
        return get(updated(howToSelect, set));
    }

    public <T extends PersistableObject> List<T> update(SelectList<T, ?> howToSelect, UpdateExpression<T> set) {
        return get(updated(howToSelect, set));
    }

    public <T extends PersistableObject> List<T> update(Collection<T> toUpdate, UpdateExpression<T> set) {
        return get(updated(toUpdate, set));
    }

    public <T extends PersistableObject> List<T> update(T toUpdate, UpdateExpression<T> set) {
        return get(updated(toUpdate, set));
    }

    public <T extends PersistableObject> List<T> delete(SelectASingle<T, ?> howToSelect) {
        return get(deleted(howToSelect));
    }

    public <T extends PersistableObject> List<T> delete(SelectList<T, ?> howToSelect) {
        return get(deleted(howToSelect));
    }

    public <T extends PersistableObject> List<T> delete(Collection<T> toDelete) {
        return get(deleted(toDelete));
    }

    public <T extends PersistableObject> List<T> delete(T... toDelete) {
        return get(deleted(toDelete));
    }

    public <T extends PersistableObject> List<T> insert(Collection<T> toInsert) {
        return get(inserted(toInsert));
    }

    public <T extends PersistableObject> List<T> insert(T... toInsert) {
        return get(inserted(toInsert));
    }
}
