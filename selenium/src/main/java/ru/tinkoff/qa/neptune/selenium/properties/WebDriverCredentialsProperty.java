package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;
import ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials;

@PropertyDescription(description = {"Defines a subclass of ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials",
        "whose instance performs login/authentication in a browser"},
        section = "Selenium. Web driver credentials")
@PropertyName("WEB_DRIVER_CREDENTIALS")
public final class WebDriverCredentialsProperty
        implements ObjectByClassPropertySupplier<BrowserCredentials<?>> {

    public static final WebDriverCredentialsProperty WEB_DRIVER_CREDENTIALS_PROPERTY = new WebDriverCredentialsProperty();

    private WebDriverCredentialsProperty() {
        super();
    }
}
