package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public abstract class StringCaptor<T> extends Captor<T, StringBuilder> {

    public StringCaptor(List<CapturedStringInjector> injectors) {
        super(injectors);
    }

    public StringCaptor() {
        this(loadSPI(CapturedStringInjector.class));
    }

    @Override
    protected abstract StringBuilder getData(T caught);
}
