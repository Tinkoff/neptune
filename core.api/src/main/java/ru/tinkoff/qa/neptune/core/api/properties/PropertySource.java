package ru.tinkoff.qa.neptune.core.api.properties;

import org.apache.commons.lang3.StringUtils;

import java.util.LinkedList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.System.getProperties;
import static java.lang.System.getenv;
import static java.util.ServiceLoader.load;
import static java.util.stream.Collectors.toCollection;
import static java.util.stream.StreamSupport.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.arePropertiesRead;
import static ru.tinkoff.qa.neptune.core.api.properties.GeneralPropertyInitializer.refreshProperties;

public interface PropertySource {

    List<PropertySource> PROPERTY_SOURCES = getPropertySources();

    static List<PropertySource> getPropertySources() {
        var iterator = load(PropertySource.class).iterator();
        Iterable<PropertySource> iterable = () -> iterator;
        var list = stream(iterable.spliterator(), false).collect(toCollection(LinkedList::new));
        list.addLast(new SystemEnvPropertySource());
        list.addLast(new SystemPropertySource());
        return list;
    }

    /**
     * Returns value of the property. It searches for a value across all known {@link PropertySource}'s
     *
     * @param property is name of the property
     * @return string value if the property is defined or null in other cases
     */
    static String getPropertyValue(String property) {
        checkArgument(isNotBlank(property), "Property name should not be a null or blank string");
        return PROPERTY_SOURCES
                .stream()
                .map(s -> s.getProperty(property))
                .filter(StringUtils::isNotBlank)
                .findFirst()
                .orElse(null);
    }

    /**
     * Returns is property with defined name present or not. It searches across all known {@link PropertySource}'s
     *
     * @param property is name of the property
     * @return {@code true} if the property with defined name present or {@code false} in other cases
     */
    static boolean isPropertyKeyPresent(String property) {
        checkArgument(isNotBlank(property), "Property name should not be a null or blank string");
        return PROPERTY_SOURCES
                .stream()
                .anyMatch(s -> s.isPropertyDefined(property));
    }

    /**
     * Returns value of the property
     *
     * @param property is name of the property
     * @return string value if the property is defined or null in other cases
     */
    String getProperty(String property);

    /**
     * Returns is property with defined name present or not
     *
     * @param property is name of the property
     * @return {@code true} if the property with defined name present or {@code false} in other cases
     */
    boolean isPropertyDefined(String property);

    final class SystemPropertySource implements PropertySource {

        private SystemPropertySource() {
            super();
        }

        @Override
        public String getProperty(String property) {
            if (!arePropertiesRead()) {
                refreshProperties();
            }

            return System.getProperty(property);
        }

        @Override
        public boolean isPropertyDefined(String property) {
            if (!arePropertiesRead()) {
                refreshProperties();
            }

            return getProperties().containsKey(property);
        }
    }

    final class SystemEnvPropertySource implements PropertySource {

        private SystemEnvPropertySource() {
            super();
        }

        @Override
        public String getProperty(String property) {
            if (!arePropertiesRead()) {
                refreshProperties();
            }

            return getenv(property);
        }

        @Override
        public boolean isPropertyDefined(String property) {
            if (!arePropertiesRead()) {
                refreshProperties();
            }

            return getenv().containsKey(property);
        }
    }
}
