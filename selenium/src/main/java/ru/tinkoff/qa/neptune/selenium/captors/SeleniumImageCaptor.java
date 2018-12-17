package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toCollection;
import static org.openqa.selenium.OutputType.BYTES;

public class SeleniumImageCaptor extends ImageCaptor<TakesScreenshot> {

    private static ElementScreenshotTaker defaultElementScreenshotTaker;
    private final ElementScreenshotTaker elementScreenshotTaker;

    public SeleniumImageCaptor(ElementScreenshotTaker elementScreenshotTaker) {
        super();
        this.elementScreenshotTaker = elementScreenshotTaker;
    }

    public SeleniumImageCaptor() {
        this(null);
    }

    /**
     * Sets a default instance of {@link ElementScreenshotTaker}
     * @param elementScreenshotTaker to set up.
     */
    public static void setDefaultElementScreenshotTaker(ElementScreenshotTaker elementScreenshotTaker) {
        defaultElementScreenshotTaker = elementScreenshotTaker;
    }

    private ElementScreenshotTaker getDefaultElementScreenshotTaker() {
        return ofNullable(elementScreenshotTaker)
                .orElse(defaultElementScreenshotTaker);
    }

    @Override
    public void capture(TakesScreenshot caught, String message) {
        super.capture(caught, format("Taken browser picture of '%s'", message));
    }

    @Override
    @SuppressWarnings("unchecked")
    protected BufferedImage getData(TakesScreenshot caught) {
        try {
            var in = ofNullable(getDefaultElementScreenshotTaker())
                    .map(elementScreenshotTaker1 -> {
                        var clazz = caught.getClass();
                        if (WebElement.class.isAssignableFrom(clazz)) {
                            return new ByteArrayInputStream(elementScreenshotTaker1
                                    .getScreenshotAs((WebElement) caught, BYTES));
                        }
                        WebElement webElement;
                        if (Widget.class.isAssignableFrom(clazz) && (webElement = ((Widget) caught).getWrappedElement()) != null) {
                            return new ByteArrayInputStream(elementScreenshotTaker1.getScreenshotAs(webElement, BYTES));
                        }
                        if (List.class.isAssignableFrom(clazz)) {
                            return new ByteArrayInputStream(elementScreenshotTaker1
                                    .getScreenshotAs((List<WebElement>) caught, BYTES));
                        }
                        return new ByteArrayInputStream(caught.getScreenshotAs(BYTES));
                    }).orElseGet(() -> new ByteArrayInputStream(caught.getScreenshotAs(BYTES)));
            return ImageIO.read(in);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public TakesScreenshot getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (TakesScreenshot.class.isAssignableFrom(clazz)) {
            return (TakesScreenshot) toBeCaptured;
        }

        if (!List.class.isAssignableFrom(clazz)) {
            return null;
        }

        ListToTakeScreenShot result = ((List<?>) toBeCaptured).stream().filter(o -> {
           if (o == null) {
               return false;
           }

           var objectClass = o.getClass();
           return (WebElement.class.isAssignableFrom(objectClass) || (WrapsElement.class.isAssignableFrom(objectClass)));
        }).map(o -> {
            var objectClass = o.getClass();

            if (WebElement.class.isAssignableFrom(objectClass)) {
                return (WebElement) o;
            }

            if (WrapsElement.class.isAssignableFrom(objectClass)) {
                return ((WrapsElement) o).getWrappedElement();
            }

            return null;
        }).filter(Objects::nonNull).collect(toCollection(ListToTakeScreenShot::new));

        if (result.size() > 0) {
            return result;
        }

        return null;
    }

    private static class ListToTakeScreenShot extends ArrayList<WebElement> implements TakesScreenshot {

        @Override
        public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
            return null;
        }
    }
}