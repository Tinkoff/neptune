package ru.tinkoff.qa.neptune.selenium.properties;

import org.openqa.selenium.MutableCapabilities;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariOptions;
import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;

import java.util.List;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public enum CapabilityTypes implements PropertySupplier<MutableCapabilities> {
    /**
     * Capabilities for the starting of {@link org.openqa.selenium.remote.RemoteWebDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.browserName} to define browser. This is the required property
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code remote.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    REMOTE("remote", () -> {
        var browserName = CommonCapabilityProperties.BROWSER_NAME.get();
        if (browserName == null ||
                isBlank(String.valueOf(browserName))) {
            throw new IllegalArgumentException(format("The property %s should be defined",
                    CommonCapabilityProperties.BROWSER_NAME.getPropertyName()));
        }

        var browserNameString = String.valueOf(browserName);
        if (BrowserType.SAFARI.equalsIgnoreCase(browserNameString)) {
            return new SafariOptions();
        }

        if (BrowserType.CHROME.equalsIgnoreCase(browserNameString)) {
            return new ChromeOptions();
        }

        if (BrowserType.EDGE.equalsIgnoreCase(browserNameString)) {
            return new EdgeOptions();
        }

        if (BrowserType.FIREFOX.equalsIgnoreCase(browserNameString)) {
            return new FirefoxOptions();
        }

        if (BrowserType.IEXPLORE.equalsIgnoreCase(browserNameString)) {
            return new InternetExplorerOptions();
        }

        if (BrowserType.OPERA_BLINK.equalsIgnoreCase(browserNameString)) {
            return new OperaOptions();
        }

        return new MutableCapabilities();
    }),

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.chrome.ChromeDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code chrome.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    CHROME("chrome", ChromeOptions::new),

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.edge.EdgeDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code edge.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    EDGE("edge", EdgeOptions::new),

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.firefox.FirefoxDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code firefox.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    FIREFOX("firefox", FirefoxOptions::new),

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.ie.InternetExplorerDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code ie.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    IE("ie", InternetExplorerOptions::new),

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.opera.OperaDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code opera.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    OPERA("opera", OperaOptions::new),

    /**
     * Capabilities for the starting of {@link org.openqa.selenium.safari.SafariDriver}
     * Creates {@link Capabilities} using following properties:
     *     <p>{@code web.driver.capability.platformName} to define name of a supported platform.
     *     Windows, Linux etc. This is not the required property. @see org.openqa.selenium.Platform
     *     <p>{@code web.driver.capability.javascriptEnabled} to enable or to disable js. Possible values are
     *     {@code true} or {@code false}. By default js is enabled. This is not the required property.
     *     <p>{@code web.driver.capability.browserVersion} to define a version of browser. This is not the required
     *     property.
     *     <p>{@code safari.capability.suppliers} to define additional capabilities. It is a string with name of a
     *     supplier.
     *     @see CapabilitySettingSupplier
     */
    SAFARI("safari", SafariOptions::new);

    private static final String CAPABILITY_SUPPLIERS = "capability.suppliers";
    private final String name;
    private final CapabilityReader capabilityReader;
    private final Supplier<MutableCapabilities> startingCapabilitiesSupplier;


    CapabilityTypes(String name, Supplier<MutableCapabilities> startingCapabilitiesSupplier) {
        this.name = format("%s.%s", name, CAPABILITY_SUPPLIERS);
        capabilityReader = new CapabilityReader();
        this.startingCapabilitiesSupplier = startingCapabilitiesSupplier;
    }

    @Override
    public MutableCapabilities get() {
        var desiredCapabilities = startingCapabilitiesSupplier.get();
        ofNullable(CommonCapabilityProperties.PLATFORM_NAME.get()).ifPresent(o ->
                desiredCapabilities.setCapability(CapabilityType.PLATFORM_NAME, o));

        desiredCapabilities.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT,
                CommonCapabilityProperties.SUPPORTS_JAVASCRIPT.get());

        ofNullable(CommonCapabilityProperties.BROWSER_VERSION.get()).ifPresent(o ->
                desiredCapabilities.setCapability(CapabilityType.BROWSER_VERSION, o));

        ofNullable(capabilityReader.get())
                .orElse(List.of())
                .forEach(capabilitySupplier -> capabilitySupplier.get().accept(desiredCapabilities));

        return desiredCapabilities;
    }

    @Override
    public String getPropertyName() {
        return name;
    }

    private class CapabilityReader implements MultipleObjectPropertySupplier<CapabilitySettingSupplier<MutableCapabilities>> {
        @Override
        public String getPropertyName() {
            return name;
        }
    }

    public enum CommonCapabilityProperties implements PropertySupplier<Object> {
        /**
         * Reads property {@code web.driver.capability.browserName} and returns string value.
         * Should be the same as following:
         *     <p>{@link BrowserType#CHROME}
         *     <p>{@link BrowserType#EDGE}
         *     <p>{@link BrowserType#FIREFOX}
         *     <p>{@link BrowserType#IEXPLORE}
         *     <p>{@link BrowserType#OPERA_BLINK}
         *     <p>{@link BrowserType#SAFARI}
         *
         * It has sense to define value of the property when value of the property {@link SupportedWebDriverProperty#SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY}
         * is {@link SupportedWebDrivers#REMOTE_DRIVER}
         */
        BROWSER_NAME(format("web.driver.capability.%s", CapabilityType.BROWSER_NAME)),

        /**
         * Reads property {@code web.driver.capability.platformName} and returns string value.
         * Should be the same as an item of {@link org.openqa.selenium.Platform}
         */
        PLATFORM_NAME(format("web.driver.capability.%s", CapabilityType.PLATFORM_NAME)),

        /**
         * Reads property {@code web.driver.capability.javascriptEnabled} and returns boolean value.
         * Should be {@code true} or {@code false}. By default it returns {@code true}.
         */
        SUPPORTS_JAVASCRIPT(format("web.driver.capability.%s", CapabilityType.SUPPORTS_JAVASCRIPT)) {
            @Override
                public Boolean get() {
                return returnOptionalFromEnvironment().map(Boolean::parseBoolean).orElse(true);
            }
        },

        /**
         * Reads property {@code web.driver.capability.browserVersion} and returns string value.
         */
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
            return returnOptionalFromEnvironment().orElse(null);
        }
    }
}
