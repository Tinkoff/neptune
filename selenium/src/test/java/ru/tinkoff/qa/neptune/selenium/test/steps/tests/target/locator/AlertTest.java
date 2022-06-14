package ru.tinkoff.qa.neptune.selenium.test.steps.tests.target.locator;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.BaseWebDriverPreparations;

import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.testng.Assert.fail;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.AlertCriteria.alertText;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_ALERT_TIME_UNIT;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_ALERT_TIME_VALUE;
import static ru.tinkoff.qa.neptune.selenium.test.MockAlert.*;

public class AlertTest extends BaseWebDriverPreparations {

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
        assertThat(getTimeDifference(), lessThan(ofMillis(100).toMillis()));
    }

    @Test
    public void getAlertWithAllArgsTest() {
        setStartBenchMark();
        Alert alert = seleniumSteps.get(alert()
                .criteria(alertText(TEXT_OF_ALERT))
                .timeOut(FIVE_SECONDS)
                .pollingInterval(ofMillis(100)));
        setEndBenchMark();
        assertThat(alert, not(nullValue()));
        assertThat(getTimeDifference(), lessThan(ONE_SECOND.toMillis()));
    }

    @Test(expectedExceptions = NoAlertPresentException.class)
    public void getAlertWithDefaultExceptionThrowingTest() {
        setStartBenchMark();
        try {
            seleniumSteps.get(alert()
                    .criteria(alertText("some not existing text"))
                    .timeOut(FIVE_SECONDS)
                    .pollingInterval(ofMillis(100)));
        }
        catch (Exception e) {
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
        setProperty(WAITING_ALERT_TIME_UNIT.getName(), "SECONDS");
        setProperty(WAITING_ALERT_TIME_VALUE.getName(), "5");
        setStartBenchMark();
        try {
            seleniumSteps.get(alert()
                    .criteria(alertText("some not existing text")));
        } finally {
            removeProperty(WAITING_ALERT_TIME_UNIT.getName());
            removeProperty(WAITING_ALERT_TIME_VALUE.getName());
            setEndBenchMark();
            assertThat(getTimeDifference(), greaterThanOrEqualTo(FIVE_SECONDS.toMillis()));
            assertThat(getTimeDifference() - FIVE_SECONDS.toMillis(), lessThan(ofMillis(100).toMillis()));
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
