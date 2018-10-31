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
    public void checkExistingPersistenceManagerFactoryNames() {
        assertThat(getPersistenceManagerFactory(PersistenceManagerFactorySupplierForTestBase1.class, true).getName(),
                is(PersistenceManagerFactorySupplierForTestBase1.class.getName()));
        assertThat(getPersistenceManagerFactory(PersistenceManagerFactorySupplierForTestBase2.class, true).getName(),
                is(PersistenceManagerFactorySupplierForTestBase2.class.getName()));
    }

    @Test
    public void getNotExistingPersistenceManagerFactoryByClass() {
        assertThat(getPersistenceManagerFactory(PersistenceManagerFactorySupplier.class, false),
                nullValue());
    }

    @Test(expectedExceptions = IllegalArgumentException.class,
            expectedExceptionsMessageRegExp = "A supplier of persistence manager factories has not been found. " +
                    "Expected class: ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactorySupplier")
    public void getNotExistingPersistenceManagerFactoryByClassWithThrowingException() {
        assertThat(getPersistenceManagerFactory(PersistenceManagerFactorySupplier.class, true),
                nullValue());
    }
}
