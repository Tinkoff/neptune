package com.github.toy.constructor.selenium.properties;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;
import java.util.function.Supplier;

import static com.github.toy.constructor.selenium.properties.CapabilityTypes.*;
import static com.github.toy.constructor.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;
import static io.github.bonigarcia.wdm.WebDriverManager.*;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.add;

/**
 * This enum wraps a class of supported {@link WebDriver} and array of arguments
 * which are used for instantiation.
 */
public enum SupportedWebDrivers implements Supplier<Object[]> {
    /**
     * This item describes instantiation of {@link RemoteWebDriver}
     */
    REMOTE_DRIVER(RemoteWebDriver.class, null, REMOTE, true),
    /**
     * This item describes instantiation of {@link ChromeDriver}
     */
    CHROME_DRIVER(ChromeDriver.class, chromedriver(), CHROME, false),
    /**
     * This item describes instantiation of {@link EdgeDriver}
     */
    EDGE_DRIVER(EdgeDriver.class, edgedriver(), EDGE, false),

    /**
     * This item describes instantiation of {@link FirefoxDriver}
     */
    FIREFOX_DRIVER(FirefoxDriver.class, firefoxdriver(), FIREFOX, false),

    /**
     * This item describes instantiation of {@link InternetExplorerDriver}
     */
    IE_DRIVER(InternetExplorerDriver.class, iedriver(), IE, false),

    /**
     * This item describes instantiation of {@link OperaDriver}
     */
    OPERA_DRIVER(OperaDriver.class, operadriver(), OPERA, false),

    /**
     * This item describes instantiation of {@link SafariDriver}
     */
    SAFARI_DRIVER(SafariDriver.class, null, SAFARI, false),

    /**
     * This item describes instantiation of {@link PhantomJSDriver}
     */
    PHANTOM_JS_DRIVER(PhantomJSDriver.class, phantomjs(), PHANTOM_JS, false);

    private final Class<? extends WebDriver> webDriverClass;
    private WebDriverManager webDriverManager;
    private final CapabilityTypes capabilityType;
    private final boolean requiresRemoteUrl;
    private final URL remoteURL;

    SupportedWebDrivers(Class<? extends WebDriver> webDriverClass,
                        WebDriverManager webDriverManager,
                        CapabilityTypes capabilityType,
                        boolean requiresRemoteUrl) {
        this.webDriverClass = webDriverClass;
        this.webDriverManager = ofNullable(webDriverManager).orElseGet(() -> {
            String browserName = capabilityType.get().getBrowserName().trim();
            if (BrowserType.SAFARI.equalsIgnoreCase(browserName)) {
                return null;
            }

            if (BrowserType.CHROME.equalsIgnoreCase(browserName)) {
                return chromedriver();
            }

            if (BrowserType.EDGE.equalsIgnoreCase(browserName)) {
                return edgedriver();
            }

            if (BrowserType.FIREFOX.equalsIgnoreCase(browserName)) {
                return firefoxdriver();
            }

            if (BrowserType.IEXPLORE.equalsIgnoreCase(browserName)) {
                return iedriver();
            }

            if (BrowserType.OPERA_BLINK.equalsIgnoreCase(browserName)) {
                return operadriver();
            }

            if (BrowserType.PHANTOMJS.equalsIgnoreCase(browserName)) {
                return phantomjs();
            }

            return null;
        });
        this.capabilityType = capabilityType;
        this.requiresRemoteUrl = requiresRemoteUrl;
        remoteURL = REMOTE_WEB_DRIVER_URL_PROPERTY.get();
    }

    public Object[] get() {
        Object[] args = new Object[]{};
        if (remoteURL != null) {
            args = add(args, remoteURL);
        }
        args = add(args, capabilityType.get());
        return args;
    }

    public Class<? extends WebDriver> getWebDriverClass() {
        return webDriverClass;
    }

    /**
     * Does supported subclass of {@link WebDriver} require an URL of the remote node
     * to start a new session or not.
     *
     * @return true if supported subclass of {@link WebDriver} requires an URL of the remote node
     * to start a new session.
     */
    public boolean requiresRemoteUrl() {
        return requiresRemoteUrl;
    }

    /**
     * Returns an URL where a new remote session should be started.
     *
     * @return URL of the node to start a new remote session.
     */
    public URL getRemoteURL() {
        return remoteURL;
    }

    public WebDriverManager getWebDriverManager() {
        return webDriverManager;
    }
}
