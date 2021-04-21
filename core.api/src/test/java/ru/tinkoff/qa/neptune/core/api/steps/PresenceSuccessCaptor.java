package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;

public class PresenceSuccessCaptor extends Captor<Object, String> {

    public static final List<String> CAUGHT = new ArrayList<>();

    public PresenceSuccessCaptor() {
        super("Test message", of());
    }

    @Override
    public String getData(Object caught) {
        CAUGHT.add("Present: " + caught.toString());
        return caught.toString();
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }
}
