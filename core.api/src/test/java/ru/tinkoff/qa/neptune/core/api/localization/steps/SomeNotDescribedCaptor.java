package ru.tinkoff.qa.neptune.core.api.localization.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.StringCaptor;

public class SomeNotDescribedCaptor extends StringCaptor<Object> {
    @Override
    public Object getCaptured(Object toBeCaptured) {
        return null;
    }

    @Override
    public StringBuilder getData(Object caught) {
        return null;
    }
}
