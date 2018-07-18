package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.io.File;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

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
