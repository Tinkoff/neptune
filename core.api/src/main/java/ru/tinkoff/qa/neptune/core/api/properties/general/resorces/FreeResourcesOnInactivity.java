package ru.tinkoff.qa.neptune.core.api.properties.general.resorces;

import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;

/**
 * This property means that it is necessary to free unused resources, e.g. opened browser, connections to databases etc.
 * It may be useful when tests are run in multiple threads. The name of the property is {@code 'to.free.resources.on.inactivity'}
 */
public class FreeResourcesOnInactivity implements BooleanValuePropertySupplier {

    private FreeResourcesOnInactivity() {
        super();
    }

    private static final String PROPERTY_NAME = "to.free.resources.on.inactivity";

    /**
     * Reads the property value and is used to get access to its value.
     */
    public static final FreeResourcesOnInactivity TO_FREE_RESOURCES_ON_INACTIVITY_PROPERTY = new FreeResourcesOnInactivity();

    @Override
    public String getPropertyName() {
        return PROPERTY_NAME;
    }
}
