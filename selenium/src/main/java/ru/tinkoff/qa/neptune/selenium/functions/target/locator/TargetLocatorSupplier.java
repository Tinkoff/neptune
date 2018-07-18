package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;

import java.util.function.Function;
import java.util.function.Supplier;

public interface TargetLocatorSupplier<T> extends Supplier<Function<SeleniumSteps, T>> {
    @Override
    Function<SeleniumSteps, T> get();
}
