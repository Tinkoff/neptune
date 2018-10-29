package ru.tinkoff.qa.neptune.selenium.test.capability.suppliers;

import org.openqa.selenium.MutableCapabilities;
import ru.tinkoff.qa.neptune.selenium.properties.AdditionalCapabilitiesFor;
import ru.tinkoff.qa.neptune.selenium.properties.CapabilitySupplier;
import org.openqa.selenium.chrome.ChromeOptions;

import java.util.HashMap;

import static ru.tinkoff.qa.neptune.selenium.properties.CapabilityTypes.CHROME;

@AdditionalCapabilitiesFor(type = CHROME, supplierName = "withExperimental")
public class ChromeTestSupplierWithExperimentalOption implements CapabilitySupplier {
    @Override
    public MutableCapabilities get() {
        return new ChromeOptions().setExperimentalOption("experimentalOption", new HashMap<>())
                .addArguments("--use-fake-device-for-media-stream",
                "--start-maximized", "--enable-automation",
                "--disable-web-security");
    }
}
