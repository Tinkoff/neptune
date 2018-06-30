package com.github.toy.constructor.core.api.event.firing.captors;

import com.github.toy.constructor.core.api.event.firing.Captor;

import java.util.List;

import static com.github.toy.constructor.core.api.utils.SPIUtil.loadSPI;

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
