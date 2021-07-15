package ru.tinkoff.qa.neptune.selenium.test.steps.tests.java.script;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static java.lang.String.format;
import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.asynchronousJavaScript;
import static ru.tinkoff.qa.neptune.selenium.functions.java.script.GetJavaScriptResultSupplier.javaScript;
import static ru.tinkoff.qa.neptune.selenium.test.enums.Scripts.SCRIPT_1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.Scripts.SCRIPT_2;

public class JavaScriptTest extends BaseWebDriverTest {

    private static final Object[] ARGUMENTS = new Object[]{"Argument 1", "Argument 2", "Argument 3", "Argument 4"};
    private static final Criteria<Object> CONTAINS_ARGUMENT_1 =
            condition("Contains `Argument 1`", o -> String.valueOf(o).contains("Argument 1"));
    private static final Criteria<Object> CONTAINS_ARGUMENT_5 =
            condition("Contains `Argument 5`", o -> String.valueOf(o).contains("Argument 5"));
    private static final String EXCEPTION_MESSAGE = "Could't evaluate script";

    @Test
    public void javaScriptEvaluationFullParametersWithPositiveResult() {
        setStartBenchMark();
        Object result = seleniumSteps.evaluate(javaScript(SCRIPT_1.getScript(), ARGUMENTS)
                .criteria(CONTAINS_ARGUMENT_1)
                .timeOut(FIVE_SECONDS)
                .pollingInterval(ofMillis(100))
                .throwOnNoResult());

        setEndBenchMark();
        assertThat(result, is("Argument 1,Argument 2,Argument 3,Argument 4"));
        assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void javaScriptEvaluationFullParametersWithNegativeResult() {
        setStartBenchMark();
        try {
            seleniumSteps.evaluate(javaScript(SCRIPT_1.getScript(), ARGUMENTS)
                    .criteria(CONTAINS_ARGUMENT_5)
                    .timeOut(FIVE_SECONDS)
                    .pollingInterval(ofMillis(100))
                    .throwOnNoResult());

        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
        }
        fail("Exception was expected");
    }

    @Test
    public void javaScriptEvaluationFullParametersWithNullResult() {
        setStartBenchMark();
        Object result = seleniumSteps.evaluate(javaScript(SCRIPT_1.getScript(), ARGUMENTS)
                .criteria(CONTAINS_ARGUMENT_5)
                .timeOut(FIVE_SECONDS)
                .pollingInterval(ofMillis(100)));
        setEndBenchMark();
        assertThat(result, nullValue());
        assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void javaScriptEvaluationFullParametersWithExceptionThrowing() {
        setStartBenchMark();
        try {
            seleniumSteps.evaluate(javaScript(SCRIPT_2.getScript(), ARGUMENTS)
                    .criteria(CONTAINS_ARGUMENT_1)
                    .timeOut(FIVE_SECONDS)
                    .pollingInterval(ofMillis(100))
                    .throwOnNoResult());

        } catch (Exception e) {
            setEndBenchMark();
            assertThat(e.getMessage(), containsString(format("It is not possible to execute script %s with parameters %s",
                    SCRIPT_2.getScript(), ArrayUtils.toString(ARGUMENTS))));
            throw e;
        } finally {
            assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
        }
        fail("Exception was expected");
    }

    @Test
    public void asyncJavaScriptEvaluationFullParametersWithPositiveResult() {
        Object result =
                seleniumSteps.evaluate(asynchronousJavaScript(SCRIPT_1.getScript(), ARGUMENTS)
                        .criteria(CONTAINS_ARGUMENT_1)
                        .throwOnNoResult());

        assertThat(result, is("Argument 1,Argument 2,Argument 3,Argument 4"));
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void asyncJavaScriptEvaluationFullParametersWithNegativeResult() {
        try {
            seleniumSteps.evaluate(asynchronousJavaScript(SCRIPT_1.getScript(), ARGUMENTS)
                    .criteria(CONTAINS_ARGUMENT_5)
                    .throwOnNoResult());

        } catch (Exception e) {
            throw e;
        }
        fail("Exception was expected");
    }

    @Test
    public void asyncJavaScriptEvaluationFullParametersWithNullResult() {
        Object result = seleniumSteps.evaluate(asynchronousJavaScript(SCRIPT_1.getScript(), ARGUMENTS)
                .criteria(CONTAINS_ARGUMENT_5));
        assertThat(result, nullValue());
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void asyncJavaScriptEvaluationFullParametersWithExceptionThrowing() {
        setStartBenchMark();
        try {
            seleniumSteps.evaluate(asynchronousJavaScript(SCRIPT_2.getScript(), ARGUMENTS)
                    .criteria(CONTAINS_ARGUMENT_1)
                    .throwOnNoResult());

        } catch (Exception e) {
            assertThat(e.getMessage(), containsString(format("It is not possible to execute script %s with parameters %s",
                    SCRIPT_2.getScript(), ArrayUtils.toString(ARGUMENTS))));
            throw e;
        }
        fail("Exception was expected");
    }
}
