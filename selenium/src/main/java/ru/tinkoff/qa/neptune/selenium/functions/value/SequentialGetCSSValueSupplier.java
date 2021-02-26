package ru.tinkoff.qa.neptune.selenium.functions.value;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasCssValue;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@SequentialGetStepSupplier.DefaultParameterNames(
        from = "Element to get value of the css"
)
public final class SequentialGetCSSValueSupplier extends
        SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, String, SearchContext, SequentialGetCSSValueSupplier> {

    private boolean isElementDefined;

    private SequentialGetCSSValueSupplier(String property) {
        super("Value of the css property '" + property + "'", searchContext -> {
            Class<? extends SearchContext> searchContextClass = searchContext.getClass();
            if (WebElement.class.isAssignableFrom(searchContextClass)) {
                return ((WebElement) searchContext).getCssValue(property);
            }

            if (HasCssValue.class.isAssignableFrom(searchContextClass)) {
                return ((HasCssValue) searchContext).getCssValue(property);
            }

            throw new UnsupportedOperationException(format("It is impossible to get value of the css property %s from " +
                            "the %s instance. Instance of " +
                            "%s or %s or %s is expected.", property, searchContextClass.getName(), WebElement.class.getName(),
                    SearchContext.class.getName(), HasCssValue.class.getName()));
        });
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
     *
     * @param e is the element to get value of the css property
     * @return self-reference
     */
    public SequentialGetCSSValueSupplier of(SearchContext e) {
        isElementDefined = true;
        return super.from(e);
    }

    /**
     * Adds an element to get value of the css property
     *
     * @param searchSupplier is how to find the element to get value of the css property.
     * @param <T>            subtype of {@link SearchContext} which provides ability to get value of the css property.
     * @return self-reference
     */
    public <T extends SearchContext> SequentialGetCSSValueSupplier of(SearchSupplier<T> searchSupplier) {
        isElementDefined = true;
        return super.from(searchSupplier.get().compose(currentContent()));
    }

    @Override
    public Function<SeleniumStepContext, String> get() {
        checkArgument(isElementDefined, "It is necessary to define element to get css property");
        return super.get();
    }
}
