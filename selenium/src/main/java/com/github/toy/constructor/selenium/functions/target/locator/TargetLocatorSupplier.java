package com.github.toy.constructor.selenium.functions.target.locator;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;

public abstract class TargetLocatorSupplier<T, S extends TargetLocatorSupplier<T, S>> extends GetSupplier<SeleniumSteps, T, S> {
}
