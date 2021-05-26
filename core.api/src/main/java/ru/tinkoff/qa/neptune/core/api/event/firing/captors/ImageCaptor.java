package ru.tinkoff.qa.neptune.core.api.event.firing.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.Captor;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.UseInjectors;

import java.awt.image.BufferedImage;
import java.util.List;

@UseInjectors(CapturedImageInjector.class)
public abstract class ImageCaptor<T> extends Captor<T, BufferedImage> {

    protected ImageCaptor(String message, List<CapturedImageInjector> injectors) {
        super(message, injectors);
    }

    protected ImageCaptor(String message) {
        super(message);
    }

    public ImageCaptor(List<CapturedImageInjector> injectors) {
        super(injectors);
    }

    public ImageCaptor() {
        super();
    }

    @Override
    public abstract BufferedImage getData(T caught);
}
