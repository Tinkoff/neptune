package com.github.toy.constructor.selenium.captors;

import com.github.toy.constructor.core.api.captors.ImageCaptor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static java.lang.String.format;

public class SeleniumImageCaptor extends ImageCaptor<TakesScreenshot> {
    @Override
    protected void capture(TakesScreenshot caught, String message) {
        try {
            BufferedImage image = getData(caught);
            injectors.forEach(injector -> injector.inject(image, format("Taken browser picture of '%s'", message)));
        }
        catch (RuntimeException ignored) {
        }
    }

    @Override
    protected BufferedImage getData(TakesScreenshot caught) {
        InputStream in = new ByteArrayInputStream(caught.getScreenshotAs(OutputType.BYTES));
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}