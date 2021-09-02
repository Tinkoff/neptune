package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;
import org.openqa.selenium.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.awt.image.BufferedImage;

import static java.util.Optional.ofNullable;

/**
 * Takes screenshot from a web element/widget
 */
@Description("Screenshot taken from the element")
public class WebElementImageCaptor extends ImageCaptor<ElementPhotographer> {

    @Override
    public ElementPhotographer getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (!WebElement.class.isAssignableFrom(clazz) && !WrapsElement.class.isAssignableFrom(clazz)) {
            return null;
        }

        WebElement e;
        if (WebElement.class.isAssignableFrom(clazz)) {
            e = (WebElement) toBeCaptured;
        } else {
            e = ofNullable(((WrapsElement) toBeCaptured).getWrappedElement())
                    .orElse(null);
        }

        if (e == null) {
            return null;
        }

        WebDriver driver = null;
        if (e instanceof WrapsDriver) {
            driver = ((WrapsDriver) e).getWrappedDriver();
        }

        if (driver == null) {
            return null;
        }

        return new ElementPhotographer(driver, e);
    }

    @Override
    public BufferedImage getData(ElementPhotographer caught) {
        return caught.getScreenshot();
    }
}
