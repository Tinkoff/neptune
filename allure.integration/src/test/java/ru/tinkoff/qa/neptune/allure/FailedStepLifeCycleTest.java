package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.model.Status;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Map;

import static io.qameta.allure.model.Status.BROKEN;
import static io.qameta.allure.model.Status.FAILED;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;

public class FailedStepLifeCycleTest extends AbstractAllurePreparations {

    @DataProvider
    public static Object[][] data1() {
        return new Object[][]{
                {new AssertionError("Test assertion error"), FAILED},
                {new RuntimeException(), BROKEN}
        };
    }

    @Test(dataProvider = "data1")
    public void failedRootStep(Throwable thrown, Status expected) {
        fireEventStarting("Some step", Map.of());
        var stepId = lifeCycle.getCurrentTestCaseOrStep().get();
        var result = storage.getStep(stepId).get();

        fireThrownException(thrown);
        fireEventFinishing();
        assertThat(result.getStatus(), is(expected));
        assertThat(result.getStop(), instanceOf(Long.class));
    }

    @Test(dataProvider = "data1")
    public void failedNestedStep(Throwable thrown, Status expected) {
        fireEventStarting("Some root step", Map.of());
        var rootStepId = lifeCycle.getCurrentTestCaseOrStep().get();
        var rootResult = storage.getStep(rootStepId).get();

        fireEventStarting("Some nested step", Map.of());
        var nestedStepId = lifeCycle.getCurrentTestCaseOrStep().get();
        var nestedResult = storage.getStep(nestedStepId).get();

        fireThrownException(thrown);
        fireEventFinishing();

        assertThat(rootResult.getStatus(), nullValue());
        assertThat(rootResult.getStop(), nullValue());

        assertThat(nestedResult.getStatus(), is(expected));
        assertThat(nestedResult.getStop(), instanceOf(Long.class));
    }

    @Test
    public void failedWhenNoActiveStep() {
        fireThrownException(new AssertionError("Test assertion error"));
        assertThat(lifeCycle.getCurrentTestCaseOrStep().get(), is(testCaseUUID.toString()));
    }
}
