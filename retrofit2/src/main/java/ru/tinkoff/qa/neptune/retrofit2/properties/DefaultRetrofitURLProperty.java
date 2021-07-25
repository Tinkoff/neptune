package ru.tinkoff.qa.neptune.retrofit2.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.url.URLValuePropertySupplier;

@PropertyDescription(description = {"Default URL of an application to connect",
        "It is recommended to define it as a String which consists of protocol, host and port",
        "Value should end with '/'"},
        section = "Retrofit")
@PropertyName("DEFAULT_RETROFIT_URL")
public final class DefaultRetrofitURLProperty implements URLValuePropertySupplier {

    /**
     * This instance reads value of the property {@code 'DEFAULT_RETROFIT_URL'} and returns an {@link java.net.URL}
     */
    public static final DefaultRetrofitURLProperty DEFAULT_RETROFIT_URL_PROPERTY =
            new DefaultRetrofitURLProperty();

    private DefaultRetrofitURLProperty() {
        super();
    }
}
