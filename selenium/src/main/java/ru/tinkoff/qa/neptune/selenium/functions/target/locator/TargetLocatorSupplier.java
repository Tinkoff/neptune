package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

import java.util.function.Function;
import java.util.function.Supplier;

public interface TargetLocatorSupplier<T> extends Supplier<Function<SeleniumStepContext, T>> {
    @Override
    Function<SeleniumStepContext, T> get();
}
