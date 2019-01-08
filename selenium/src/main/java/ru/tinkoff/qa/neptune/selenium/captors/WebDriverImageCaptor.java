package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static org.openqa.selenium.OutputType.BYTES;

public class WebDriverImageCaptor extends ImageCaptor<WrapsDriver> {

    public WebDriverImageCaptor() {
        super("Browser screenshot");
    }

    @Override
    public BufferedImage getData(WrapsDriver caught) {
        var in = new ByteArrayInputStream(((TakesScreenshot) caught.getWrappedDriver()).getScreenshotAs(BYTES));
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public WrapsDriver getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (!WrapsDriver.class.isAssignableFrom(clazz)) {
            return null;
        }

        if (WebElement.class.isAssignableFrom(clazz) || Widget.class.isAssignableFrom(clazz)) {
            return null;
        }

        return ofNullable(((WrapsDriver) toBeCaptured).getWrappedDriver())
                .map(webDriver -> {
                    if (TakesScreenshot.class.isAssignableFrom(webDriver.getClass())) {
                        return (WrapsDriver) toBeCaptured;
                    }
                    return null;
                })
                .orElse(null);
    }
}
