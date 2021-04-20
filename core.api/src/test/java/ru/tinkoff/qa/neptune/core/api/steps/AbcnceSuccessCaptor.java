package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.collect.ImmutableList.of;

public class AbcnceSuccessCaptor extends Captor<Object, String> {

    public static final List<String> CAUGHT = new ArrayList<>();

    public AbcnceSuccessCaptor() {
        super("Test message", of());
    }

    @Override
    public String getData(Object caught) {
        CAUGHT.add("Absent here: " + caught.toString());
        return caught.toString();
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }
}
