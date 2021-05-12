package ru.tinkoff.qa.neptune.selenium.test.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedImageInjector;

import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

public class TestImageInjector implements CapturedImageInjector {

    static final Map<BufferedImage, String> INJECTED = new HashMap<>();

    public TestImageInjector() {
        super();
    }

    @Override
    public void inject(BufferedImage toBeInjected, String message) {
        INJECTED.put(toBeInjected, message);
    }
}
