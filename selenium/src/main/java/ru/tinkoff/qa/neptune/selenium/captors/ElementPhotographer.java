package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsDriver;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.openqa.selenium.OutputType.BYTES;

public class ElementPhotographer implements WrapsDriver {

    private final WebDriver driver;
    private final WebElement element;

    ElementPhotographer(WebDriver driver, WebElement element) {
        this.driver = driver;
        this.element = element;
    }

    static BufferedImage merge(Collection<ElementPhotographer> elementPhotographers,
                               WebDriver driver) {

        List<BufferedImage> images = new ArrayList<>();
        for (var e : elementPhotographers) {
            try {
                images.add(e.getScreenshotFromElement());
            } catch (Exception exception) {
                try {
                    return readBytes(((TakesScreenshot) driver).getScreenshotAs(BYTES));
                } catch (Exception exception2) {
                    return null;
                }
            }
        }

        int heightTotal = 0;
        int maxWidth = 0;
        for (var image : images) {
            heightTotal += image.getHeight() + 10;
            if (image.getWidth() > maxWidth) {
                maxWidth = image.getWidth();
            }
        }

        BufferedImage output = new BufferedImage(
                maxWidth + 20,
                heightTotal,
                BufferedImage.TYPE_INT_ARGB);

        int heightCurr = 10;
        Graphics2D g2d = output.createGraphics();
        for (var image : images) {
            g2d.drawImage(image, 10, heightCurr, null);
            heightCurr += image.getHeight() + 10;
        }
        g2d.dispose();

        return output;
    }

    private static BufferedImage readBytes(byte[] bytes) throws IOException {
        var in = new ByteArrayInputStream(bytes);
        return ImageIO.read(in);
    }

    private BufferedImage getScreenshotFromElement() throws Exception {
        return readBytes(element.getScreenshotAs(BYTES));
    }

    public BufferedImage getScreenshot() {
        try {
            return getScreenshotFromElement();
        } catch (Exception e) {
            e.printStackTrace();
            if (driver instanceof TakesScreenshot) {
                try {
                    return readBytes(((TakesScreenshot) driver).getScreenshotAs(BYTES));
                } catch (Exception e2) {
                    e2.printStackTrace();
                    return null;
                }
            }
            return null;
        }
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }
}
