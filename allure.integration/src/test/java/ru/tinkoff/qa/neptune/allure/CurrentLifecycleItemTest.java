package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.model.FixtureResult;
import io.qameta.allure.model.TestResult;
import io.qameta.allure.model.TestResultContainer;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static io.qameta.allure.Allure.getLifecycle;
import static java.util.UUID.randomUUID;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static ru.tinkoff.qa.neptune.allure.lifecycle.LifeCycleItemItemStorage.*;

public class CurrentLifecycleItemTest {

    private static final TestResultContainer CONTAINER = new TestResultContainer().setUuid(randomUUID().toString()).setName("Root");
    private static final FixtureResult FIXTURE_RESULT = new FixtureResult().setName("Fixture 1");
    private static final TestResult TEST_RESULT = new TestResult().setName("Result 1").setUuid(randomUUID().toString());

    @BeforeClass
    public static void beforeClass() {
        getLifecycle().startTestContainer(CONTAINER);
    }

    @AfterClass
    public static void afterClass() {
        CURRENT_FIXTURE.removeItem();
        CURRENT_TEST_RESULT.removeItem();
        getLifecycle().stopTestContainer(CONTAINER.getUuid());
    }

    @Test
    public void fixtureResultTest() {
        var fixtureUUID = randomUUID().toString();

        getLifecycle().startPrepareFixture(CONTAINER.getUuid(), fixtureUUID, FIXTURE_RESULT);
        assertThat(getCurrentLifecycleItem(), is(FIXTURE_RESULT));

        getLifecycle().stopFixture(fixtureUUID);
        assertThat(getCurrentLifecycleItem(), nullValue());
    }

    @Test(dependsOnMethods = "fixtureResultTest")
    public void testResultTest() {
        getLifecycle().scheduleTestCase(TEST_RESULT);
        assertThat(getCurrentLifecycleItem(), (nullValue()));

        getLifecycle().startTestCase(TEST_RESULT.getUuid());
        assertThat(getCurrentLifecycleItem(), is(TEST_RESULT));

        getLifecycle().stopTestCase(TEST_RESULT.getUuid());
        assertThat(getCurrentLifecycleItem(), nullValue());
    }
}
