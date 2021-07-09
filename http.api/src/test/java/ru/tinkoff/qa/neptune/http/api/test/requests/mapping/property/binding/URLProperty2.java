package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

@PropertyDefaultValue("http://127.0.0.1:8090")
@PropertyName("property2")
@Bind(to = ServiceAPI2.class, withSubclasses = true, exclude = {
        ServiceAPI5.class,
        ServiceAPI6.class})
public class URLProperty2 implements URLValuePropertySupplier {
}
