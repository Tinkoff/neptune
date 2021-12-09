package ru.tinkoff.qa.neptune.hibernate.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;

@PropertyDescription(description = {"Defines if framework should use JPA configuration with persistence.xml ",
        "instead of hibernate .cfg files"},
        section = "Database. Hibernate properties")
@PropertyName("USE_JPA_CONFIG")
@PropertyDefaultValue("false")
public final class UseJpaConfig implements BooleanValuePropertySupplier {

    public static final UseJpaConfig USE_JPA_CONFIG = new UseJpaConfig();

    private UseJpaConfig() {
    }
}
