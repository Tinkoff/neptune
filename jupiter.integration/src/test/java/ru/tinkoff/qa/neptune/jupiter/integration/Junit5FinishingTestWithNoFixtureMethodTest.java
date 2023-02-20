package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.Junit5RefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore.*;

public class Junit5FinishingTestWithNoFixtureMethodTest {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
            {null, 3},
            {of(ALL_STARTING), 0},
            {of(EACH_STARTING), 0},
            {of(TEST_STARTING), 3},
            {asList(RefreshEachTimeBefore.values()), 3}

        };
    }

    private void runBeforeTheChecking() {
        ContextClass2.refreshCountToZero();
        var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(TestWithoutFixtureMethod.class))
                .build();
        var launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request);
    }

    @Test(dataProvider = "data")
    public void refreshTest(List<RefreshEachTimeBefore> strategies, int expected) {
        REFRESH_STRATEGY_PROPERTY.accept(strategies);
        getCreatedContextOrCreate(ContextClass2.class);
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(expected));
    }

    @AfterMethod
    public void afterRefreshTest() {
        REFRESH_STRATEGY_PROPERTY.accept(null);
    }
}
