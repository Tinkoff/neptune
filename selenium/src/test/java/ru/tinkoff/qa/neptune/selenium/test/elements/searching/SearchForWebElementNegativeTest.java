package ru.tinkoff.qa.neptune.selenium.test.elements.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.BaseWebDriverPreparations;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static org.hamcrest.Matchers.lessThan;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.ELEMENT_WAITING_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class SearchForWebElementNegativeTest extends BaseWebDriverPreparations {

    private static final By CLASS_THAT_DOES_NOT_EXIST = className("fakeClass");

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                .timeOut(ONE_SECOND));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementFirstLevelOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedWithoutConditionWithDefinedTimeTest() {
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedWithoutConditionWithTimeDefinedImplicitlyTest() {
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(webElement(tagName(BUTTON_TAG))));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findWebElementChainedOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(webElement(className(SPREAD_SHEET_CLASS))));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementWithoutConditionWithDefinedTimeTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementWithoutConditionWithTimeDefinedImplicitlyTest() {
        WebElement parent = seleniumSteps.find(webElement(tagName(BUTTON_TAG)));
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(CLASS_THAT_DOES_NOT_EXIST)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndDefinedTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndImplicitTimeTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "1");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }

    @Test(expectedExceptions = NoSuchElementException.class, retryAnalyzer = RetryAnalyzer.class)
    public void findFromWebElementOnlyVisibleImplicitConditionAndTimeConflictTest() {
        setProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName(), "true");
        setProperty(ELEMENT_WAITING_TIME_UNIT.getName(), "SECONDS");
        setProperty(ELEMENT_WAITING_TIME_VALUE.getName(), "5");
        WebElement parent = seleniumSteps.find(webElement(className(SPREAD_SHEET_CLASS)));
        setStartBenchMark();
        try {
            seleniumSteps.find(webElement(INVISIBLE_SPAN_BY)
                    .timeOut(ONE_SECOND)
                    .foundFrom(parent));
        } catch (Exception e) {
            setEndBenchMark();
            throw e;
        } finally {
            removeProperty(FIND_ONLY_VISIBLE_ELEMENTS.getName());
            removeProperty(ELEMENT_WAITING_TIME_UNIT.getName());
            removeProperty(ELEMENT_WAITING_TIME_VALUE.getName());
            assertThat(getTimeDifference(), greaterThanOrEqualTo(ONE_SECOND.toMillis()));
            assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
        }
    }
}
