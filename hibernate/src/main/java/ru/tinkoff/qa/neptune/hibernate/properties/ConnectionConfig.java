package ru.tinkoff.qa.neptune.hibernate.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

public enum ConnectionConfig implements PropertySupplier<List<String>, String> {
    @PropertyDescription(description = {"Defines relative locations of hibernate configuration files (.cfg) " +
        "which will be used to build Hibernate SessionFactory"},
        section = "Database. Hibernate properties")
    @PropertyName("HIBERNATE_CONFIG_FILENAMES")
    HIBERNATE_CONFIG_FILENAMES,

    @PropertyDescription(description = {"Defines persistence units names from META-INF/persistence.xml " +
        "which will be initialized as hibernate datasources"},
        section = "Database. Hibernate properties")
    @PropertyName("PERSISTENCE_UNITS")
    PERSISTENCE_UNITS;


    @Override
    public List<String> parse(String value) {
        var trimmed = value.trim();
        if (isBlank(trimmed)) {
            return Collections.emptyList();
        }
        return List.of(trimmed.split(","));
    }
}
