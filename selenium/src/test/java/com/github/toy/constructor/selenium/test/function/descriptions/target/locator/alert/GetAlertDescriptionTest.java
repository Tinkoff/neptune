package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.alert;

import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.Test;

import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeUnitProperties.WAITING_ALERT_TIME_UNIT;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.TimeValueProperties.WAITING_ALERT_TIME_VALUE;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

public class GetAlertDescriptionTest {

    @Test
    public void alertFullDescriptionTest() {
        assertThat(alert(condition("Some alert", alert -> true), ofSeconds(6), ofMillis(500),
                NoAlertPresentException::new).get().toString(),
                is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:00:06:000"));
    }

    @Test
    public void alertWithoutConditionWithThrowingOfExceptionDescriptionTest() {
        assertThat(alert(ofSeconds(6), ofMillis(500),
                NoAlertPresentException::new).get().toString(),
                is("Present alert on condition as is. Time to get valuable result: 0:00:06:000"));
    }

    @Test
    public void alertWithoutThrowingOfExceptionDescriptionTest() {
        assertThat(alert(condition("Some alert", alert -> true), ofSeconds(6), ofMillis(500)).get().toString(),
                is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:00:06:000"));
    }

    @Test
    public void alertWithOnlyTimeOutsDescriptionTest() {
        assertThat(alert(ofSeconds(6), ofMillis(500)).get().toString(),
                is("Present alert on condition as is. Time to get valuable result: 0:00:06:000"));
    }

    @Test
    public void alertWithoutTimeOutsDescriptionTest() {
        assertThat(alert(condition("Some alert", alert -> true), NoAlertPresentException::new).get().toString(),
                is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void alertWithTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(alert(condition("Some alert", alert -> true), NoAlertPresentException::new).get().toString(),
                    is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_ALERT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_ALERT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void alertWithOnlyExceptionThrowingDescriptionTest() {
        assertThat(alert((Supplier<NoAlertPresentException>) NoAlertPresentException::new).get().toString(),
                is("Present alert on condition as is. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void alertWithOnlyExceptionThrowingAndTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(alert((Supplier<NoAlertPresentException>) NoAlertPresentException::new).get().toString(),
                    is("Present alert on condition as is. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_ALERT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_ALERT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void alertWithConditionAndDurationDescriptionTest() {
        assertThat(alert(condition("Some alert", alert -> true), ofSeconds(6)).get().toString(),
                is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:00:06:000"));
    }

    @Test
    public void alertWitOnlyDurationDescriptionTest() {
        assertThat(alert(ofSeconds(6)).get().toString(),
                is("Present alert on condition as is. Time to get valuable result: 0:00:06:000"));
    }

    @Test
    public void alertWithOnlyConditionDescriptionTest() {
        assertThat(alert(condition("Some alert", alert -> true)).get().toString(),
                is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void alertWithOnlyConditionAndTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(alert(condition("Some alert", alert -> true)).get().toString(),
                    is("Alert from (Present alert) on condition Some alert. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_ALERT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_ALERT_TIME_VALUE.getPropertyName());
        }
    }

    @Test
    public void alertWithNoArgumentDescriptionTest() {
        assertThat(alert().get().toString(),
                is("Present alert on condition as is. Time to get valuable result: 0:01:00:000"));
    }

    @Test
    public void alertWithNoArgumentAndTimeOutDefinedInPropertiesDescriptionTest() {
        System.setProperty(WAITING_ALERT_TIME_UNIT.getPropertyName(), "MINUTES");
        System.setProperty(WAITING_ALERT_TIME_VALUE.getPropertyName(), "3");

        try {
            assertThat(alert().get().toString(),
                    is("Present alert on condition as is. Time to get valuable result: 0:03:00:000"));
        }
        finally {
            System.getProperties().remove(WAITING_ALERT_TIME_UNIT.getPropertyName());
            System.getProperties().remove(WAITING_ALERT_TIME_VALUE.getPropertyName());
        }
    }
}
