package ru.tinkoff.qa.neptune.selenium.functions.value;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasCssValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class SequentialGetCSSValueSupplier extends
        SequentialGetStepSupplier<SeleniumSteps, String, SearchContext, SequentialGetCSSValueSupplier> {
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
        return super.from(widget);
    }

    /**
     * Adds an element to get value of the css property
     * @param t instance of some type which provides ability to get value of the css property.
     * @param <T> subtype of {@link SearchContext} which provides ability to get value of the css property.
     * @return self-reference
     */
    public <T extends SearchContext & HasCssValue> SequentialGetCSSValueSupplier of(T t) {
        return super.from(t);
    }

    /**
     * Adds an element to get value of the css property
     * @param searchSupplier is how to find the element to get value of the css property.
     *                       It is expected that result of the wrapped function is some instance of {@link WebElement}
     *                       or some class which implements {@link SearchContext} and {@link HasCssValue}. Otherwise
     *                       evaluation of the css value throws {@link UnsupportedOperationException}.
     * @return self-reference
     */
    public <T extends SearchContext> SequentialGetCSSValueSupplier of(SearchSupplier<T> searchSupplier) {
        return super.from(searchSupplier.get().compose(currentContent()));
    }

    @Override
    public Function<SeleniumSteps, String> get() {
        return ofNullable(super.get())
                .orElseThrow(() -> new IllegalArgumentException("It is necessary to define element to get css property"));
    }

    @Override
    protected Function<SearchContext, String> getEndFunction() {
        return toGet(format("Value of the css property '%s'", property), searchContext -> {
            Class<? extends SearchContext> searchContextClass = searchContext.getClass();
            if (WebElement.class.isAssignableFrom(searchContextClass)) {
                return WebElement.class.cast(searchContext).getCssValue(property);
            }

            if (HasCssValue.class.isAssignableFrom(searchContextClass)) {
                return HasCssValue.class.cast(searchContext).getCssValue(property);
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of thr css property %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", property, searchContextClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasCssValue.class.getName()));
        });
    }
}
