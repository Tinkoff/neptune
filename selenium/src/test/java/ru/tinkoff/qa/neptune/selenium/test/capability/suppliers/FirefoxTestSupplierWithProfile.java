package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import ru.tinkoff.qa.neptune.selenium.properties.AdditionalCapabilitiesFor;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySupplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.FIREFOX;

@AdditionalCapabilitiesFor(type = FIREFOX, supplierName = "withProfile")
public class FirefoxTestSupplierWithProfile implements CapabilitySupplier {

    public static final FirefoxProfile EMPTY_PROFILE = new FirefoxProfile();

    @Override
    public Capabilities get() {
        return new FirefoxOptions().setProfile(EMPTY_PROFILE);
    }
}
