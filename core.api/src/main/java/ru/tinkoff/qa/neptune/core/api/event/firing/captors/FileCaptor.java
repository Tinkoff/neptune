package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.io.File;
import java.util.List;

public abstract class FileCaptor<T> extends Captor<T, File> {

    public FileCaptor(List<CapturedFileInjector> injectors) {
        super(injectors);
    }

    public FileCaptor(String message) {
        this(SPIUtil.loadSPI(CapturedFileInjector.class));
    }

    @Override
    public abstract File getData(T caught);
}
