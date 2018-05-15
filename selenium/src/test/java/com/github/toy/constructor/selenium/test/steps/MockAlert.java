package com.github.toy.constructor.selenium.test.steps;

import org.openqa.selenium.Alert;

public class MockAlert implements Alert {

    public static final String TEXT_OF_ALERT = "Text of the mocked alert";

    @Override
    public void dismiss() {
        //does nothing
    }

    @Override
    public void accept() {
        //does nothing
    }

    @Override
    public String getText() {
        return TEXT_OF_ALERT;
    }

    @Override
    public void sendKeys(String keysToSend) {
        //does nothing
    }
}
