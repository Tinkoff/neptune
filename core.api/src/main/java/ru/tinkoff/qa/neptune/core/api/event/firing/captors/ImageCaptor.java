package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;

import java.awt.image.BufferedImage;
import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.utils.SPIUtil.loadSPI;

public abstract class ImageCaptor<T> extends Captor<T, BufferedImage> {

    public ImageCaptor(String message, List<CapturedImageInjector> injectors) {
        super(message, injectors);
    }

    public ImageCaptor(String message) {
        this(message, loadSPI(CapturedImageInjector.class));
    }

    @Override
    public abstract BufferedImage getData(T caught);
}
