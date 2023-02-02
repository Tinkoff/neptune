package ru.tinkoff.qa.neptune.core.api.agent.testng;

import org.testng.ISuite;
import org.testng.ISuiteListener;

import static ru.tinkoff.qa.neptune.core.api.agent.NeptuneRuntimeAgentStarter.runAgent;

public final class AgentStartingSuiteListener implements ISuiteListener {

    static {
        runAgent();
    }

    @Override
    public void onStart(ISuite suite) {
    }
}
