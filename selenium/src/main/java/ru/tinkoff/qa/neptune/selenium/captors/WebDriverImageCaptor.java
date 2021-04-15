package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static java.util.Optional.ofNullable;
import static org.openqa.selenium.OutputType.BYTES;

/**
 * Takes screenshot from a whole page
 */
@Description("Browser screenshot")
public class WebDriverImageCaptor extends ImageCaptor<WebDriver> {

    @Override
    public BufferedImage getData(WebDriver caught) {
        var in = new ByteArrayInputStream(((TakesScreenshot) caught).getScreenshotAs(BYTES));
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }

    @Override
    public WebDriver getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (!WrapsDriver.class.isAssignableFrom(clazz) && !WebDriver.class.isAssignableFrom(clazz)) {
            return null;
        }

        if (WebElement.class.isAssignableFrom(clazz) || Widget.class.isAssignableFrom(clazz)) {
            return null;
        }

        if (WebDriver.class.isAssignableFrom(clazz)) {
            return (WebDriver) toBeCaptured;
        }

        return ofNullable(((WrapsDriver) toBeCaptured).getWrappedDriver())
                .map(webDriver -> {
                    if (TakesScreenshot.class.isAssignableFrom(webDriver.getClass())) {
                        return webDriver;
                    }
                    return null;
                })
                .orElse(null);
    }
}
