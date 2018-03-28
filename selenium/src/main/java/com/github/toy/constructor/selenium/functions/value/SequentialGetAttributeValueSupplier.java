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

public final class SequentialGetAttributeValueSupplier extends
        SequentialGetSupplier<SeleniumSteps, String, WebElement, SequentialGetAttributeValueSupplier> {
    private final String attr;

    private SequentialGetAttributeValueSupplier(String attr) {
        this.attr = attr;
    }

    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the taking of value of the attribute.
     *
     * @param attr is the name if the target attribute
     * @return an instance of {@link SequentialGetAttributeValueSupplier}
     */
    public static SequentialGetAttributeValueSupplier attribute(String attr) {
        return new SequentialGetAttributeValueSupplier(attr);
    }

    /**
     * Adds an element to get value of the attribute
     * @param e is the element to get value of the attribute
     * @return self-reference
     */
    public SequentialGetAttributeValueSupplier of(WebElement e) {
        return super.from(e);
    }

    /**
     * Adds an element to get value of the attribute
     * @param widget is the widget to get value of the attribute
     * @return self-reference
     */
    public SequentialGetAttributeValueSupplier of(Widget widget) {
        return super.from(widget.getWrappedElement());
    }


    /**
     * Adds an element to get value of the attribute
     * @param searchSupplier is how to find the element to get value of the attribute.
     *                       Expected result if the wrapped function is some instance of {@link WebElement}
     *                       or {@link WrapsElement}.
     * @return self-reference
     */
    public SequentialGetAttributeValueSupplier of(SequentialSearchSupplier<?> searchSupplier) {
        return super.from(toGet(searchSupplier.toString(), seleniumSteps -> {
            SearchContext result = searchSupplier.get().apply(seleniumSteps);
            Class<?> resultClass = result.getClass();

            if (WebElement.class.isAssignableFrom(resultClass)) {
                return WebElement.class.cast(result);
            }

            if (WrapsElement.class.isAssignableFrom(resultClass)) {
                return WrapsElement.class.cast(result).getWrappedElement();
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of the attribute %s from %s",
                    attr, result));
        }));
    }

    @Override
    public Function<SeleniumSteps, String> get() {
        return ofNullable(super.get())
                .orElseThrow(() -> new IllegalArgumentException("It is necessary to define element to get attribute"));
    }

    @Override
    protected Function<WebElement, String> getEndFunction() {
        return toGet(format("Value of the attribute '%s'", attr), webElement -> webElement.getAttribute(attr));
    }
}
