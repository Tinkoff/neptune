package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import org.reflections.Reflections;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.lang.reflect.Constructor;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

/**
 * This class is used for the supporting of actions to be performed on some web element when it is found with some {@link SearchSupplier}.
 * It is about the scrolling an element into view area.
 */
public interface ScrollsIntoView {

    /**
     * This method should scroll an element into view. When some subclass of the {@link Widget} or {@link WebElement}
     * implements {@link ScrollsIntoView} and an instance is received with some {@link SearchSupplier} then
     * the method is invoked every time before any action is performed on the instance. Otherwise an instance of
     * {@link ScrollWebElementIntoView} is used for this purpose when some element is received with some
     * {@link SearchSupplier}.
     * @see ScrollWebElementIntoView
     * @see ScrollWebElementIntoView#getDefaultScrollerIntoView
     */
    void scrollsIntoView();

    abstract class ScrollWebElementIntoView implements ScrollsIntoView, WrapsElement {
        private static final Reflections reflections = new Reflections("");
        private final WebElement elementToBeScrolledIntoView;

        public ScrollWebElementIntoView(WebElement elementToBeScrolledIntoView) {
            this.elementToBeScrolledIntoView = elementToBeScrolledIntoView;
        }

        static ScrollsIntoView getDefaultScrollerIntoView(WebElement e) throws Throwable {
            if (isNull(e)) {
                return null;
            }

            Constructor<?> constructor = reflections
                    .getSubTypesOf(ScrollWebElementIntoView.class)
                    .stream()
                    .map(aClass -> {
                        var constructors = aClass.getDeclaredConstructors();
                        return stream(constructors).filter(c -> {
                            var params = c.getParameterTypes();
                            if (params.length != 1) {
                                return false;
                            }

                            return params[0].isAssignableFrom(e.getClass());
                        }).findFirst().orElse(null);
                    })
                    .filter(Objects::nonNull)
                    .findFirst()
                    .orElse(null);

            if (constructor != null) {
                constructor.setAccessible(true);
                return (ScrollsIntoView) constructor.newInstance(e);
            }

            return null;
        }

        public WebElement getWrappedElement() {
            return elementToBeScrolledIntoView;
        }
    }
}
