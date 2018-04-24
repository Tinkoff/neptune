package com.github.toy.constructor.selenium.properties;

import com.github.toy.constructor.core.api.PropertySupplier;

import java.io.*;
import java.util.Properties;
import java.util.function.Function;

import static com.github.toy.constructor.selenium.properties.SupportedWebDriverPropertyProperty.SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class SeleniumPropertyInitializer {

    public static final String SELENIUM_PROPERTY_FILE = "selenium.properties";

    static {
        try {
            refreshProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private SeleniumPropertyInitializer() {
        super();
    }

    private static File findPropertyFile() {
        return findPropertyFile(new File(SELENIUM_PROPERTY_FILE).getAbsolutePath().replace(SELENIUM_PROPERTY_FILE, ""));
    }

    private static File findPropertyFile(String startPath) {
        // attempt to find configuration in the specified directory
        File defaultConfig = new File(startPath);
        File list[] = defaultConfig
                .listFiles((dir, name) -> name
                        .endsWith(SELENIUM_PROPERTY_FILE));

        if (list.length > 0) {
            return list[0];
        }

        if (list.length == 0) {
            File inner[] = defaultConfig.listFiles();
            File result = null;
            for (File element : inner) {
                if (element.isDirectory()) {
                    result = findPropertyFile(element.getPath());
                }
                if (result != null) {
                    return result;
                }
            }
        }
        return null;
    }

    private static void checkPropertiesAndFillIfNecessary(Properties properties, PropertySupplier<?>[] propertySuppliers) {
        stream(propertySuppliers).forEach(propertySupplier ->
                ofNullable(properties.getProperty(propertySupplier.getPropertyName()))
                        .ifPresent(s -> {
                            if (isBlank(propertySupplier.returnOptionalFromEnvironment().map(String::trim)
                                    .orElse(null))) {
                                System.setProperty(propertySupplier.getPropertyName(), s);
                            }
                        }));
    }

    public static void refreshProperties() throws IOException  {
        File propertyFile = findPropertyFile();
        if (propertyFile == null) {
            return;
        }

        Properties prop = new Properties();
        FileInputStream input = new FileInputStream(propertyFile);
        prop.load(input);
        checkPropertiesAndFillIfNecessary(prop, WaitingProperties.TimeValueProperties.values());
        checkPropertiesAndFillIfNecessary(prop, WaitingProperties.TimeUnitProperties.values());
        checkPropertiesAndFillIfNecessary(prop, URLProperties.values());
        checkPropertiesAndFillIfNecessary(prop, FlagProperties.values());
        checkPropertiesAndFillIfNecessary(prop, CapabilityTypes.CommonCapabilityProperties.values());
        checkPropertiesAndFillIfNecessary(prop, CapabilityTypes.values());
        checkPropertiesAndFillIfNecessary(prop, new PropertySupplier[] {SUPPORTED_WEB_DRIVER_PROPERTY_PROPERTY});
        input.close();
    }
}
