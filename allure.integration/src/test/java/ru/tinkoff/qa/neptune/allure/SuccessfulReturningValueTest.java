package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;

import static io.qameta.allure.model.Stage.RUNNING;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;

public class SuccessfulReturningValueTest extends AbstractAllurePreparations {


    private final LinkedHashMap<String, String> params = new LinkedHashMap<>() {
        {
            put("test param 1", "value1");
            put("test param 2", "value2");
            put("test param 3", "value3");
            put("test param 4", "value4");
        }
    };

    private String rootStepUUID;
    private String nestedStepUUID;

    @BeforeClass(dependsOnMethods = "beforeClass")
    public void prepareTest() {
        fireEventStarting("Root step", params);
        rootStepUUID = lifeCycle.getCurrentTestCaseOrStep().get();

        fireEventStarting("Nested step", params);
        nestedStepUUID = lifeCycle.getCurrentTestCaseOrStep().get();
    }

    @AfterMethod
    public void finishStep() {
        fireEventFinishing();
    }

    @Test
    public void successfulFinishingOfNestedStep() {
        fireReturnedValue("Returned object", new Object());

        assertThat(storage.getStep(nestedStepUUID).isPresent(), is(true));
        assertThat(lifeCycle.getCurrentTestCaseOrStep().get(), is(nestedStepUUID));

        var step = storage.getStep(rootStepUUID).get();

        assertThat(step.getStage(), is(RUNNING));
        assertThat(step.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
            equalTo(params));
        assertThat(step.getStart(), instanceOf(Long.class));
        assertThat(step.getStop(), nullValue());
        var notFinished = step.getSteps().get(0);

        assertThat(notFinished.getStop(), nullValue());
        assertThat(notFinished.getStage(), is(RUNNING));
        assertThat(notFinished.getStatus(), is(Status.PASSED));
        assertThat(notFinished.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
            hasEntry(equalTo("Returned object"), not(emptyOrNullString())));
    }

    @Test(dependsOnMethods = "successfulFinishingOfNestedStep")
    public void finishOfRootStep() {
        var step = storage.getStep(rootStepUUID).get();
        fireReturnedValue("Returned object", new Object());

        assertThat(storage.getStep(rootStepUUID).isPresent(), is(true));
        assertThat(lifeCycle.getCurrentTestCaseOrStep().get(), is(rootStepUUID));

        assertThat(step.getStop(), nullValue());
        assertThat(step.getStage(), is(RUNNING));
        assertThat(step.getStatus(), is(Status.PASSED));
        assertThat(step.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
            hasEntry(equalTo("Returned object"), not(emptyOrNullString())));
    }

    @Test(dependsOnMethods = "finishOfRootStep")
    public void stepFinishWhenNoStepIsActive() {
        fireReturnedValue("Returned object", new Object());
        assertThat(lifeCycle.getCurrentTestCaseOrStep().get(), is(testCaseUUID.toString()));
    }
}
