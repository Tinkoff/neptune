package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.awt.image.BufferedImage;

/**
 * Image elements
 */
@Name("Image element")
public abstract class Image extends Widget implements HasValue<BufferedImage> {

    public Image(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
