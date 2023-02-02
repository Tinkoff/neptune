package ru.tinkoff.qa.neptune.core.api.junit5;

import org.junit.jupiter.api.Test;
import ru.tinkoff.qa.neptune.core.api.agent.NeptuneRuntimeAgentStarter;

import static org.junit.jupiter.api.Assertions.assertTrue;

class IsAgentLaunchedTest {

    @Test
    void isAgentLaunchedTest() {
        assertTrue(NeptuneRuntimeAgentStarter.isIsLaunched());
    }
}
