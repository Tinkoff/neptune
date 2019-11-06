package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.sqlite.JDBC;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest.testDB1;

public class ConnectionDataSupplierForTestBase1 extends DBConnectionSupplier {

    @Override
    protected PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled) {
        toBeFilled.addProperty("datanucleus.ConnectionDriverName", JDBC.class.getName());
        toBeFilled.addProperty("datanucleus.identifier.case", "MixedCase");
        toBeFilled.addProperty("datanucleus.ConnectionURL", format("jdbc:sqlite:%s?foreign_keys=on;", testDB1.getAbsolutePath()));
        return toBeFilled;
    }
}
