package ru.tinkoff.qa.neptune.check;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Does not match any of the listed criteria")
final class DoesNotMatchAnyCriteria extends MismatchDescriber {
}
