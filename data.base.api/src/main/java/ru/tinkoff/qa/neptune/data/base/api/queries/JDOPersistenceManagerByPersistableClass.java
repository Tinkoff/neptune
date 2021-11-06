package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.PersistableObject;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.data.base.api.ConnectionDataReader.getConnection;

@Deprecated(forRemoval = true)
class JDOPersistenceManagerByPersistableClass<T extends PersistableObject> implements Function<DataBaseStepContext, JDOPersistenceManager> {

    private final Class<T> clazz;

    private JDOPersistenceManagerByPersistableClass(Class<T> clazz) {
        checkNotNull(clazz);
        this.clazz = clazz;
    }

    static <T extends PersistableObject> JDOPersistenceManagerByPersistableClass<T> getConnectionByClass(Class<T> clazz) {
        return new JDOPersistenceManagerByPersistableClass<>(clazz);
    }

    @Override
    public JDOPersistenceManager apply(DataBaseStepContext dataBaseStepContext) {
        var connection = getConnection(clazz);
        return dataBaseStepContext.getManager(connection);
    }
}
