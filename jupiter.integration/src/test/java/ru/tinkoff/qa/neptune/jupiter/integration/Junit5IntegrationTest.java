package ru.tinkoff.qa.neptune.jupiter.integration;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.testng.annotations.AfterGroups;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static ru.tinkoff.qa.neptune.core.api.concurrency.ObjectContainer.getAllObjects;
import static ru.tinkoff.qa.neptune.core.api.steps.context.ContextFactory.getCreatedContextOrCreate;
import static ru.tinkoff.qa.neptune.jupiter.integration.properties.Junit5RefreshStrategyProperty.REFRESH_STRATEGY_PROPERTY;

public abstract class Junit5IntegrationTest {

    private final Class<? extends BaseJunit5IntegrationTest> toRun;
    private final Object[][] expectedRefreshInvocations;
    private final int expectedContextCount;
    private final int expectedHookInvocations;

    public Junit5IntegrationTest(Class<? extends BaseJunit5IntegrationTest> toRun,
                                 Object[][] expectedRefreshInvocations,
                                 int expectedContextCount,
                                 int expectedHookInvocations) {
        this.toRun = toRun;
        this.expectedRefreshInvocations = expectedRefreshInvocations;
        this.expectedContextCount = expectedContextCount;

        this.expectedHookInvocations = expectedHookInvocations;
    }

    private void runBeforeTheChecking() {
        ContextClass2.refreshCountToZero();
        var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(toRun))
                .configurationParameter("junit.jupiter.execution.parallel.enabled", "true")
                .configurationParameter("junit.jupiter.execution.parallel.mode.default", "same_thread")
                .build();
        var launcher = LauncherFactory.create();
        launcher.discover(request);
        launcher.execute(request);
    }

    @DataProvider
    public Object[][] data() {
        return expectedRefreshInvocations;
    }

    @Test(dataProvider = "data", groups = "refresh")
    public void refreshTest(List<RefreshEachTimeBefore> strategies, int expected) {
        REFRESH_STRATEGY_PROPERTY.accept(strategies);
        getCreatedContextOrCreate(ContextClass2.class);
        getCreatedContextOrCreate(ContextClass1.class);
        runBeforeTheChecking();
        assertThat(ContextClass2.getRefreshCount(), is(expected));
    }

    @AfterGroups(groups = "refresh")
    public void afterRefreshTest() {
        REFRESH_STRATEGY_PROPERTY.accept(null);
    }

    @Test
    public void hookTest() {
        TestHook.count = 0;
        runBeforeTheChecking();
        assertThat(TestHook.count, is(expectedHookInvocations));
    }

    @Test(dependsOnMethods = {"refreshTest", "hookTest"})
    public void instanceCountTest() {
        assertThat(getAllObjects(ContextClass2.class, objectContainer -> true), hasSize(expectedContextCount));
        assertThat(getAllObjects(ContextClass1.class, objectContainer -> true), hasSize(expectedContextCount));
    }
}
