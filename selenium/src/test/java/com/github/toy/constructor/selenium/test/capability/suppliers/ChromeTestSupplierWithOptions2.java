package com.github.toy.constructor.selenium.test.capability.suppliers;

import com.github.toy.constructor.selenium.properties.AdditionalCapabilitiesFor;
import com.github.toy.constructor.selenium.properties.CapabilitySupplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeOptions;

import static com.github.toy.constructor.selenium.properties.CapabilityTypes.CHROME;

@AdditionalCapabilitiesFor(type = CHROME, supplierName = "withArguments2")
public class ChromeTestSupplierWithOptions2 implements CapabilitySupplier {
    @Override
    public Capabilities get() {
        return new ChromeOptions().addArguments(
                "--use-fake-ui-for-media-stream");
    }
}
