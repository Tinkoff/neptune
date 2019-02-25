package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import java.util.function.Consumer;

public class FirefoxSettingsSupplierWithProfile implements CapabilitySettingSupplier<FirefoxOptions> {

    public static final FirefoxProfile EMPTY_PROFILE = new FirefoxProfile();

    @Override
    public Consumer<FirefoxOptions> get() {
        return firefoxOptions -> firefoxOptions.setProfile(EMPTY_PROFILE);
    }
}
