package com.github.toy.constructor.selenium.properties;

import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.function.Supplier;

public interface CapabilitySupplier extends Supplier<DesiredCapabilities> {
}
