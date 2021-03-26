package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.util.List;

public abstract class StringCaptor<T> extends Captor<T, StringBuilder> {

    public StringCaptor(List<CapturedStringInjector> injectors) {
        super(injectors);
    }

    public StringCaptor() {
        this(SPIUtil.loadSPI(CapturedStringInjector.class));
    }

    @Override
    public abstract StringBuilder getData(T caught);
}
