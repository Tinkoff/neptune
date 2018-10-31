package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import org.openqa.selenium.MutableCapabilities;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySupplier;
import org.openqa.selenium.chrome.ChromeOptions;

public class ChromeTestSupplierWithOptions implements CapabilitySupplier {
    @Override
    public MutableCapabilities get() {
        return new ChromeOptions().addArguments("--use-fake-device-for-media-stream",
                "--start-maximized", "--enable-automation",
                "--disable-web-security");
    }
}
