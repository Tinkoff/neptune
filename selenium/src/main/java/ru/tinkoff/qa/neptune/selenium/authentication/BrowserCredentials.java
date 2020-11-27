package ru.tinkoff.qa.neptune.selenium.authentication;

import org.openqa.selenium.WebDriver;

import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.properties.WebDriverCredentialsProperty.WEB_DRIVER_CREDENTIALS_PROPERTY;

/**
 * This class is designed to perform login/authentication in a browser
 *
 * @param <T> is a type of an object that represents credentials
 */
public abstract class BrowserCredentials<T> {

    private static final ThreadLocal<BrowserCredentials<?>> CREDENTIAL_CONTAINER_THREAD_LOCAL = new ThreadLocal<>();
    private boolean areCredentialsChanged;

    public BrowserCredentials() {
        CREDENTIAL_CONTAINER_THREAD_LOCAL.set(this);
    }

    static BrowserCredentials<?> getCurrentCredentials() {
        var cred = CREDENTIAL_CONTAINER_THREAD_LOCAL.get();
        if (cred != null) {
            return cred;
        }

        return ofNullable(WEB_DRIVER_CREDENTIALS_PROPERTY.get())
                .map(Supplier::get)
                .orElse(null);
    }

    /**
     * Changes login/credentials for an opened browser.
     *
     * @param credentials is an object that represents credentials
     * @param <T>         is a type of an object that represents credentials
     * @throws UnsupportedOperationException when there is no object that can perform login action in a browser.
     * @throws IllegalArgumentException      when {@code credentials} are not supported
     */
    @SuppressWarnings("unchecked")
    public static <T> void changeBrowserLogin(T credentials) {
        var c = getCurrentCredentials();
        if (c == null) {
            throw new UnsupportedOperationException(format("There is no object that can perform login action in a browser. " +
                            "Please define the '%s' property",
                    WEB_DRIVER_CREDENTIALS_PROPERTY.getName()));
        }

        try {
            ((BrowserCredentials<T>) c).changeCredentials(credentials);
        } catch (ClassCastException e) {
            throw new IllegalArgumentException(format("Object of type %s doesn't support credentials of type %s",
                    c.getClass(),
                    ofNullable(credentials).map(t -> t.getClass().getName()).orElse("<null>")));
        }
    }

    boolean areCredentialsChanged() {
        return areCredentialsChanged;
    }

    private void changeCredentials(T credentials) {
        areCredentialsChanged = areCredentialsDifferent(credentials);
    }

    protected abstract boolean areCredentialsDifferent(T credentials);

    /**
     * Performs login/authentication in a browser.
     *
     * @param webDriver    is an instance of opened browser represented by {@link WebDriver}
     * @param isNewSession is it a new browser session or not
     */
    protected abstract void authentication(WebDriver webDriver, boolean isNewSession);

    void performAuthentication(WebDriver webDriver, boolean isNewSession) {
        authentication(webDriver, isNewSession);
        areCredentialsChanged = false;
    }
}
