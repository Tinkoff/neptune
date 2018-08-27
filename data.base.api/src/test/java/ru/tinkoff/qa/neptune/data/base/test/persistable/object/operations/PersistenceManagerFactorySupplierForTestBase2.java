package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.sqlite.JDBC;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceUnit;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest.*;

@PersistenceUnit(name = TEST_BASE2)
public class PersistenceManagerFactorySupplierForTestBase2 extends PersistenceManagerFactorySupplier {

    @Override
    protected PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled) {
        toBeFilled.addProperty("datanucleus.Mapping", "sqlite");
        toBeFilled.addProperty("datanucleus.ConnectionDriverName", JDBC.class.getName());
        toBeFilled.addProperty("datanucleus.identifier.case", "MixedCase");
        toBeFilled.addProperty("datanucleus.ConnectionURL", format("jdbc:sqlite:%s", testDB2.getAbsolutePath()));
        return toBeFilled;
    }
}
