package ru.tinkoff.qa.neptune.selenium.properties;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.util.function.Supplier;

import static io.github.bonigarcia.wdm.WebDriverManager.*;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.addAll;
import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.REMOTE;
import static ru.tinkoff.qa.neptune.selenium.properties.URLProperties.REMOTE_WEB_DRIVER_URL_PROPERTY;

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

            return null;
        }

        @Override
        public Object[] get() {
            return ofNullable(REMOTE_WEB_DRIVER_URL_PROPERTY.get())
                    .map(url -> {
                        var result = new Object[]{url};
                        return addAll(result, super.get());
                    }).orElseThrow(() -> new IllegalStateException(format("The property %s should be defined",
                            REMOTE_WEB_DRIVER_URL_PROPERTY.getName())));
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

    public abstract WebDriverManager getWebDriverManager();
}
