package com.github.toy.constructor.selenium.test.function.descriptions.target.locator.alert;

import com.github.toy.constructor.selenium.test.function.descriptions.DescribedAlert;
import org.openqa.selenium.NoAlertPresentException;
import org.testng.annotations.Test;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.accept;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.dismiss;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.AlertActionSupplier.sendKeys;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlertSupplier.alert;
import static java.time.Duration.ofMillis;
import static java.time.Duration.ofSeconds;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class AlertActionDescriptionTest {

    @Test
    public void dismissWithSearchingForAlertDescriptionTest() {
        assertThat(dismiss(alert(condition("Some alert", alert -> true), ofSeconds(6), ofMillis(500),
                NoAlertPresentException::new)).get().toString(),
                is("Dismiss. With parameters: {Present alert with condition Some alert. " +
                        "Time to get valuable result: 0:00:06:000}"));
    }

    @Test
    public void dismissWithAlertDescriptionTest() {
        assertThat(dismiss(new DescribedAlert()).get().toString(),
                is("Dismiss. With parameters: {Test alert}"));
    }

    @Test
    public void acceptWithSearchingForAlertDescriptionTest() {
        assertThat(accept(alert(condition("Some alert", alert -> true), ofSeconds(6), ofMillis(500),
                NoAlertPresentException::new)).get().toString(),
                is("Accept. With parameters: {Present alert with condition Some alert. " +
                        "Time to get valuable result: 0:00:06:000}"));
    }

    @Test
    public void acceptWithAlertDescriptionTest() {
        assertThat(accept(new DescribedAlert()).get().toString(),
                is("Accept. With parameters: {Test alert}"));
    }

    @Test
    public void sendKeysWithSearchingForAlertDescriptionTest() {
        assertThat(sendKeys(alert(condition("Some alert", alert -> true), ofSeconds(6), ofMillis(500),
                NoAlertPresentException::new), "Some keys").get().toString(),
                is("Send keys. With parameters: {Present alert with condition Some alert. " +
                        "Time to get valuable result: 0:00:06:000,Some keys}"));
    }

    @Test
    public void sendKeysWithAlertDescriptionTest() {
        assertThat(sendKeys(new DescribedAlert(), "Some keys").get().toString(),
                is("Send keys. With parameters: {Test alert,Some keys}"));
    }
}
