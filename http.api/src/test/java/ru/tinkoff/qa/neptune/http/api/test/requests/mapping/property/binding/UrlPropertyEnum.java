package ru.tinkoff.qa.neptune.http.api.test.requests.mapping.property.binding;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

public enum UrlPropertyEnum implements URLValuePropertySupplier {
    @PropertyDefaultValue("http://127.0.0.1:8090")
    @PropertyName("property3")
    @Bind(to = ServiceAPI6.class)
    URL_PROPERTY3
}
