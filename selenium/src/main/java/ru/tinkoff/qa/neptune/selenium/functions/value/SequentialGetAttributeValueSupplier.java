package ru.tinkoff.qa.neptune.selenium.functions.value;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasAttribute;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static java.lang.String.format;

public final class SequentialGetAttributeValueSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, String, SearchContext, SequentialGetAttributeValueSupplier> {

    private boolean isElementDefined;

    private SequentialGetAttributeValueSupplier(String attr) {
        super("Value of the attribute '%s'", searchContext -> {
            var searchContextClass = searchContext.getClass();
            if (WebElement.class.isAssignableFrom(searchContextClass)) {
                return ((WebElement) searchContext).getAttribute(attr);
            }

            if (HasAttribute.class.isAssignableFrom(searchContextClass)) {
                return ((HasAttribute) searchContext).getAttribute(attr);
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of the attribute %s from the " +
                            "%s instance. Instance of " +
                            "%s or %s or %s is expected.", attr, searchContextClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasAttribute.class.getName()));
        });
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
        isElementDefined = true;
        return super.from(e);
    }

    /**
     * Adds an element to get value of the attribute
     * @param widget is the widget to get value of the attribute
     * @return self-reference
     */
    public SequentialGetAttributeValueSupplier of(Widget widget) {
        isElementDefined = true;
        return super.from(widget);
    }

    /**
     * Adds an element to get value of the attribute
     * @param t instance of some type which provides ability to get value of the attribute.
     * @param <T> subtype of {@link SearchContext} which provides ability to get value of the attribute.
     * @return self-reference
     */
    public <T extends SearchContext & HasAttribute> SequentialGetAttributeValueSupplier of(T t) {
        isElementDefined = true;
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
        isElementDefined = true;
        return super.from(searchSupplier.get().compose(currentContent()));
    }

    @Override
    public Function<SeleniumStepContext, String> get() {
        checkArgument(isElementDefined, "It is necessary to define element to get attribute");
        return super.get();
    }
}
