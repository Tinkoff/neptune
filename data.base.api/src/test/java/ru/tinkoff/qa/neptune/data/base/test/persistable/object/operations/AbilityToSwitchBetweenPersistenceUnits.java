package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;

public class AbilityToSwitchBetweenPersistenceUnits extends BaseDbOperationTest {

    @Test
    public void checkDefaultPersistenceManager() {
        assertThat(dataBaseSteps.getCurrentFactory().getName(),
                is(PersistenceManagerFactorySupplierForTestBase1.class.getName()));
    }

    @Test(dependsOnMethods = "checkDefaultPersistenceManager")
    public void checkAbilityToSwitchBetweenPersistenceManagersByClass() {
        try {
            assertThat(dataBaseSteps.switchTo(PersistenceManagerFactorySupplierForTestBase2.class)
                    .getCurrentFactory().getName(),
                    is(PersistenceManagerFactorySupplierForTestBase2.class.getName()));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test(dependsOnMethods = "checkAbilityToSwitchBetweenPersistenceManagersByClass")
    public void checkAbilityToSwitchToDefaultPersistenceManager() {
        try {
            dataBaseSteps.switchTo(PersistenceManagerFactorySupplierForTestBase2.class);
            assertThat(dataBaseSteps.switchToDefault().getCurrentFactory().getName(),
                    is(PersistenceManagerFactorySupplierForTestBase1.class.getName()));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test(dependsOnMethods = "checkAbilityToSwitchToDefaultPersistenceManager")
    public void checkAbilityToSwitchToPersistenceManagersByItself() {
        try {
            assertThat(dataBaseSteps.switchTo(
                    getPersistenceManagerFactory(PersistenceManagerFactorySupplierForTestBase2.class, true))
                            .getCurrentFactory().getName(),
                    is(PersistenceManagerFactorySupplierForTestBase2.class.getName()));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }
}
