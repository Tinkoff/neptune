package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.utils.SPIUtil;

import java.awt.image.BufferedImage;
import java.util.List;

public abstract class ImageCaptor<T> extends Captor<T, BufferedImage> {

    public ImageCaptor(List<CapturedImageInjector> injectors) {
        super(injectors);
    }

    public ImageCaptor() {
        this(SPIUtil.loadSPI(CapturedImageInjector.class));
    }

    @Override
    public abstract BufferedImage getData(T caught);
}
