package ru.tinkoff.qa.neptune.selenium.test.captors;

import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.captors.ListOfWebElementImageCaptor;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

import static org.openqa.selenium.OutputType.BYTES;

public class TestListOfWebElementImageCaptor extends ListOfWebElementImageCaptor {

    public TestListOfWebElementImageCaptor() {
        super();
    }

    @Override
    public BufferedImage getData(List<WebElement> caught) {
        byte[] bytes = new byte[]{};

        for (WebElement e : caught) {
            bytes = ArrayUtils.addAll(bytes, e.getScreenshotAs(BYTES));
        }

        var in = new ByteArrayInputStream(bytes);
        try {
            return ImageIO.read(in);
        } catch (IOException e) {
            return null;
        }
    }
}
