package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;
import java.util.function.Consumer;

public class ChromeSettingsSupplierWithExperimentalOption implements CapabilitySettingSupplier<ChromeOptions> {
    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions -> chromeOptions.setExperimentalOption("experimentalOption", new HashMap<>())
                .addArguments("--use-fake-device-for-media-stream",
                        "--start-maximized", "--enable-automation",
                        "--disable-web-security");
    }
}
