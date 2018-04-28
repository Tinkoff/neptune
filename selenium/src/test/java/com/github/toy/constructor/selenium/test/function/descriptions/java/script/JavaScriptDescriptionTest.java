package com.github.toy.constructor.selenium.test.function.descriptions.java.script;

import org.testng.annotations.Test;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;
import static com.github.toy.constructor.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class JavaScriptDescriptionTest {

    private static final Supplier<RuntimeException> TEST_EXCEPTION_SUPPLIER = () -> new RuntimeException("Test exception");
    private static final List<Object> PARAMETERS = List.of(
            new Object() {
                @Override
                public String toString() {
                    return "param1";
                }
            },

            new Object() {
                @Override
                public String toString() {
                    return "param2";
                }
            },

            new Object() {
                @Override
                public String toString() {
                    return "param3";
                }
            }
    );

    @Test
    public void jsWithParametersConditionSleepingAndDurationThrowsException() {
        assertThat(javaScript("some script", condition("Not null value", Objects::nonNull),
                ofSeconds(50), ofSeconds(1), TEST_EXCEPTION_SUPPLIER, PARAMETERS.toArray()).get().toString(),
                is("Result from (Evaluation of java script 'some script' with parameters [param1, param2, param3]) " +
                        "on condition Not null value. Time to get valuable result: 0:00:50:000"));
    }

    @Test
    public void jsWithParametersConditionAndDurationThrowsException() {
        assertThat(javaScript("some script", condition("Not null value", Objects::nonNull),
                ofSeconds(50), TEST_EXCEPTION_SUPPLIER, PARAMETERS.toArray()).get().toString(),
                is("Result from (Evaluation of java script 'some script' with parameters [param1, param2, param3]) " +
                        "on condition Not null value. Time to get valuable result: 0:00:50:000"));
    }

    @Test
    public void jsWithParametersConditionAndDuration() {
        assertThat(javaScript("some script", condition("Not null value", Objects::nonNull),
                ofSeconds(50), PARAMETERS.toArray()).get().toString(),
                is("Result from (Evaluation of java script 'some script' with parameters [param1, param2, param3]) " +
                        "on condition Not null value. Time to get valuable result: 0:00:50:000"));
    }

    @Test
    public void jsWithParametersCondition() {
        assertThat(javaScript("some script", condition("Not null value", Objects::nonNull),
                PARAMETERS.toArray()).get().toString(),
                is("Result from (Evaluation of java script 'some script' with parameters [param1, param2, param3]) on " +
                        "condition Not null value"));
    }

    @Test
    public void jsWithParameters() {
        assertThat(javaScript("some script", PARAMETERS.toArray()).get().toString(),
                is("Evaluation of java script 'some script' with parameters [param1, param2, param3]"));
    }

    @Test
    public void jsWithOUTParameters() {
        assertThat(javaScript("some script").get().toString(),
                is("Evaluation of java script 'some script'"));
    }

    @Test
    public void asyncJsWithParametersConditionThrowsException() {
        assertThat(asynchronousJavaScript("some script", condition("Not null value", Objects::nonNull),
                TEST_EXCEPTION_SUPPLIER, PARAMETERS.toArray()).get().toString(),
                is("Result from (Evaluation of asynchronous java script 'some script' " +
                        "with parameters [param1, param2, param3]) on condition Not null value"));
    }

    @Test
    public void asyncJsWithParametersCondition() {
        assertThat(asynchronousJavaScript("some script", condition("Not null value", Objects::nonNull),
                PARAMETERS.toArray()).get().toString(),
                is("Result from (Evaluation of asynchronous java script 'some script' " +
                        "with parameters [param1, param2, param3]) on condition Not null value"));
    }

    @Test
    public void asyncJsWithParameters() {
        assertThat(asynchronousJavaScript("some script", PARAMETERS.toArray()).get().toString(),
                is("Evaluation of asynchronous java script 'some script' with parameters [param1, param2, param3]"));
    }

    @Test
    public void asyncJsWithoutParameters() {
        assertThat(asynchronousJavaScript("some script").get().toString(),
                is("Evaluation of asynchronous java script 'some script'"));
    }
}
