package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore;

import java.util.List;

import static java.util.Arrays.asList;
import static java.util.List.of;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static ru.tinkoff.qa.neptune.core.api.concurrency.ObjectContainer.getAllObjects;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.Junit5RefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore.*;

public class Junit5TestFinishingTest {

    @DataProvider
    public static Object[][] data() {
        return new Object[][]{
                {null, 8},
                {of(ALL_STARTING), 2},
                {of(EACH_STARTING), 8},
                {of(TEST_STARTING), 8},
                {asList(RefreshEachTimeBefore.values()), 9}

        };
    }

    private void runBeforeTheChecking() {
        ContextClass2.refreshCountToZero();
        var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(Junit5Suite.class))
                .build();
        var launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request);
    }

    @Test(dataProvider = "data", groups = "refresh")
    public void refreshTest(List<RefreshEachTimeBefore> strategies, int expected) {
        REFRESH_STRATEGY_PROPERTY.accept(strategies);
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(expected));
    }

    @AfterGroups(groups = "refresh")
    public void afterRefreshTest() {
        REFRESH_STRATEGY_PROPERTY.accept(null);
    }

    @Test
    public void instanceCountTest() {
        runBeforeTheChecking();
        assertThat(getAllObjects(ContextClass2.class, objectContainer -> true), hasSize(7));
        assertThat(getAllObjects(ContextClass1.class, objectContainer -> true), hasSize(7));
    }

    @Test
    public void hookTest() {
        TestHook.count = 0;
        runBeforeTheChecking();
        assertThat(TestHook.count, is(24));
    }
}
