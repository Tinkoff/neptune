package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.awt.image.BufferedImage;
import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.captors.ElementPhotographer.merge;

/**
 * Takes screenshot from a list of web elements/widgets
 */
@Beta
@Description("Screenshot taken from the list of elements")
public final class ListOfWebElementImageCaptor extends ImageCaptor<List<ElementPhotographer>> {

    @Override
    public List<ElementPhotographer> getCaptured(Object toBeCaptured) {
        var clazz = toBeCaptured.getClass();

        if (!List.class.isAssignableFrom(clazz)) {
            return null;
        }

        return ((List<?>) toBeCaptured).stream().filter(o -> {
            if (isNull(o)) {
                return false;
            }

            var objectClass = o.getClass();
            return (WebElement.class.isAssignableFrom(objectClass) || (WrapsElement.class.isAssignableFrom(objectClass)));
        }).map(o -> {
            WebElement e;
            if (o instanceof WebElement) {
                e = (WebElement) o;
            } else {
                e = ofNullable(((WrapsElement) o)
                        .getWrappedElement())
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
        }).filter(Objects::nonNull).collect(toList());
    }

    @Override
    public BufferedImage getData(List<ElementPhotographer> toBeCaptured) {
        if (toBeCaptured.size() == 0) {
            return null;
        }

        var driver = toBeCaptured.get(0).getWrappedDriver();
        return merge(toBeCaptured, driver);
    }
}
