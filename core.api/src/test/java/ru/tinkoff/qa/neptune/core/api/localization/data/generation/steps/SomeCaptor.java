package ru.tinkoff.qa.neptune.core.api.localization.data.generation.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Some captor")
public class SomeCaptor extends StringCaptor<Object> {
    @Override
    public Object getCaptured(Object toBeCaptured) {
        return null;
    }

    @Override
    public StringBuilder getData(Object caught) {
        return null;
    }
}
