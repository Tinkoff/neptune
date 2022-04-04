package ru.tinkoff.qa.neptune.selenium;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.NoSuchSessionException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.devtools.DevTools;
import org.openqa.selenium.devtools.HasDevTools;
import org.openqa.selenium.remote.UnreachableBrowserException;
import ru.tinkoff.qa.neptune.core.api.cleaning.ContextRefreshable;
import ru.tinkoff.qa.neptune.selenium.authentication.AuthenticationPerformer;
import ru.tinkoff.qa.neptune.selenium.properties.SupportedWebDrivers;

import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.ConstructorUtil.findSuitableConstructor;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FORCE_WINDOW_MAXIMIZING_ON_START;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.KEEP_WEB_DRIVER_SESSION_OPENED;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.BASE_WEB_DRIVER_URL_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FOR_PAGE_LOADED_DURATION;

public class WrappedWebDriver implements WrapsDriver, ContextRefreshable {
    private final SupportedWebDrivers supportedWebDriver;
    private WebDriver driver;
    private DevTools devTools;
    private boolean isWebDriverInstalled;
    private final AuthenticationPerformer authenticationPerformer = new AuthenticationPerformer();

    public WrappedWebDriver(SupportedWebDrivers supportedWebDriver) {
        this.supportedWebDriver = supportedWebDriver;
    }

    private synchronized boolean isNewSession() {
        if (isAlive()) {
            return false;
        }
        devTools = null;
        Object[] parameters = supportedWebDriver.get();

        try {
            var c = findSuitableConstructor(supportedWebDriver.getWebDriverClass(),
                    parameters);

            if (!isWebDriverInstalled) {
                ofNullable(supportedWebDriver.getWebDriverManager())
                        .ifPresent(WebDriverManager::setup);
                isWebDriverInstalled = true;
            }

            var webDriverClass = supportedWebDriver.getWebDriverClass();
            var webDriverConstructor = webDriverClass.getConstructor(c.getParameterTypes());
            var createdWebDriver = (WebDriver) webDriverConstructor.newInstance(parameters);

            ofNullable(BASE_WEB_DRIVER_URL_PROPERTY.get())
                    .ifPresent(url -> createdWebDriver.get(url.toString()));
            if (FORCE_WINDOW_MAXIMIZING_ON_START.get()) {
                createdWebDriver.manage().window().maximize();
            }

            createdWebDriver.manage().timeouts().pageLoadTimeout(WAITING_FOR_PAGE_LOADED_DURATION.get());

            authenticationPerformer.performAuthentication(createdWebDriver, true);

            this.driver = createdWebDriver;
            return true;
        } catch (Exception e) {
            throw new WebDriverCreationException(e);
        }
    }

    private boolean isAlive() {
        if (driver == null) {
            return false;
        }

        try {
            driver.getCurrentUrl();
            return true;
        } catch (UnreachableBrowserException | NoSuchSessionException e1) {
            return false;
        } catch (WebDriverException e2) {
            return true;
        } catch (Exception e) {
            var cause = e.getCause();
            while (nonNull(cause)) {
                var causeClass = cause.getClass();
                if (causeClass.equals(UnreachableBrowserException.class) || causeClass.equals(NoSuchSessionException.class)) {
                    return false;
                }
                if (causeClass.equals(WebDriverException.class)) {
                    return true;
                }
                cause = cause.getCause();
            }
            throw new RuntimeException(e);
        }
    }

    @Override
    public synchronized void refreshContext() {
        boolean isAlive = isAlive();

        if (!isAlive || !KEEP_WEB_DRIVER_SESSION_OPENED.get()) {
            if (isAlive) {
                driver.quit();
            }
            driver = null;
            return;
        }

        ofNullable(devTools)
                .map(DevTools::getCdpSession)
                .ifPresent(cdpSession -> devTools.clearListeners());

        devTools = null;
    }

    @Override
    public synchronized WebDriver getWrappedDriver() {
        if (!isNewSession()) {
            authenticationPerformer.performAuthentication(driver, false);
        }
        return driver;
    }

    public void shutDown() {
        devTools = null;
        ofNullable(driver).ifPresent(webDriver -> {
            try {
                webDriver.quit();
            } catch (Throwable ignored) {
            }
        });
    }

    public DevTools getDevTools() {
        return ofNullable(devTools)
                .map(dt -> {
                    dt.createSessionIfThereIsNotOne();
                    return dt;
                })
                .orElseGet(() -> {
                    var driver = getWrappedDriver();
                    if (driver instanceof HasDevTools) {
                        devTools = ((HasDevTools) driver).getDevTools();
                        devTools.createSession();
                        return devTools;
                    } else {
                        throw new UnsupportedOperationException(format("This wrappedDriver(%s) does not support the use of selenium devTools", driver.getClass()));
                    }
                });

    }

    private static final class WebDriverCreationException extends RuntimeException {
        private WebDriverCreationException(Throwable cause) {
            super(cause);
        }
    }
}
