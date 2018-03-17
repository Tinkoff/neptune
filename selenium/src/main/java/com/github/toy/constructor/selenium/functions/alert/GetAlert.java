package com.github.toy.constructor.selenium.functions.alert;

import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;

final class GetAlert implements Function<SeleniumSteps, Alert> {

    private GetAlert() {
        super();
    }

    static Function<SeleniumSteps, Alert> getAlert() {
        return toGet("Find alert", new GetAlert());
    }

    @Override
    public Alert apply(SeleniumSteps seleniumSteps) {
        try {
            return seleniumSteps.getWrappedDriver().switchTo().alert();
        }
        catch (NoAlertPresentException e) {
            return null;
        }
    }
}
