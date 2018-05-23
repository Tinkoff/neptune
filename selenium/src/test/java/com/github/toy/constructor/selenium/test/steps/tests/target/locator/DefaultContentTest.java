package com.github.toy.constructor.selenium.test.steps.tests.target.locator;

import com.github.toy.constructor.selenium.test.steps.MockWebDriver;
import com.github.toy.constructor.selenium.test.steps.tests.BaseStepTest;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class DefaultContentTest extends BaseStepTest {

    @Test
    public void defaultContentTest() {
        setStartBenchMark();
        assertThat(((MockWebDriver) seleniumSteps.get(defaultContent())).isSwitchedToDefaultContent(),
                is(true));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
    }

    @Test
    public void defaultContentWithDefinedTimeTest() {
        setStartBenchMark();
        assertThat(((MockWebDriver) seleniumSteps.get(defaultContent(FIVE_SECONDS))).isSwitchedToDefaultContent(),
                is(true));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void defaultContentInSituationOfThrownExceptionWhenTimeIsNotDefined() {
        seleniumSteps.get(defaultContent());
        setStartBenchMark();
        try {
            seleniumSteps.get(defaultContent()); //mock is designed the special way
            //when it tries to switch to default content more than one time per session
            //then it throws the exception
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void defaultContentInSituationOfThrownExceptionWhenTimeIsDefined() {
        seleniumSteps.get(defaultContent());
        setStartBenchMark();
        try {
            seleniumSteps.get(defaultContent(FIVE_SECONDS)); //mock is designed the special way
            //when it tries to switch to default content more than one time per session
            //then it throws the exception
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }
}
