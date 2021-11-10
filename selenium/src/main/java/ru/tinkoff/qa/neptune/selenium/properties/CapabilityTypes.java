package ru.tinkoff.qa.neptune.selenium.properties;

import org.openqa.selenium.MutableCapabilities;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.opera.OperaOptions;
import org.openqa.selenium.remote.Browser;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.safari.SafariOptions;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.joining;
import static org.apache.commons.lang3.StringUtils.isBlank;

public enum CapabilityTypes implements PropertySupplier<MutableCapabilities, Class<? extends CapabilitySettingSupplier<?>>[]> {

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<MutableCapabilities>",
            "whose instances create and supplement capabilities",
            "for the starting of RemoteWebDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("REMOTE_CAPABILITY_SUPPLIERS")
    REMOTE(() -> {
        var browserName = CommonCapabilityProperties.BROWSER_NAME.get();
        if (browserName == null ||
                isBlank(String.valueOf(browserName))) {
            throw new IllegalArgumentException(format("The property %s should be defined",
                    CommonCapabilityProperties.BROWSER_NAME.getName()));
        }

        var browserNameString = String.valueOf(browserName);
        if (Browser.SAFARI.browserName().equalsIgnoreCase(browserNameString)) {
            return new SafariOptions();
        }

        if (Browser.CHROME.browserName().equalsIgnoreCase(browserNameString)) {
            return new ChromeOptions();
        }

        if (Browser.EDGE.browserName().equalsIgnoreCase(browserNameString)) {
            return new EdgeOptions();
        }

        if (Browser.FIREFOX.browserName().equalsIgnoreCase(browserNameString)) {
            return new FirefoxOptions();
        }

        if (Browser.IE.browserName().equalsIgnoreCase(browserNameString)) {
            return new InternetExplorerOptions();
        }

        return new MutableCapabilities();
    }),

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<ChromeOptions>",
            "whose instances create and supplement ChromeOptions",
            "for the starting of ChromeDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("CHROME_CAPABILITY_SUPPLIERS")
    CHROME(ChromeOptions::new),

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<EdgeOptions>",
            "whose instances create and supplement EdgeOptions",
            "for the starting of EdgeDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("EDGE_CAPABILITY_SUPPLIERS")
    EDGE(EdgeOptions::new),

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<FirefoxOptions>",
            "whose instances create and supplement FirefoxOptions",
            "for the starting of FirefoxDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("FIREFOX_CAPABILITY_SUPPLIERS")
    FIREFOX(FirefoxOptions::new),

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<InternetExplorerOptions>",
            "whose instances create and supplement InternetExplorerOptions",
            "for the starting of InternetExplorerDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("IE_CAPABILITY_SUPPLIERS")
    IE(InternetExplorerOptions::new),

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<OperaOptions>",
            "whose instances create and supplement OperaOptions",
            "for the starting of OperaDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("OPERA_CAPABILITY_SUPPLIERS")
    OPERA(OperaOptions::new),

    @PropertyDescription(description = {"Defines full names of classes that implement CapabilitySettingSupplier<SafariOptions>",
            "whose instances create and supplement SafariOptions",
            "for the starting of SafariDriver",
            "It is possible to define multiple comma-separated value"},
            section = "Selenium. Capabilities")
    @PropertyName("SAFARI_CAPABILITY_SUPPLIERS")
    SAFARI(SafariOptions::new);

    private final CapabilityReader capabilityReader;
    private final Supplier<MutableCapabilities> startingCapabilitiesSupplier;


    CapabilityTypes(Supplier<MutableCapabilities> startingCapabilitiesSupplier) {
        capabilityReader = new CapabilityReader(this);
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
                .forEach(capabilityConsumer -> capabilityConsumer.accept(desiredCapabilities));

        return desiredCapabilities;
    }

    @SafeVarargs
    @Override
    public final String readValuesToSet(Class<? extends CapabilitySettingSupplier<?>>... value) {
        return stream(value).map(Class::getName).collect(joining(","));
    }

    /**
     * It does nothing
     */
    @Deprecated
    @Override
    public MutableCapabilities parse(String value) {
        return null;
    }

    public enum CommonCapabilityProperties implements PropertySupplier<Object, Object> {
        @PropertyDescription(description = {"Defines the capability 'browserName'",
                "It has sense when value of the property 'WEB_DRIVER_TO_LAUNCH' is 'REMOTE_DRIVER'"},
                section = "Selenium. Capabilities")
        @PropertyName("WEB_DRIVER_CAPABILITY_BROWSER_NAME")
        BROWSER_NAME,

        /**
         * Reads property {@code web.driver.capability.platformName} and returns string value.
         * Should be the same as an item of {@link org.openqa.selenium.Platform}
         */
        @PropertyDescription(description = "Defines the capability 'platformName'",
                section = "Selenium. Capabilities")
        @PropertyName("WEB_DRIVER_CAPABILITY_PLATFORM_NAME")
        PLATFORM_NAME,

        @PropertyDescription(description = "Defines the capability 'javascriptEnabled'",
                section = "Selenium. Capabilities")
        @PropertyName("WEB_DRIVER_CAPABILITY_SUPPORTS_JAVASCRIPT")
        @PropertyDefaultValue("true")
        SUPPORTS_JAVASCRIPT {
            @Override
            public Boolean parse(String value) {
                return Boolean.parseBoolean(value);
            }
        },

        @PropertyDescription(description = "Defines the capability 'browserVersion'",
                section = "Selenium. Capabilities")
        @PropertyName("WEB_DRIVER_CAPABILITY_BROWSER_VERSION")
        BROWSER_VERSION;


        @Override
        public Object parse(String value) {
            return value;
        }
    }

    private static class CapabilityReader
            implements MultipleObjectPropertySupplier<Consumer<MutableCapabilities>, CapabilitySettingSupplier<MutableCapabilities>> {

        private final CapabilityTypes type;

        private CapabilityReader(CapabilityTypes type) {
            this.type = type;
        }

        @Override
        public String getName() {
            return type.getName();
        }
    }
}
