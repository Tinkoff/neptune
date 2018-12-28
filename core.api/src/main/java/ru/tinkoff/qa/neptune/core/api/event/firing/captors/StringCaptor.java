package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public abstract class StringCaptor<T> extends Captor<T, StringBuilder> {

    public StringCaptor(String message, List<CapturedStringInjector> injectors) {
        super(message, injectors);
    }

    public StringCaptor(String message) {
        this(message, loadSPI(CapturedStringInjector.class));
    }

    @Override
    public abstract StringBuilder getData(T caught);
}
