package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.io.File;
import java.util.List;

public abstract class FileCaptor<T> extends Captor<T, File> {

    protected FileCaptor(String message, List<CapturedFileInjector> injectors) {
        super(message, injectors);
    }

    protected FileCaptor(String message) {
        this(message, SPIUtil.loadSPI(CapturedFileInjector.class));
    }

    public FileCaptor(List<CapturedFileInjector> injectors) {
        super(injectors);
    }

    public FileCaptor() {
        this(SPIUtil.loadSPI(CapturedFileInjector.class));
    }

    @Override
    public abstract File getData(T caught);
}
