package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriverException;

import static org.apache.commons.lang3.StringUtils.isNotBlank;

final class AlertImpl implements Alert {

    private final Alert wrapped;

    AlertImpl(Alert wrapped) {
        this.wrapped = wrapped;
    }

    @Override
    public void dismiss() {
        wrapped.dismiss();
    }

    @Override
    public void accept() {
        wrapped.accept();
    }

    @Override
    public String getText() {
        return wrapped.getText();
    }

    @Override
    public void sendKeys(String keysToSend) {
        wrapped.sendKeys(keysToSend);
    }

    public String toString() {
        var result = new StringBuilder("Alert");
        try {
            var text = wrapped.getText();
            if (isNotBlank(text)) {
                return result.append("[").append(text).append("]").toString();
            }
            return result.append("[<no text/impossible to read>]").toString();
        } catch (WebDriverException e) {
            return result.append("[<no text/impossible to read>]").toString();
        }
    }
}
