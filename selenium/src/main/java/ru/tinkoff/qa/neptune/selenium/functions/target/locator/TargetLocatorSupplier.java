package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import ru.tinkoff.qa.neptune.selenium.SeleniumStepPerformer;

import java.util.function.Function;
import java.util.function.Supplier;

public interface TargetLocatorSupplier<T> extends Supplier<Function<SeleniumStepPerformer, T>> {
    @Override
    Function<SeleniumStepPerformer, T> get();
}
