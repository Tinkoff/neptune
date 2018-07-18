package ru.tinkoff.qa.neptune.selenium.captors;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.InputStream;

import static java.lang.String.format;

public class SeleniumImageCaptor extends ImageCaptor<TakesScreenshot> {

    @Override
    public void capture(TakesScreenshot caught, String message) {
        super.capture(caught, format("Taken browser picture of '%s'", message));
    }

    @Override
    protected BufferedImage getData(TakesScreenshot caught) {
        try {
            InputStream in = new ByteArrayInputStream(caught.getScreenshotAs(OutputType.BYTES));
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