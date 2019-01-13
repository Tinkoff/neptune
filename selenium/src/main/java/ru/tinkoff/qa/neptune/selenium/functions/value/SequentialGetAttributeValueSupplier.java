package ru.tinkoff.qa.neptune.selenium.functions.value;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasAttribute;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class SequentialGetAttributeValueSupplier extends
        SequentialGetStepSupplier<SeleniumStepContext, String, SearchContext, SequentialGetAttributeValueSupplier> {
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
    public static SequentialGetAttributeValueSupplier attributeValue(String attr) {
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
        return super.from(widget);
    }

    /**
     * Adds an element to get value of the attribute
     * @param t instance of some type which provides ability to get value of the attribute.
     * @param <T> subtype of {@link SearchContext} which provides ability to get value of the attribute.
     * @return self-reference
     */
    public <T extends SearchContext & HasAttribute> SequentialGetAttributeValueSupplier of(T t) {
        return super.from(t);
    }


    /**
     * Adds an element to get value of the attribute
     * @param searchSupplier is how to find the element to get value of the attribute.
     *                       It is expected that result of the wrapped function is some instance of {@link WebElement}
     *                       or some class which implements {@link SearchContext} and {@link HasAttribute}. Otherwise
     *                       evaluation of the attribute value throws {@link UnsupportedOperationException}.
     * @param <T> subtype of {@link SearchContext} which provides ability to get value of the attribute.
     * @return self-reference
     */
    public <T extends SearchContext> SequentialGetAttributeValueSupplier of(SearchSupplier<T> searchSupplier) {
        return super.from(searchSupplier.get().compose(currentContent()));
    }

    @Override
    public Function<SeleniumStepContext, String> get() {
        return ofNullable(super.get())
                .orElseThrow(() -> new IllegalArgumentException("It is necessary to define element to get attribute"));
    }

    @Override
    protected Function<SearchContext, String> getEndFunction() {
        return toGet(format("Value of the attribute '%s'", attr), searchContext -> {
            var searchContextClass = searchContext.getClass();
            if (WebElement.class.isAssignableFrom(searchContextClass)) {
                return ((WebElement) searchContext).getAttribute(attr);
            }

            if (HasAttribute.class.isAssignableFrom(searchContextClass)) {
                return ((HasAttribute) searchContext).getAttribute(attr);
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of the attribute %s from " +
                            "the instance of %s. Instance of " +
                            "%s or subclass of %s and %s is expected.", attr, searchContextClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasAttribute.class.getName()));
        });
    }
}
