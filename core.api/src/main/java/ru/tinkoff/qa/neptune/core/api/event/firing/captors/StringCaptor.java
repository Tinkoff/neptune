package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;

import java.util.List;

@UseInjectors(CapturedStringInjector.class)
public abstract class StringCaptor<T> extends Captor<T, StringBuilder> {

    protected StringCaptor(String message, List<CapturedStringInjector> injectors) {
        super(message, injectors);
    }

    protected StringCaptor(String message) {
        super(message);
    }

    public StringCaptor(List<CapturedStringInjector> injectors) {
        super(injectors);
    }

    public StringCaptor() {
        super();
    }

    @Override
    public abstract StringBuilder getData(T caught);
}
