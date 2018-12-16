package ru.tinkoff.qa.neptune.core.api;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.FileCaptor;

import java.io.File;

public class TestFileCaptor extends FileCaptor<Object> {

    public TestFileCaptor() {
        super();
    }

    @Override
    protected File getData(Object caught) {
        return null;
    }

    @Override
    public Object getCaptured(Object toBeCaptured) {
        return toBeCaptured;
    }
}
