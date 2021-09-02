package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.data.format.DataTransformer;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;

@PropertyDescription(description = {"Defines default serialization and deserialization",
        "of bodies of proxied http requests/responses"},
        section = "Selenium. Proxied browser traffic")
@PropertyName("PROXIED_TRAFFIC_BODY_TRANSFORMER")
public final class ProxiedTrafficBodyTransformer implements ObjectByClassPropertySupplier<DataTransformer> {

    public static final ProxiedTrafficBodyTransformer
            PROXIED_TRAFFIC_BODY_TRANSFORMER = new ProxiedTrafficBodyTransformer();

    private ProxiedTrafficBodyTransformer() {
        super();
    }
}