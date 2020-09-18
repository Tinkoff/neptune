package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import org.openqa.selenium.chrome.ChromeOptions;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySettingSupplier;

import java.util.function.Consumer;

public class ChromeSettingsSupplierForProxy implements CapabilitySettingSupplier<ChromeOptions> {

    @Override
    public Consumer<ChromeOptions> get() {
        return chromeOptions ->
                chromeOptions.addArguments("--headless")
                        .addArguments("--disable-extensions")
                        .addArguments("--accept-insecure-localhost")
                        .addArguments("--ignore-certificate-errors");
    }
}
