package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;

public class TestFileCaptor extends FileCaptor<Object> {

    public TestFileCaptor() {
        super("Saved to file");
    }

    @Override
    public File getData(Object caught) {
        return null;
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }
}
