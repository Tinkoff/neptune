package ru.tinkoff.qa.neptune.http.api.properties;

import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

/**
 * This class is designed to read value of the property {@code 'END_POINT_OF_TARGET_API'} and convert it to an instance of
 * {@link java.net.URL}. This is a root URL that consists of protocol/scheme, host/ip and port only.
 */
public final class DefaultEndPointOfTargetAPIProperty implements URLValuePropertySupplier {

    /**
     * This instance reads value of the property {@code 'END_POINT_OF_TARGET_API'} and returns an {@link java.net.URL}
     */
    public static final DefaultEndPointOfTargetAPIProperty DEFAULT_END_POINT_OF_TARGET_API_PROPERTY =
            new DefaultEndPointOfTargetAPIProperty();
    private static final String PROPERTY_NAME = "END_POINT_OF_TARGET_API";

    private DefaultEndPointOfTargetAPIProperty() {
        super();
    }

    @Override
    public String getPropertyName() {
        return PROPERTY_NAME;
    }
}
