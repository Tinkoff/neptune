package ru.tinkoff.qa.neptune.selenium.authentication;

import org.openqa.selenium.WebDriver;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials.getCurrentCredentials;

/**
 * Performs login/authentication in a browser
 */
public final class AuthenticationPerformer {

    /**
     * Performs login/authentication in a browser.
     *
     * @param webDriver    is an instance of opened browser represented by {@link WebDriver}
     * @param isNewSession is it a new browser session or not
     */
    public void performAuthentication(WebDriver webDriver, boolean isNewSession) {
        ofNullable(getCurrentCredentials())
                .ifPresent(c -> {
                    if (isNewSession) {
                        c.performAuthentication(webDriver, true);
                        return;
                    }

                    if (c.areCredentialsChanged()) {
                        c.performAuthentication(webDriver, false);
                    }
                });
    }
}
