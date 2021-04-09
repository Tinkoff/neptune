package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.Description;

import static java.util.Optional.ofNullable;

/**
 * Takes screenshot from a web element/widget
 */
@Description("Screenshot taken from the element")
public abstract class WebElementImageCaptor extends ImageCaptor<WebElement> {

    @Override
    public WebElement getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();
        if (!WebElement.class.isAssignableFrom(clazz) && !WrapsElement.class.isAssignableFrom(clazz)) {
            return null;
        }

        if (WebElement.class.isAssignableFrom(clazz)) {
            return (WebElement) toBeCaptured;
        }

        return ofNullable(((WrapsElement) toBeCaptured).getWrappedElement())
                .orElse(null);
    }
}
