package ru.tinkoff.qa.neptune.selenium.captors;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.captors.ImageCaptor;

import java.util.List;
import java.util.Objects;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

public abstract class ListOfWebElementImageCaptor extends ImageCaptor<List<WebElement>> {

    public ListOfWebElementImageCaptor() {
        super("Screenshot taken from the list of elements");
    }

    @Override
    public List<WebElement> getCaptured(Object toBeCaptured) {
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
            var objectClass = o.getClass();

            if (WebElement.class.isAssignableFrom(objectClass)) {
                return (WebElement) o;
            }

            if (WrapsElement.class.isAssignableFrom(objectClass)) {
                return ((WrapsElement) o).getWrappedElement();
            }

            return null;
        }).filter(Objects::nonNull).collect(toList());
    }
}
