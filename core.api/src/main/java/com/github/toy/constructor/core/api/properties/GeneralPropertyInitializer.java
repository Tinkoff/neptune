package com.github.toy.constructor.core.api.properties;

import java.io.*;
import java.util.Properties;

import static java.lang.String.valueOf;
import static java.lang.System.getProperty;
import static java.lang.System.setProperty;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class GeneralPropertyInitializer {

    public static final String GENERAL_PROPERTIES = "general.properties";

    private GeneralPropertyInitializer() {
        super();
    }

    private static File findPropertyFile() {
        return findPropertyFile(new File(GENERAL_PROPERTIES).getAbsolutePath().replace(GENERAL_PROPERTIES, ""));
    }

    private static File findPropertyFile(String startPath) {
        // attempt to find configuration in the specified directory
        File defaultConfig = new File(startPath);
        File list[] = defaultConfig
                .listFiles((dir, name) -> name
                        .endsWith(GENERAL_PROPERTIES));

        if (list != null && list.length > 0) {
            return list[0];
        }

        if (list != null && list.length == 0) {
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

    /**
     * Reads properties defined in a file and instantiates system properties.
     * @param file is a file to read.
     */
    public static void refreshProperties(File file) {
        Properties prop = new Properties();
        FileInputStream input;
        try {
            input = new FileInputStream(file);
            prop.load(input);
            input.close();
        } catch (IOException e) {
            throw new PropertyReadException(e.getMessage(), e);
        }

        prop.forEach((key, value) -> checkSystemPropertyAndFillIfNecessary(valueOf(key),
                value != null ? valueOf(value) : EMPTY));
    }

    /**
     * Reads properties defined in {@link #GENERAL_PROPERTIES} which is located in any folder of the project
     * and instantiates system properties.
     */
    public static void refreshProperties() {
        File propertyFile = findPropertyFile();
        if (propertyFile != null) {
            refreshProperties(propertyFile);
        }
    }


    static {
        refreshProperties();
    }

    private static class PropertyReadException extends RuntimeException {
        private PropertyReadException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
