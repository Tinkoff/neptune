package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.function.Consumer;

public class ChromeSettingsSupplierWithBinary implements CapabilitySettingSupplier<ChromeOptions> {
    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> chromeOptions.setBinary("path/to/file");
    }
}
