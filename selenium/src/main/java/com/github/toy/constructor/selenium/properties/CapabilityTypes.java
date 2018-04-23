package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.safari.SafariOptions;

import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.openqa.selenium.remote.CapabilityType.BROWSER_NAME;

public enum CapabilityTypes implements Supplier<Capabilities> {
    /**
     * Capabilities for the starting of {@link org.openqa.selenium.remote.RemoteWebDriver}
     */
    REMOTE {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> {
                if (CommonCapabilityProperties.BROWSER_NAME.get() == null ||
                        isBlank(String.valueOf(CommonCapabilityProperties.BROWSER_NAME.get()))) {
                    throw new IllegalArgumentException(format("The property %s should be defined",
                            CommonCapabilityProperties.BROWSER_NAME.getPropertyName()));
                }
                return super.get();
            });
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.chrome.ChromeDriver}
     */
    CHROME {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> new ChromeOptions().merge(super.get()));
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.edge.EdgeDriver}
     */
    EDGE {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> new EdgeOptions().merge(super.get()));
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.firefox.FirefoxDriver}
     */
    FIREFOX {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> new FirefoxOptions().merge(super.get()));
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.ie.InternetExplorerDriver}
     */
    IE {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> new InternetExplorerOptions().merge(super.get()));
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.opera.OperaDriver}
     */
    OPERA {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> new OperaOptions().merge(super.get()));
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.safari.SafariDriver}
     */
    SAFARI {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> new SafariOptions().merge(super.get()));
            return toBeReturned;
        }
    },

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.phantomjs.PhantomJSDriver}
     */
    PHANTOM_JS {
        private Capabilities toBeReturned;

        @Override
        public Capabilities get() {
            toBeReturned = ofNullable(toBeReturned).orElseGet(() -> {
                DesiredCapabilities capabilities = new DesiredCapabilities().merge(super.get());
                capabilities.setCapability(BROWSER_NAME, BrowserType.PHANTOMJS);
                return capabilities;
            });
            return toBeReturned;
        }
    };

    @Override
    public Capabilities get() {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        ofNullable(CommonCapabilityProperties.BROWSER_NAME.get()).ifPresent(o ->
                desiredCapabilities.setCapability(CapabilityType.BROWSER_NAME, o));

        ofNullable(CommonCapabilityProperties.PLATFORM_NAME.get()).ifPresent(o ->
                desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, o));

        desiredCapabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT,
                CommonCapabilityProperties.SUPPORTS_JAVASCRIPT.get());

        ofNullable(CommonCapabilityProperties.BROWSER_VERSION.get()).ifPresent(o ->
                desiredCapabilities.setCapability(CapabilityType.BROWSER_VERSION, o));

        return desiredCapabilities;
    }

    private enum CommonCapabilityProperties implements PropertySupplier<Object> {
        BROWSER_NAME(format("web.driver.capability.%s", CapabilityType.BROWSER_NAME)),
        PLATFORM_NAME(format("web.driver.capability.%s", CapabilityType.PLATFORM_NAME)),
        SUPPORTS_JAVASCRIPT(format("web.driver.capability.%s", CapabilityType.SUPPORTS_JAVASCRIPT)) {
            @Override
                public Boolean get() {
                return returnOptional().map(Boolean::parseBoolean).orElse(true);
            }
        },
        BROWSER_VERSION(format("web.driver.capability.%s", CapabilityType.BROWSER_VERSION));

        private final String name;

        CommonCapabilityProperties(String name) {
            this.name = name;
        }

        @Override
        public String getPropertyName() {
            return name;
        }

        @Override
        public Object get() {
            return returnOptional().orElse(null);
        }
    }
}
