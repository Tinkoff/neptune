package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.core.api.steps.Description;

import java.awt.image.BufferedImage;

@Description("Screenshot taken after action on web element/widget")
public class ImageCaptorAfterActionOnElement extends ImageCaptor<? extends WrapsDriver, TakesScreenshot> {

    @Override
    public WrapsDriver getCaptured(Object toBeCaptured) {
        return null;
    }

    @Override
    public BufferedImage getData(WrapsDriver caught) {
        return null;
    }
}
