package ru.tinkoff.qa.neptune.selenium.functions.searching;

import com.google.common.annotations.Beta;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ScanResult;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.WrapsElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Objects;

import static java.util.Arrays.stream;
import static java.util.Objects.isNull;

/**
 * This class is used for the supporting of actions to be performed on some web element
 * when it is found with some {@link SearchSupplier}.
 */
@Beta
public abstract class ScrollWebElementIntoView implements ScrollsIntoView, WrapsElement {

    private static final ScanResult SCAN_RESULT = new ClassGraph()
            .enableAllInfo()
            .scan();
    private final WebElement elementToBeScrolledIntoView;

    public ScrollWebElementIntoView(WebElement elementToBeScrolledIntoView) {
        this.elementToBeScrolledIntoView = elementToBeScrolledIntoView;
    }

    /**
     * Defines the necessity to perform the scrolling of the element into view.
     *
     * @param method is a method to perform the scrolling before
     * @return {@code true} when it is necessary to perform the scrolling of an element. {@code false} is returned
     * otherwise.
     */
    protected abstract boolean needsForTheScrolling(Method method);

    /**
     * This method should scroll the web element into view.
     * <p>NOTE!</p>
     * When a {@link WebElement} is received with some {@link SearchSupplier} then this method is invoked implicitly
     * every time before any method passed via {@link #needsForTheScrolling(Method)} with the result {@code true}.
     */
    public abstract void scrollIntoView();


    public WebElement getWrappedElement() {
        return elementToBeScrolledIntoView;
    }

    static ScrollWebElementIntoView getDefaultScrollerIntoView(WebElement e) throws Throwable {
        if (isNull(e)) {
            return null;
        }

        Constructor<?> constructor = SCAN_RESULT.getSubclasses(ScrollWebElementIntoView.class.getName())
                .loadClasses(ScrollWebElementIntoView.class)
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
            return (ScrollWebElementIntoView) constructor.newInstance(e);
        }

        return null;
    }
}
