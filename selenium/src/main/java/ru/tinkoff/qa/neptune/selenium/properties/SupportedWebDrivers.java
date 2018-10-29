package ru.tinkoff.qa.neptune.selenium.properties;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.URL;
import java.util.function.Supplier;

import static org.apache.commons.lang3.ArrayUtils.addAll;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.*;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;
import static io.github.bonigarcia.wdm.WebDriverManager.*;
import static java.util.Optional.ofNullable;

/**
 * This enum wraps a class of supported {@link WebDriver} and array of arguments
 * which are used for instantiation.
 */
public enum SupportedWebDrivers implements Supplier<Object[]> {
    /**
     * This item describes instantiation of {@link RemoteWebDriver}
     */
    REMOTE_DRIVER(RemoteWebDriver.class, REMOTE, true) {
        @Override
        public WebDriverManager getWebDriverManager() {

            if (REMOTE_WEB_DRIVER_URL_PROPERTY.get() != null) {
                return null;
            }

            var browserName = ofNullable(CommonCapabilityProperties.BROWSER_NAME.get())
                    .map(Object::toString)
                    .orElse(null);

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

            return null;
        }

        @Override
        public Object[] get() {
            return ofNullable(REMOTE_WEB_DRIVER_URL_PROPERTY.get())
                    .map(url -> {
                        var result = new Object[]{url};
                        return addAll(result, super.get());
                    }).orElseGet(super::get);
        }
    },
    /**
     * This item describes instantiation of {@link ChromeDriver}
     */
    CHROME_DRIVER(ChromeDriver.class, CapabilityTypes.CHROME, false) {
        @Override
        public WebDriverManager getWebDriverManager() {
            return chromedriver();
        }
    },
    /**
     * This item describes instantiation of {@link EdgeDriver}
     */
    EDGE_DRIVER(EdgeDriver.class, CapabilityTypes.EDGE, false) {
        @Override
        public WebDriverManager getWebDriverManager() {
            return edgedriver();
        }
    },

    /**
     * This item describes instantiation of {@link FirefoxDriver}
     */
    FIREFOX_DRIVER(FirefoxDriver.class, CapabilityTypes.FIREFOX, false) {
        @Override
        public WebDriverManager getWebDriverManager() {
            return firefoxdriver();
        }
    },

    /**
     * This item describes instantiation of {@link InternetExplorerDriver}
     */
    IE_DRIVER(InternetExplorerDriver.class, CapabilityTypes.IE, false) {
        @Override
        public WebDriverManager getWebDriverManager() {
            return iedriver();
        }
    },

    /**
     * This item describes instantiation of {@link OperaDriver}
     */
    OPERA_DRIVER(OperaDriver.class, CapabilityTypes.OPERA, false) {
        @Override
        public WebDriverManager getWebDriverManager() {
            return operadriver();
        }
    },

    /**
     * This item describes instantiation of {@link SafariDriver}
     */
    SAFARI_DRIVER(SafariDriver.class, CapabilityTypes.SAFARI, false) {
        @Override
        public WebDriverManager getWebDriverManager() {
            return null;
        }
    };

    private final Class<? extends WebDriver> webDriverClass;
    private final CapabilityTypes capabilityType;
    private final boolean requiresRemoteUrl;

    SupportedWebDrivers(Class<? extends WebDriver> webDriverClass,
                        CapabilityTypes capabilityType,
                        boolean requiresRemoteUrl) {
        this.webDriverClass = webDriverClass;
        this.capabilityType = capabilityType;
        this.requiresRemoteUrl = requiresRemoteUrl;
    }

    public Object[] get() {
       return new Object[]{capabilityType.get()};
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
        return URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY.get();
    }

    public abstract WebDriverManager getWebDriverManager();
}
