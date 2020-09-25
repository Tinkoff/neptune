package ru.tinkoff.qa.neptune.core.api.properties.general.resorces;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;

@PropertyDescription(description = "Is it necessary to free unused resources, e.g. opened browser, connections to databases etc.",
        section = "General properties. Resource management")
@PropertyName("TO_FREE_RESOURCES_ON_INACTIVITY")
public class FreeResourcesOnInactivity implements BooleanValuePropertySupplier {

    private FreeResourcesOnInactivity() {
        super();
    }
    /**
     * Reads the property value and is used to get access to its value.
     */
    public static final FreeResourcesOnInactivity TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY = new FreeResourcesOnInactivity();
}
