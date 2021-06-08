package ru.tinkoff.qa.neptune.core.api.hamcrest.pojo;

import ru.tinkoff.qa.neptune.core.api.hamcrest.MatchObjectName;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

@Description("value returned from '{getter}'")
final class ReturnedObject extends MatchObjectName {

    @DescriptionFragment("getter")
    final String getter;

    ReturnedObject(String getter) {
        this.getter = getter;
    }
}
