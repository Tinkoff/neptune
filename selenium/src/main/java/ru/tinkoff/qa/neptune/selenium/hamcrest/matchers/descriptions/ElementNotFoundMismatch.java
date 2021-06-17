package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("{search} not found")
public final class ElementNotFoundMismatch extends MismatchDescriber {

    @DescriptionFragment("search")
    final SequentialGetStepSupplier<?, ?, ?, ?, ?> search;

    public ElementNotFoundMismatch(SequentialGetStepSupplier<?, ?, ?, ?, ?> search) {
        this.search = search;
    }
}
