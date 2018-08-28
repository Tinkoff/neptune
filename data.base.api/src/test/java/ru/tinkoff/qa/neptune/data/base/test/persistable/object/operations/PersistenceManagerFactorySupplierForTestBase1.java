package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.datanucleus.metadata.PersistenceUnitMetaData;
import org.sqlite.JDBC;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceUnit;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest.TEST_BASE;
import static ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations.BaseDbOperationTest.testDB1;

@PersistenceUnit(name = TEST_BASE)
public class PersistenceManagerFactorySupplierForTestBase1 extends PersistenceManagerFactorySupplier {

    @Override
    protected PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled) {
        toBeFilled.addProperty("datanucleus.ConnectionDriverName", JDBC.class.getName());
        toBeFilled.addProperty("datanucleus.identifier.case", "MixedCase");
        toBeFilled.addProperty("datanucleus.ConnectionURL", format("jdbc:sqlite:%s", testDB1.getAbsolutePath()));
        return toBeFilled;
    }
}
