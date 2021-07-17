package ru.tinkoff.qa.neptune.retrofit2.tests;

import ru.tinkoff.qa.neptune.core.api.binding.Bind;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService2;
import ru.tinkoff.qa.neptune.retrofit2.tests.services.customized.CustomService4;

public enum CustomUrls implements URLValuePropertySupplier {

    @Bind(to = CustomService2.class, withSubclasses = true)
    @PropertyName("URL_1")
    URL1,

    @Bind(to = CustomService4.class, withSubclasses = true)
    @PropertyName("URL_2")
    URL2
}
