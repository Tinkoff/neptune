package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import org.openqa.selenium.TakesScreenshot;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.ElementList;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
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
     * @param defaultElementScreenshotTaker to set up.
     */
    public static void setDefaultElementScreenshotTaker(ElementScreenshotTaker defaultElementScreenshotTaker) {
        SeleniumImageCaptor.defaultElementScreenshotTaker = defaultElementScreenshotTaker;
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
    protected BufferedImage getData(TakesScreenshot caught) {
        try {
            Class<? extends TakesScreenshot> clazz = caught.getClass();
            InputStream in;

            if (!WebElement.class.isAssignableFrom(clazz) && !Widget.class.isAssignableFrom(clazz)
                    && ElementList.class.isAssignableFrom(clazz)) {
                in = new ByteArrayInputStream(caught.getScreenshotAs(BYTES));
            }
            else {
                in = ofNullable(getDefaultElementScreenshotTaker())
                        .map(elementScreenshotTaker1 -> {
                            if (WebElement.class.isAssignableFrom(clazz)) {
                                return new ByteArrayInputStream(elementScreenshotTaker1
                                        .getScreenshotAs((WebElement) caught, BYTES));
                            }
                            if (Widget.class.isAssignableFrom(clazz)) {
                                return new ByteArrayInputStream(elementScreenshotTaker1
                                        .getScreenshotAs((Widget) caught, BYTES));
                            }
                            if (ElementList.class.isAssignableFrom(clazz)) {
                                return new ByteArrayInputStream(elementScreenshotTaker1
                                        .getScreenshotAs((ElementList) caught, BYTES));
                            }
                            return new ByteArrayInputStream(caught.getScreenshotAs(BYTES));
                        }).orElseGet(() -> new ByteArrayInputStream(caught.getScreenshotAs(BYTES)));
            }
            return ImageIO.read(in);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Class<TakesScreenshot> getTypeToBeCaptured() {
        return TakesScreenshot.class;
    }
}