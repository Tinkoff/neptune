package ru.tinkoff.qa.neptune.selenium.test;

import org.openqa.selenium.Alert;

public class MockAlert implements Alert {

    public static final String TEXT_OF_ALERT = "Text of the mocked alert";
    private static boolean accepted;
    private static boolean dismissed;
    private static String sentKeys;
    private static boolean isSwitchedTo;

    public static boolean isAccepted() {
        return accepted;
    }

    public static void setAccepted(boolean value) {
        accepted = value;
    }

    @Override
    public String getText() {
        return TEXT_OF_ALERT;
    }

    public static boolean isDismissed() {
        return dismissed;
    }

    public static void setDismissed(boolean value) {
        dismissed = value;
    }

    public static String getSentKeys() {
        return sentKeys;
    }

    public static void setSentKeys(String keys) {
        sentKeys = keys;
    }

    public static boolean isSwitchedTo() {
        return isSwitchedTo;
    }

    public static void setSwitchedTo(boolean switchedTo) {
        isSwitchedTo = switchedTo;
    }

    @Override
    public void dismiss() {
        setDismissed(true);
    }

    @Override
    public void accept() {
        setAccepted(true);
    }

    @Override
    public void sendKeys(String keysToSend) {
        setSentKeys(keysToSend);
    }
}
