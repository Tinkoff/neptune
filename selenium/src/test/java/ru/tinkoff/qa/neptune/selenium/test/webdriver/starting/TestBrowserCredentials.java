package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials;

import java.util.Objects;

public class TestBrowserCredentials extends BrowserCredentials<String> {

    public static boolean isANewSession;
    public static boolean hasLoginPerformed;

    private String credentials;

    @Override
    protected boolean areCredentialsDifferent(String credentials) {
        if (!Objects.equals(this.credentials, credentials)) {
            this.credentials = credentials;
            return true;
        }

        hasLoginPerformed = false;
        return false;
    }

    @Override
    protected void authentication(WebDriver webDriver, boolean isNewSession) {
        isANewSession = isNewSession;
        hasLoginPerformed = true;
    }
}
