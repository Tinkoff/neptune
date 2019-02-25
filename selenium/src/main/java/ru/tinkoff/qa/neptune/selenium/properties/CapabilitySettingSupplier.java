package ru.tinkoff.qa.neptune.selenium.properties;

import org.openqa.selenium.MutableCapabilities;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface CapabilitySettingSupplier<T extends MutableCapabilities> extends Supplier<Consumer<T>> {
}
