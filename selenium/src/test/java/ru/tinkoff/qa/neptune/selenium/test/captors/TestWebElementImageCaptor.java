package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;

import static org.openqa.selenium.OutputType.BYTES;

public class TestWebElementImageCaptor extends WebElementImageCaptor {

    @Override
    public BufferedImage getData(WebElement caught) {
        var in = new ByteArrayInputStream(caught.getScreenshotAs(BYTES));
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }
}
