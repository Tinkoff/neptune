package ru.tinkoff.qa.neptune.core.api.agent.junit.platform;

import org.junit.platform.launcher.TestExecutionListener;
import org.junit.platform.launcher.TestPlan;

import static ru.tinkoff.qa.neptune.core.api.agent.NeptuneRuntimeAgentStarter.runAgent;

public final class AgentStartingTestExecutionListener implements TestExecutionListener {

    static {
        runAgent();
    }

    @Override
    public void testPlanExecutionStarted(TestPlan testPlan) {
    }
}
