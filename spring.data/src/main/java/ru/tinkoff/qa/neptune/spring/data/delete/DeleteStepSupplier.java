package ru.tinkoff.qa.neptune.spring.data.delete;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.spring.data.SpringDataContext;

import java.util.function.Function;

public class DeleteStepSupplier extends SequentialGetStepSupplier.GetSimpleStepSupplier<SpringDataContext, Void, DeleteStepSupplier> {

    protected DeleteStepSupplier(Function<SpringDataContext, Void> originalFunction) {
        super(originalFunction);
    }
}
