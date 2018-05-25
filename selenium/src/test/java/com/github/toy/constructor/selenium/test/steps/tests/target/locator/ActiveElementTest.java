package com.github.toy.constructor.selenium.test.steps.tests.target.locator;

import com.github.toy.constructor.selenium.test.ActiveWebElement;
import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.WebDriverException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static com.github.toy.constructor.selenium.functions.target.locator.active.element.GetActiveElementSupplier.activeElement;
import static com.github.toy.constructor.selenium.functions.target.locator.content.DefaultContentSupplier.defaultContent;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

public class ActiveElementTest extends BaseWebDriverTest {

    @BeforeMethod
    public void clearActiveElement() {
        ActiveWebElement.activeWebElement = null;
    }

    @Test
    public void activeElementTest() {
        setStartBenchMark();
        assertThat(seleniumSteps.get(activeElement()).toString(),
                is("Active web element"));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
    }

    @Test
    public void activeElementWithDefinedTimeTest() {
        setStartBenchMark();
        assertThat(seleniumSteps.get(activeElement()).toString(),
                is("Active web element"));
        setEndBenchMark();
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void activeElementInSituationOfThrownExceptionWhenTimeIsNotDefined() {
        seleniumSteps.get(activeElement());
        setStartBenchMark();
        try {
            seleniumSteps.get(activeElement()); //mock is designed the special way
            //when it tries to switch to active element more than one time per session
            //then it throws the exception
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test(expectedExceptions = WebDriverException.class)
    public void activeElementInSituationOfThrownExceptionWhenTimeIsDefined() {
        seleniumSteps.get(activeElement());
        setStartBenchMark();
        try {
            seleniumSteps.get(activeElement(FIVE_SECONDS)); //mock is designed the special way
            //when it tries to switch to active element more than one time per session
            //then it throws the exception
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }
}
