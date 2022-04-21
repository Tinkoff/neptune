package ru.tinkoff.qa.neptune.hibernate.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.util.Collections;
import java.util.List;

import static org.apache.commons.lang3.StringUtils.isBlank;


@PropertyDescription(description = {"Defines persistence units names from META-INF/persistence.xml " +
        "which will be initialized as hibernate datasources"},
        section = "Database. Hibernate properties")
@PropertyName("PERSISTENCE_UNITS")
public final class PersistenceUnits implements PropertySupplier<List<String>, String> {

    public static final PersistenceUnits PERSISTENCE_UNITS = new PersistenceUnits();

    private PersistenceUnits() {
    }

    @Override
    public List<String> parse(String value) {
        var trimmed = value.trim();
        if (isBlank(trimmed)) {
            return Collections.emptyList();
        }
        return List.of(trimmed.split(","));
    }
}
