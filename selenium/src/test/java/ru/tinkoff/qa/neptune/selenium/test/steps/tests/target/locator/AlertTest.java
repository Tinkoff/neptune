package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.condition;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_ALERT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_ALERT_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.MockAlert.*;

public class AlertTest extends BaseWebDriverTest {

    @BeforeMethod
    public void beforeEveryTest() {
        setSwitchedTo(false);
        setSentKeys(null);
        setDismissed(false);
        setAccepted(false);
    }

    @Test
    public void getAlertWithNoArgsTest() {
        setStartBenchMark();
        Alert alert = seleniumSteps.get(alert());
        setEndBenchMark();

        assertThat(alert, not(nullValue()));
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
    }

    @Test
    public void getAlertWithAllArgsTest() {
        setStartBenchMark();
        Alert alert = seleniumSteps.get(alert()
                .criteria(condition(format("Alert with the text %s", TEXT_OF_ALERT), alert1 -> TEXT_OF_ALERT.equals(alert1.getText())))
                .timeOut(FIVE_SECONDS)
                .pollingInterval(HALF_SECOND));
        setEndBenchMark();
        assertThat(alert, not(nullValue()));
        assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
    }

    @Test(expectedExceptions = NoAlertPresentException.class)
    public void getAlertWithDefaultExceptionThrowingTest() {
        setStartBenchMark();
        try {
            seleniumSteps.get(alert()
                    .criteria(condition("Alert with the text 'some not existing text'", alert1 ->
                            "some not existing text".equals(alert1.getText())))
                    .timeOut(FIVE_SECONDS)
                    .pollingInterval(HALF_SECOND));
        }
        catch (Exception e) {
            assertThat(e.getMessage(), containsString("No alert that suits criteria 'Alert with the text 'some not existing text'' has been found"));
            throw e;
        }
        finally {
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThanOrEqualTo(700L));
        }
        fail("Exception was expected");
    }

    @Test(expectedExceptions = NoAlertPresentException.class)
    public void getAlertWithWithWaitingTimeDefinedImplicitlyTest() {
        setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "SECONDS");
        setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.get(alert()
                    .criteria(condition("Alert with the text 'some not existing text'",
                            alert1 -> "some not existing text".equals(alert1.getText()))));
        }
        finally {
            removeProperty(WAITING_ALERT_TIME_UNIT.getPropertyName());
            removeProperty(WAITING_ALERT_TIME_VALUE.getPropertyName());
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(HALF_SECOND.toMillis()));
        }
    }

    @Test
    public void acceptAlertBySearching() {
        seleniumSteps.accept(alert());
        assertThat(isAccepted(), is(true));
    }

    @Test
    public void acceptAlert() {
        Alert alert = seleniumSteps.get(alert());
        seleniumSteps.accept(alert);
        assertThat(isAccepted(), is(true));
    }

    @Test
    public void dismissAlertBySearching() {
        seleniumSteps.dismiss(alert());
        assertThat(isDismissed(), is(true));
    }

    @Test
    public void dismissAlert() {
        Alert alert = seleniumSteps.get(alert());
        seleniumSteps.dismiss(alert);
        assertThat(isDismissed(), is(true));
    }

    @Test
    public void sendTextAlertBySearching() {
        seleniumSteps.alertSendKeys(alert(), "123");
        assertThat(getSentKeys(), is("123"));
    }

    @Test
    public void sendTextAlert() {
        Alert alert = seleniumSteps.get(alert());
        seleniumSteps.alertSendKeys(alert, "123");
        assertThat(getSentKeys(), is("123"));
    }
}
