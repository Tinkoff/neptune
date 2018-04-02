package com.github.toy.constructor.selenium.functions.target.locator;

import com.github.toy.constructor.selenium.SeleniumSteps;

import java.util.function.Function;
import java.util.function.Supplier;

public interface TargetLocatorSupplier<T> extends Supplier<Function<SeleniumSteps, T>> {
    @Override
    Function<SeleniumSteps, T> get();
}
