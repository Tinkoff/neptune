package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import org.openqa.selenium.MutableCapabilities;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySupplier;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

public class FirefoxTestSupplierWithProfile implements CapabilitySupplier {

    public static final FirefoxProfile EMPTY_PROFILE = new FirefoxProfile();

    @Override
    public MutableCapabilities get() {
        return new FirefoxOptions().setProfile(EMPTY_PROFILE);
    }
}
