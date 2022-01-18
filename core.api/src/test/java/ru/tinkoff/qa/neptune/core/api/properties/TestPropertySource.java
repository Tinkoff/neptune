package ru.tinkoff.qa.neptune.core.api.properties;

import java.util.Properties;

public class TestPropertySource implements PropertySource {

    private final Properties properties;

    public TestPropertySource() {
        properties = new Properties();
        properties.setProperty("propertyA", "A");
        properties.setProperty("propertyB", "B");
    }

    @Override
    public String getProperty(String property) {
        return properties.getProperty(property);
    }

    @Override
    public boolean isPropertyDefined(String property) {
        return properties.containsKey(property);
    }
}
