package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;

import java.io.File;
import java.util.List;

@UseInjectors(CapturedFileInjector.class)
public abstract class FileCaptor<T> extends Captor<T, File> {

    protected FileCaptor(String message, List<CapturedFileInjector> injectors) {
        super(message, injectors);
    }

    protected FileCaptor(String message) {
        super(message);
    }

    public FileCaptor(List<CapturedFileInjector> injectors) {
        super(injectors);
    }

    public FileCaptor() {
        super();
    }

    @Override
    public abstract File getData(T caught);
}
