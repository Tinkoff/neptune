package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionStore.getKnownConnection;

public class ConnectionStoreTest extends BaseDbOperationTest {

    @Test
    public void checkExistingConnectionSupplier() {
        assertThat(getKnownConnection(ConnectionDataSupplierForTestBase1.class, true),
                equalTo(new ConnectionDataSupplierForTestBase1().get()));
        assertThat(getKnownConnection(ConnectionDataSupplierForTestBase2.class, true),
                equalTo(new ConnectionDataSupplierForTestBase2().get()));
    }

    @Test
    public void getNotExistingConnectionSupplierByClass() {
        assertThat(getKnownConnection(DBConnectionSupplier.class, false),
                nullValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "A supplier of DB connection is not found. " +
                    "Expected class: ru.tinkoff.qa.neptune.data.base.api.connection.data.DBConnectionSupplier")
    public void getNotExistingConnectionSupplierWithThrowingException() {
        assertThat(getKnownConnection(DBConnectionSupplier.class, true),
                nullValue());
    }
}
