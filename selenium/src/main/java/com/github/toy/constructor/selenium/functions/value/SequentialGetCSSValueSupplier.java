package com.github.toy.constructor.selenium.functions.value;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.functions.searching.SequentialSearchSupplier;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.internal.WrapsElement;

import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class SequentialGetCSSValueSupplier extends
        SequentialGetSupplier<SeleniumSteps, String, WebElement, SequentialGetCSSValueSupplier> {
    private final String property;

    private SequentialGetCSSValueSupplier(String property) {
        this.property = property;
    }

    /**
     * Creates an instance of {@link SequentialGetCSSValueSupplier} for the taking of value of the css property.
     *
     * @param property is the name of the target css property
     * @return an instance of {@link SequentialGetCSSValueSupplier}
     */
    public static SequentialGetCSSValueSupplier cssValue(String property) {
        return new SequentialGetCSSValueSupplier(property);
    }

    /**
     * Adds an element to get value of the css property
     * @param e is the element to get value of the css property
     * @return self-reference
     */
    public SequentialGetCSSValueSupplier of(WebElement e) {
        return super.from(e);
    }

    /**
     * Adds an element to get value of the css property
     * @param widget is the widget to get value of the css property
     * @return self-reference
     */
    public SequentialGetCSSValueSupplier of(Widget widget) {
        return super.from(widget.getWrappedElement());
    }


    /**
     * Adds an element to get value of the css property
     * @param searchSupplier is how to find the element to get value of the css property.
     *                       Expected result if the wrapped function is some instance of {@link WebElement}
     *                       or {@link WrapsElement}.
     * @return self-reference
     */
    public <T extends SearchContext> SequentialGetCSSValueSupplier of(SequentialSearchSupplier<T> searchSupplier) {
        return super.from(toGet(searchSupplier.toString(), seleniumSteps -> {
            T result = searchSupplier.get().apply(seleniumSteps);
            Class<?> resultClass = result.getClass();

            if (WebElement.class.isAssignableFrom(resultClass)) {
                return WebElement.class.cast(result);
            }

            if (WrapsElement.class.isAssignableFrom(resultClass)) {
                return WrapsElement.class.cast(result).getWrappedElement();
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of the css property %s from %s",
                    property, result));
        }));
    }

    @Override
    public Function<SeleniumSteps, String> get() {
        return ofNullable(super.get())
                .orElseThrow(() -> new IllegalArgumentException("It is necessary to define element to get css property"));
    }

    @Override
    protected Function<WebElement, String> getEndFunction() {
        return toGet(format("Value of the css property '%s'", property), webElement -> webElement.getCssValue(property));
    }
}
