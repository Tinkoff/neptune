package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

@PropertyDefaultValue("http://127.0.0.1:8089")
@PropertyName("property1")
@Bind(to = ServiceAPI.class)
public class URLProperty1 implements URLValuePropertySupplier {
}
