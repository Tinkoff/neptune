package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;


@PropertyDescription(description = "Defines objects which provide required cookies",
        section = "Selenium. Initial cookies")
@PropertyName("REQUIRED_COOKIE_PROVIDERS")
public final class RequiredCookieProviders implements MultipleObjectPropertySupplier<RequiredCookieSupplier> {

    public static final RequiredCookieProviders REQUIRED_COOKIE_PROVIDERS = new RequiredCookieProviders();

    private RequiredCookieProviders() {
        super();
    }
}
