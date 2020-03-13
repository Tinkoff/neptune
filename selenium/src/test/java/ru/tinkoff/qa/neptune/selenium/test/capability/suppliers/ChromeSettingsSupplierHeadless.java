package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import org.openqa.selenium.chrome.ChromeOptions;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;

import java.util.function.Consumer;

public class ChromeSettingsSupplierHeadless implements CapabilitySettingSupplier<ChromeOptions> {
    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> chromeOptions.addArguments("--headless");
    }
}
