package ru.tinkoff.qa.neptune.allure;

import io.qameta.allure.model.Parameter;
import io.qameta.allure.model.Status;
import org.testng.annotations.Test;

import java.util.LinkedHashMap;
import java.util.Map;

import static io.qameta.allure.model.Stage.FINISHED;
import static io.qameta.allure.model.Stage.RUNNING;
import static java.util.stream.Collectors.toMap;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static ru.tinkoff.qa.neptune.core.api.event.firing.StaticEventFiring.*;

public class SuccessfulStepLifeCycleTest extends AbstractAllureTest {


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

    @Test
    public void initialStepTest() {
        fireEventStarting("Root step", params);
        rootStepUUID = lifeCycle.getCurrentTestCaseOrStep().get();


        var step = storage.getStep(rootStepUUID).get();
        assertThat(step.getName(), is("Root step"));
        assertThat(step.getStage(), is(RUNNING));
        assertThat(step.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
                equalTo(params));
        assertThat(step.getStart(), instanceOf(Long.class));
        assertThat(step.getStop(), nullValue());
    }

    @Test(dependsOnMethods = "initialStepTest")
    public void nestedStepTest() {
        fireEventStarting("Nested step", params);
        nestedStepUUID = lifeCycle.getCurrentTestCaseOrStep().get();

        var rootStep = storage.getStep(rootStepUUID).get();

        assertThat(nestedStepUUID, not(equalTo(rootStepUUID)));
        var step = storage.getStep(nestedStepUUID).get();
        assertThat(step.getName(), is("Nested step"));
        assertThat(step.getStage(), is(RUNNING));
        assertThat(step.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
                equalTo(params));
        assertThat(step.getStart(), instanceOf(Long.class));
        assertThat(rootStep.getSteps(), contains(step));
        assertThat(step.getStart(), greaterThanOrEqualTo(rootStep.getStart()));
        assertThat(step.getStop(), nullValue());
        assertThat(rootStep.getStop(), nullValue());
    }

    @Test(dependsOnMethods = "nestedStepTest")
    public void addingAdditionalParametersToNestedStep() {
        fireAdditionalParameters(Map.of("additional 1", "value 1"));
        var step = storage.getStep(nestedStepUUID).get();
        var rootStep = storage.getStep(rootStepUUID).get();

        assertThat(step.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
                hasEntry("additional 1", "value 1"));

        assertThat(rootStep.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
                not(hasEntry("additional 1", "value 1")));
    }

    @Test(dependsOnMethods = {"addingAdditionalParametersToNestedStep", "nestedStepTest"})
    public void successfulFinishingOfNestedStep() {
        fireEventFinishing();

        assertThat(storage.getStep(nestedStepUUID).isPresent(), is(false));
        assertThat(lifeCycle.getCurrentTestCaseOrStep().get(), is(rootStepUUID));

        var step = storage.getStep(rootStepUUID).get();

        assertThat(step.getStage(), is(RUNNING));
        assertThat(step.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
                equalTo(params));
        assertThat(step.getStart(), instanceOf(Long.class));
        assertThat(step.getStop(), nullValue());
        var finished = step.getSteps().get(0);

        assertThat(finished.getStop(), instanceOf(Long.class));
        assertThat(finished.getStage(), is(FINISHED));
        assertThat(finished.getStatus(), is(Status.PASSED));
    }

    @Test(dependsOnMethods = "successfulFinishingOfNestedStep")
    public void addingAdditionalParametersToRootStep() {
        fireAdditionalParameters(Map.of("additional 1", "value 1"));
        var rootStep = storage.getStep(rootStepUUID).get();

        assertThat(rootStep.getParameters().stream().collect(toMap(Parameter::getName, Parameter::getValue)),
                hasEntry("additional 1", "value 1"));
    }

    @Test(dependsOnMethods = "addingAdditionalParametersToRootStep")
    public void finishOfRootStep() {
        var step = storage.getStep(rootStepUUID).get();
        fireEventFinishing();

        assertThat(storage.getStep(rootStepUUID).isPresent(), is(false));
        assertThat(step.getStop(), instanceOf(Long.class));
        assertThat(step.getStage(), is(FINISHED));
        assertThat(step.getStatus(), is(Status.PASSED));
    }
}
