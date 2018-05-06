package com.github.toy.constructor.selenium.test.function.descriptions;

import org.openqa.selenium.Alert;

public class DescribedAlert implements Alert {
    @Override
    public void dismiss() {

    }

    @Override
    public void accept() {

    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public void sendKeys(String keysToSend) {

    }

    @Override
    public String toString() {
        return "Test alert";
    }
}
