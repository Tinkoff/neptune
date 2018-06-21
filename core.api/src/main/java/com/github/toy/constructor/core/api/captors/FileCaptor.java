package com.github.toy.constructor.core.api.captors;

import com.github.toy.constructor.core.api.Captor;

import java.io.File;
import java.util.List;

import static com.github.toy.constructor.core.api.SPIUtil.loadSPI;

public abstract class FileCaptor<T> extends Captor<T, File> {

    public FileCaptor(List<CapturedFileInjector> injectors) {
        super(injectors);
    }

    public FileCaptor() {
        this(loadSPI(CapturedFileInjector.class));
    }

    @Override
    protected abstract File getData(T caught);
}
