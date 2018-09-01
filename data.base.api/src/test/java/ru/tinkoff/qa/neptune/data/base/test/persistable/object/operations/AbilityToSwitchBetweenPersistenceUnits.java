package ru.tinkoff.qa.neptune.data.base.test.persistable.object.operations;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.data.base.api.persistence.data.PersistenceManagerFactoryStore.getPersistenceManagerFactory;

public class AbilityToSwitchBetweenPersistenceUnits extends BaseDbOperationTest {

    @Test
    public void checkDefaultPersistenceManager() {
        assertThat(dataBaseSteps.getCurrentFactory().getName(), is(TEST_BASE));
    }

    @Test(dependsOnMethods = "checkDefaultPersistenceManager")
    public void checkAbilityToSwitchBetweenPersistenceManagersByName() {
        try {
            assertThat(dataBaseSteps.switchTo(TEST_BASE2).getCurrentFactory().getName(), is(TEST_BASE2));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test(dependsOnMethods = "checkAbilityToSwitchBetweenPersistenceManagersByName")
    public void checkAbilityToSwitchToDefaultPersistenceManager() {
        try {
            dataBaseSteps.switchTo(TEST_BASE2);
            assertThat(dataBaseSteps.switchToDefault().getCurrentFactory().getName(), is(TEST_BASE));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }

    @Test(dependsOnMethods = "checkAbilityToSwitchToDefaultPersistenceManager")
    public void checkAbilityToSwitchToPersistenceManagersByItself() {
        try {
            assertThat(dataBaseSteps.switchTo(getPersistenceManagerFactory(TEST_BASE2, true))
                    .getCurrentFactory().getName(), is(TEST_BASE2));
        }
        finally {
            dataBaseSteps.switchToDefault();
        }
    }
}
