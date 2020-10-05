package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.http.api.service.mapping.APIUses;

@PropertyDefaultValue("http://127.0.0.1:8089")
@PropertyName("property1")
@APIUses(usedBy = ServiceAPI.class)
public class URLProperty1 implements URLValuePropertySupplier {
}
