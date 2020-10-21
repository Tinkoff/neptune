package ru.tinkoff.qa.neptune.core.api.properties;

import java.io.*;
import java.nio.file.Path;
import java.util.Properties;

import static java.lang.ClassLoader.getSystemClassLoader;
import static java.lang.String.valueOf;
import static java.lang.System.getProperties;
import static java.lang.System.setProperty;
import static java.lang.Thread.currentThread;
import static java.util.Arrays.copyOfRange;
import static java.util.Objects.isNull;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;

public final class GeneralPropertyInitializer {

    public static final String PROPERTIES = "neptune.properties";
    public static final String GLOBAL_PROPERTIES = "neptune.global.properties";
    static boolean arePropertiesRead;

    private GeneralPropertyInitializer() {
        super();
    }

    private static InputStream getResourceInputStream(String name) {
        return ofNullable(getSystemClassLoader()
                .getResourceAsStream(name))
                .orElseGet(() -> currentThread().getContextClassLoader().getResourceAsStream(name));
    }

    private static synchronized InputStream findGlobalProperties(String... path) {
        if (path == null || path.length == 0) {
            return ofNullable(getResourceInputStream(GLOBAL_PROPERTIES))
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
        } else {
            Path p;
            if (path.length == 1) {
                p = Path.of(path[0]);
            } else {
                p = Path.of(path[0], add(copyOfRange(path, 1, path.length), GLOBAL_PROPERTIES));
            }

            var f = p.toFile();
            if (f.exists()) {
                try {
                    return new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return findGlobalProperties();
            }
        }
    }

    private static synchronized InputStream findLocalProperties(String... path) {
        if (path == null || path.length == 0) {
            return ofNullable(getResourceInputStream(PROPERTIES))
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
        } else {
            Path p;
            if (path.length == 1) {
                p = Path.of(path[0]);
            } else {
                p = Path.of(path[0], add(copyOfRange(path, 1, path.length), PROPERTIES));
            }

            var f = p.toFile();
            if (f.exists()) {
                try {
                    return new FileInputStream(f);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }
            } else {
                return findLocalProperties();
            }
        }
    }

    private static void checkSystemPropertyAndFillIfNecessary(String propertyName, String valueToSet) {
        if (isBlank(valueToSet)) {
            return;
        }
        if (getProperties().containsKey(propertyName)) {
            return;
        }
        setProperty(propertyName, valueToSet);
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

    private static synchronized void setUpProperties(Properties prop) {
        prop.forEach((key, value) -> checkSystemPropertyAndFillIfNecessary(valueOf(key),
                nonNull(value) ? valueOf(value) : EMPTY));
    }

    private static Properties mergeProperties(Properties properties1, Properties properties2) {
        properties2.forEach((key, value) -> properties1.setProperty(valueOf(key), valueOf(value)));
        return properties1;
    }

    /**
     * @return properties defined in {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES}
     */
    public static Properties getGlobalProperties(String... path) {
        return propertiesFromStream(findGlobalProperties(path));
    }

    /**
     * @return properties defined in {@link GeneralPropertyInitializer#PROPERTIES}
     */
    public static Properties getLocalProperties(String... path) {
        return propertiesFromStream(findLocalProperties(path));
    }

    /**
     * @return merged properties defined in {@link GeneralPropertyInitializer#PROPERTIES} and {@link GeneralPropertyInitializer#GLOBAL_PROPERTIES}
     */
    public static Properties getAllProperties(String... path) {
        var globalProps = getGlobalProperties(path);
        var localProps = getLocalProperties(path);

        if (isNull(globalProps) && isNull(localProps)) {
            return null;
        }

        if (isNull(globalProps)) {
            return localProps;
        } else if (isNull(localProps)) {
            return globalProps;
        } else {
            return mergeProperties(globalProps, localProps);
        }
    }

    /**
     * Reads properties defined in {@link #PROPERTIES} which is located in any folder of the project
     * and instantiates system properties.
     */
    public static synchronized void refreshProperties() {
        ofNullable(getAllProperties()).ifPresent(GeneralPropertyInitializer::setUpProperties);
        arePropertiesRead = true;
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
