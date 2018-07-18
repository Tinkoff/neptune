package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;

import java.util.function.Function;

final class GetAlert implements Function<WebDriver, Alert> {

    private GetAlert() {
        super();
    }

    static Function<WebDriver, Alert> getAlert() {
        return new GetAlert();
    }

    @Override
    public Alert apply(WebDriver webDriver) {
        try {
            return webDriver.switchTo().alert();
        }
        catch (NoAlertPresentException e) {
            return null;
        }
    }
}
