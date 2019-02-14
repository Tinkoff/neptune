package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.util.List;

public abstract class StringCaptor<T> extends Captor<T, StringBuilder> {

    public StringCaptor(String message, List<CapturedStringInjector> injectors) {
        super(message, injectors);
    }

    public StringCaptor(String message) {
        this(message, SPIUtil.loadSPI(CapturedStringInjector.class));
    }

    @Override
    public abstract StringBuilder getData(T caught);
}
