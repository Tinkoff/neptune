package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;

import java.util.function.Function;

@MaxDepthOfReporting(0)
class CalculateGetSupplier<T, R> extends SequentialGetStepSupplier.GetObjectStepSupplier<T, R, CalculateGetSupplier<T, R>> {
    protected CalculateGetSupplier(Function<T, R> originalFunction) {
        super(originalFunction);
    }

    @Override
    protected CalculateGetSupplier<T, R> setDescription(String description) {
        return super.setDescription(description);
    }
}
