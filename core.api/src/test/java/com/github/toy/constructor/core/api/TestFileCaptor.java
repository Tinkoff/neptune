package com.github.toy.constructor.core.api;

import com.github.toy.constructor.core.api.captors.FileCaptor;

import java.io.File;

public class TestFileCaptor extends FileCaptor<Object> {

    public TestFileCaptor() {
        super();
    }

    @Override
    protected File getData(Object caught) {
        return null;
    }
}
