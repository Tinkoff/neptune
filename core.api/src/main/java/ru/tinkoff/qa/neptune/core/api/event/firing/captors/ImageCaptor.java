package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.awt.image.BufferedImage;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public abstract class ImageCaptor<T> extends Captor<T, BufferedImage> {

    public ImageCaptor(List<CapturedImageInjector> injectors) {
        super(injectors);
    }

    public ImageCaptor() {
        this(loadSPI(CapturedImageInjector.class));
    }

    @Override
    protected abstract BufferedImage getData(T caught);
}
