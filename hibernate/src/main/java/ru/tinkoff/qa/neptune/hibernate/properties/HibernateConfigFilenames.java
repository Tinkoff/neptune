package ru.tinkoff.qa.neptune.hibernate.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;

@PropertyDescription(description = {"Defines relative locations of hibernate configuration files (.cfg) " +
        "which will be used to build Hibernate SessionFactory"},
        section = "Database. Hibernate properties")
@PropertyName("HIBERNATE_CONFIG_FILENAMES")
public final class HibernateConfigFilenames implements PropertySupplier<List<String>, String> {

    public static final HibernateConfigFilenames HIBERNATE_CONFIG_FILENAMES = new HibernateConfigFilenames();

    private HibernateConfigFilenames() {
        super();
    }

    @Override
    public List<String> parse(String value) {
        var trimmed = value.trim();
        if (isBlank(trimmed)) {
            return null;
        }
        return List.of(trimmed.split(","));
    }
}

