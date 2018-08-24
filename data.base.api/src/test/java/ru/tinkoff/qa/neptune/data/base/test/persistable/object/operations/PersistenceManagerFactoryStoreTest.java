package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactorySuppliers;

public class PersistenceManagerFactoryStoreTest extends BaseDbOperationTest {

    @Test
    public void getPersistenceManagerFactorySuppliersTest() {
        List<PersistenceManagerFactorySupplier> suppliers = getPersistenceManagerFactorySuppliers();
        assertThat(suppliers.stream()
                .map(PersistenceManagerFactorySupplier::name).collect(toList()),
                containsInAnyOrder(TEST_BASE, TEST_BASE2));
    }

    @Test
    public void getExistingPersistenceManagerFactoryByName() {
        assertThat(getPersistenceManagerFactory(TEST_BASE, true).getName(), is(TEST_BASE));
        assertThat(getPersistenceManagerFactory(TEST_BASE2, true).getName(), is(TEST_BASE2));
    }

    @Test
    public void getNotExistingPersistenceManagerFactoryByName() {
        assertThat(getPersistenceManagerFactory("FakeName", false), nullValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "A supplier of persistence manager factories named FakeName has not been found")
    public void getNotExistingPersistenceManagerFactoryByNameWithThrowingException() {
        assertThat(getPersistenceManagerFactory("FakeName", true), nullValue());
    }
}
