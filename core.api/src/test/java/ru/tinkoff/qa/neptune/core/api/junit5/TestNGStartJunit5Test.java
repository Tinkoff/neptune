package ru.tinkoff.qa.neptune.core.api.junit5;

import org.junit.platform.launcher.core.LauncherDiscoveryRequestBuilder;
import org.junit.platform.launcher.core.LauncherFactory;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.agent.NeptuneRuntimeAgentStarter;

import static org.junit.platform.engine.discovery.DiscoverySelectors.selectClass;
import static org.mockito.Mockito.mockStatic;
import static org.mockito.Mockito.times;

public class TestNGStartJunit5Test {

    @Test
    public void isAgentInvokedTest() {
        try (var starterStatic = mockStatic(NeptuneRuntimeAgentStarter.class)) {
            var request = LauncherDiscoveryRequestBuilder.request()
                .selectors(selectClass(IsAgentLaunchedTest.class))
                .build();
            var launcher = LauncherFactory.create();
            launcher.discover(request);
            launcher.execute(request);

            starterStatic.verify(
                NeptuneRuntimeAgentStarter::runAgent,
                times(1));
        }
    }
}
