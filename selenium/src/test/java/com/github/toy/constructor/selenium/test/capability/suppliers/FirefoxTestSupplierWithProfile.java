package com.github.toy.constructor.selenium.test.capability.suppliers;

import com.github.toy.constructor.selenium.properties.AdditionalCapabilitiesFor;
import com.github.toy.constructor.selenium.properties.CapabilitySupplier;
import org.openqa.selenium.Capabilities;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;

import static com.github.toy.constructor.selenium.properties.CapabilityTypes.FIREFOX;

@AdditionalCapabilitiesFor(type = FIREFOX, supplierName = "withProfile")
public class FirefoxTestSupplierWithProfile implements CapabilitySupplier {
    @Override
    public Capabilities get() {
        return new FirefoxOptions().setProfile(new FirefoxProfile());
    }
}
