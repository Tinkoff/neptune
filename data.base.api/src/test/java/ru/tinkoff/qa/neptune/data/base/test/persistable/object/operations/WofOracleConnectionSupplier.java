package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import oracle.jdbc.driver.OracleDriver;
import org.datanucleus.metadata.PersistenceUnitMetaData;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import static org.datanucleus.store.rdbms.RDBMSPropertyNames.PROPERTY_RDBMS_ORACLE_DATA_STORE_QUERY_DATE_EXPRESSION;
import static org.datanucleus.store.rdbms.RDBMSPropertyNames.PROPERTY_RDBMS_ORACLE_DATA_STORE_QUERY_DATE_FORMAT;

public class WofOracleConnectionSupplier extends DBConnectionSupplier {

    @Override
    protected PersistenceUnitMetaData fillPersistenceUnit(PersistenceUnitMetaData toBeFilled) {
        toBeFilled.addProperty("datanucleus.ConnectionDriverName", OracleDriver.class.getName());
        toBeFilled.addProperty("datanucleus.ConnectionURL",
                "jdbc:oracle:thin:@(description=(address=(protocol=TCP)(host=db-wof-tst.tcsbank.ru)(port=1522))" +
                        "(connect_data=(service_name=WOF_TST)))");
        toBeFilled.addProperty("datanucleus.ConnectionUserName", "wof_owner");
        toBeFilled.addProperty("datanucleus.ConnectionPassword", "brtlsyubgfertgb_348563");
        toBeFilled.addProperty("datanucleus.rdbms.oracle.nlsSortOrder", "RUSSIAN");
        toBeFilled.addProperty( PROPERTY_RDBMS_ORACLE_DATA_STORE_QUERY_DATE_FORMAT, "dd.MM.yyyy HH:mm:ss");
        toBeFilled.addProperty( PROPERTY_RDBMS_ORACLE_DATA_STORE_QUERY_DATE_EXPRESSION , "TO_DATE('%s', 'DD.MM.YYYY HH24:MI:SS')");
        return toBeFilled;
    }
}
