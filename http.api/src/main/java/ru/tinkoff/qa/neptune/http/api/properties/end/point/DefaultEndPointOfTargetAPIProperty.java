package ru.tinkoff.qa.neptune.http.api.properties.end.point;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

@PropertyDescription(description = {"Default URI of an application to connect",
        "It is recommended to define it as an URI which consists of schema, host, port (optionally)",
        "and context path (optionally)"},
        section = "Http client. Endpoint")
@PropertyName("END_POINT_OF_TARGET_API")
public final class DefaultEndPointOfTargetAPIProperty implements URLValuePropertySupplier {

    /**
     * This instance reads value of the property {@code 'END_POINT_OF_TARGET_API'} and returns an {@link java.net.URL}
     */
    public static final DefaultEndPointOfTargetAPIProperty DEFAULT_END_POINT_OF_TARGET_API_PROPERTY =
            new DefaultEndPointOfTargetAPIProperty();

    private DefaultEndPointOfTargetAPIProperty() {
        super();
    }
}
