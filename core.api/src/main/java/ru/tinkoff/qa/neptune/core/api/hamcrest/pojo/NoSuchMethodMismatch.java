package ru.tinkoff.qa.neptune.core.api.hamcrest.pojo;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("Class {clazz} has no non-static and public method '{getter}' with empty signature and which returns some value")
final class NoSuchMethodMismatch extends MismatchDescriber {

    @DescriptionFragment("clazz")
    final Class<?> clazz;

    @DescriptionFragment("getter")
    final String getter;

    NoSuchMethodMismatch(Class<?> clazz, String getter) {
        this.clazz = clazz;
        this.getter = getter;
    }
}
