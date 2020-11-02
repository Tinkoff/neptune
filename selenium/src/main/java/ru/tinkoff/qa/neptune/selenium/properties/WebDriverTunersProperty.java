package ru.tinkoff.qa.neptune.selenium.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.MultipleObjectPropertySupplier;

import java.util.function.Supplier;

@PropertyDescription(description = {"Defines classes of objects which apply customer settings on a new WebDriver session",
        "and/or every time after invocation of SeleniumStepContext#refresh()",
        "It is possible to define multiple classes as a string of full class names separated by comma"},
        section = "Selenium. Web driver tuners")
@PropertyName("WEB_DRIVER_TUNERS")
public final class WebDriverTunersProperty implements MultipleObjectPropertySupplier<Supplier<WebDriverTuner>> {

    public static final WebDriverTunersProperty WEB_DRIVER_TUNERS_PROPERTY = new WebDriverTunersProperty();

    private WebDriverTunersProperty() {
        super();
    }
}
