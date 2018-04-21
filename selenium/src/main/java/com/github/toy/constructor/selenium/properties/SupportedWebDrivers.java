package com.github.toy.constructor.selenium.properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;
import java.util.function.Supplier;

import static com.github.toy.constructor.selenium.properties.CapabilityTypes.*;
import static com.github.toy.constructor.selenium.properties.DriverNameProperty.*;
import static com.github.toy.constructor.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;

/**
 * This enum wraps a class of supported {@link WebDriver} and array of arguments
 * which are used for instantiation.
 */
public enum SupportedWebDrivers implements Supplier<Object[]> {
    /**
     * This item describes instantiation of {@link RemoteWebDriver}
     */
    REMOTE_DRIVER(REMOTE_NAME, RemoteWebDriver.class, GENERAL, true),
    /**
     * This item describes instantiation of {@link ChromeDriver}
     */
    CHROME_DRIVER(CHROME_NAME, ChromeDriver.class, CHROME, false),
    /**
     * This item describes instantiation of {@link EdgeDriver}
     */
    EDGE_DRIVER(EDGE_NAME, EdgeDriver.class, EDGE, false),

    /**
     * This item describes instantiation of {@link FirefoxDriver}
     */
    FIREFOX_DRIVER(FIREFOX_NAME, FirefoxDriver.class, FIREFOX, false),

    /**
     * This item describes instantiation of {@link InternetExplorerDriver}
     */
    IE_DRIVER(IE_NAME, InternetExplorerDriver.class, IE, false),

    /**
     * This item describes instantiation of {@link OperaDriver}
     */
    OPERA_DRIVER(OPERA_NAME, OperaDriver.class, OPERA, false),

    /**
     * This item describes instantiation of {@link SafariDriver}
     */
    SAFARI_DRIVER(SAFARI_NAME, SafariDriver.class, SAFARI, false),

    /**
     * This item describes instantiation of {@link PhantomJSDriver}
     */
    PHANTOM_JS_DRIVER(PHANTOM_JS_NAME, PhantomJSDriver.class, PHANTOM_JS, false);

    private final DriverNameProperty name;
    private final Class<? extends WebDriver> webDriverClass;
    private final CapabilityTypes capabilityType;
    private final boolean requiresRemoteUrl;
    private final URL remoteURL;

    SupportedWebDrivers(DriverNameProperty name, Class<? extends WebDriver> webDriverClass,
                                CapabilityTypes capabilityType,
                                boolean requiresRemoteUrl) {
        this.name = name;
        this.webDriverClass = webDriverClass;
        this.capabilityType = capabilityType;
        this.requiresRemoteUrl = requiresRemoteUrl;
        remoteURL = REMOTE_WEB_DRIVER_URL_PROPERTY.get();
    }


    @Override
    public String toString() {
        return name.toString();
    }


    public Object[] get() {
        //TODO to be implemented
        return null;
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
}
