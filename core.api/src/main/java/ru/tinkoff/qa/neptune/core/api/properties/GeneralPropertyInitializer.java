package ru.tinkoff.qa.neptune.core.api.properties;

import java.io.*;
import java.util.Properties;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.*;

public final class GeneralPropertyInitializer {

    public static final String PROPERTIES = "neptune.properties";
    public static final String GLOBAL_PROPERTIES = "neptune.global.properties";
    static boolean arePropertiesRead;

    private GeneralPropertyInitializer() {
        super();
    }

    private static synchronized InputStream findGlobalProperties() {
        return ofNullable(getSystemClassLoader()
                .getResourceAsStream(PROPERTIES))
                .orElseGet(() -> {
                    //try to find it in the root directory of the project
                    var f = new File(GLOBAL_PROPERTIES).getAbsoluteFile();
                    while (!f.exists()) {
                        var parent = f.getParentFile().getParentFile();
                        if (parent == null) {
                            break;
                        }
                        f = new File(parent, GLOBAL_PROPERTIES).getAbsoluteFile();
                    }

                    if (f.exists()) {
                        try {
                            return new FileInputStream(f);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return null;
                });
    }

    private static synchronized InputStream findLocalProperties() {
        return ofNullable(getSystemClassLoader()
                .getResourceAsStream(PROPERTIES))
                .orElseGet(() -> {
                    //try to find it in the root directory of the project/module
                    var f = new File(PROPERTIES);

                    if (f.exists()) {
                        try {
                            return new FileInputStream(f);
                        } catch (FileNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }

                    return null;
                });
    }

    private static void checkSystemPropertyAndFillIfNecessary(String propertyName, String valueToSet) {
        if (isBlank(valueToSet)) {
            return;
        }

        ofNullable(getProperty(propertyName))
                .ifPresentOrElse(s -> {
                    if (isBlank(s)) {
                        setProperty(propertyName, valueToSet);
                    }
                }, () -> setProperty(propertyName, valueToSet));
    }

    private static Properties propertiesFromStream(InputStream is) {
        return ofNullable(is)
                .map(inputStream -> {
                    var prop = new Properties();
                    try (inputStream) {
                        prop.load(inputStream);
                        return prop;
                    } catch (IOException e) {
                        throw new PropertyReadException(e.getMessage(), e);
                    }
                })
                .orElse(null);
    }

    private static void setUpProperties(Properties prop) {
        prop.forEach((key, value) -> checkSystemPropertyAndFillIfNecessary(valueOf(key),
                nonNull(value) ? valueOf(value) : EMPTY));
    }

    private static Properties mergeProperties(Properties properties1, Properties properties2) {
        properties2.forEach((key, value) -> {
            if (nonNull(value) && isNotBlank(valueOf(value))) {
                properties1.setProperty(valueOf(key), valueOf(value));
            }
        });
        return properties1;
    }

    private static synchronized void refreshProperties(InputStream global, InputStream local) {
        if (isNull(global) && isNull(local)) {
            return;
        }

        var globalProperties = propertiesFromStream(global);
        var localProperties = propertiesFromStream(local);

        if (isNull(globalProperties)) {
            setUpProperties(localProperties);
        }
        else if (isNull(localProperties)) {
            setUpProperties(globalProperties);
        }
        else {
            setUpProperties(mergeProperties(globalProperties, localProperties));
        }
    }

    /**
     * Reads properties defined in {@link #PROPERTIES} which is located in any folder of the project
     * and instantiates system properties.
     */
    public static synchronized void refreshProperties() {
        //Firstly we try to read properties from resources
        if (!arePropertiesRead) {
            var global = findGlobalProperties();
            var local = findLocalProperties();
            refreshProperties(global, local);
            arePropertiesRead = true;
        }
    }

    static boolean arePropertiesRead() {
        return arePropertiesRead;
    }

    private static class PropertyReadException extends RuntimeException {
        private PropertyReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
