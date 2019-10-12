package ru.tinkoff.qa.neptune.data.base.api.queries;

import org.datanucleus.api.jdo.JDOPersistenceManager;
import ru.tinkoff.qa.neptune.data.base.api.DataBaseStepContext;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;

public class JDOPersistenceManagerByConnectionSupplierClass
        implements Function<DataBaseStepContext, JDOPersistenceManager> {

    private final Class<? extends DBConnectionSupplier> clazz;

    public JDOPersistenceManagerByConnectionSupplierClass(Class<? extends DBConnectionSupplier> clazz) {
        checkNotNull(clazz);
        this.clazz = clazz;
    }

    static JDOPersistenceManagerByConnectionSupplierClass getConnectionBySupplierClass(Class<? extends DBConnectionSupplier> clazz) {
        return new JDOPersistenceManagerByConnectionSupplierClass(clazz);
    }

    @Override
    public JDOPersistenceManager apply(DataBaseStepContext dataBaseStepContext) {
        var connection = getKnownConnection(clazz, true);
        return dataBaseStepContext.getManager(connection);
    }
}
