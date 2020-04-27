package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;

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
        return wrapped.getText();
    }
}
