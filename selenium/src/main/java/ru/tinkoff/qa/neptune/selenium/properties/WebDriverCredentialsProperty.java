package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;
import ru.tinkoff.qa.neptune.selenium.authentication.BrowserCredentials;

import java.util.function.Supplier;

@PropertyDescription(description = {"Defines a class of an object which supplies an instance of ru.tinkoff.qa.neptune.selenium.authentication.CredentialContainer",
        "This instance performs login/authentication in a browser"},
        section = "Selenium. Web driver credentials")
@PropertyName("WEB_DRIVER_CREDENTIALS")
public final class WebDriverCredentialsProperty implements ObjectPropertySupplier<Supplier<BrowserCredentials<?>>> {

    public static final WebDriverCredentialsProperty WEB_DRIVER_CREDENTIALS_PROPERTY = new WebDriverCredentialsProperty();

    private WebDriverCredentialsProperty() {
        super();
    }
}
