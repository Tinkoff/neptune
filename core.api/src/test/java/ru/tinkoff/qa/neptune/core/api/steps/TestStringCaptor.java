package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

@Description("Saved to string")
public class TestStringCaptor extends StringCaptor<Object> {

    @Override
    public StringBuilder getData(Object caught) {
        return new StringBuilder(caught.toString());
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }

}
