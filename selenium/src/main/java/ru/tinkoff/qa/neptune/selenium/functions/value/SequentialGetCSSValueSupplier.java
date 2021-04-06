package ru.tinkoff.qa.neptune.selenium.functions.value;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasCssValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@Description("Value of css property '{property}' of the {element}")
public final class SequentialGetCSSValueSupplier extends
        SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, String, SearchContext, SequentialGetCSSValueSupplier> {

    @DescriptionFragment("property")
    final String property;

    @DescriptionFragment("element")
    final Object element;

    private SequentialGetCSSValueSupplier(String property, Object element) {
        super(searchContext -> {
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
        this.property = property;
        this.element = element;
    }

    private SequentialGetCSSValueSupplier(String property, SearchContext element) {
        this(property, (Object) element);
        from(element);
    }

    private SequentialGetCSSValueSupplier(String property, SearchSupplier<?> searchSupplier) {
        this(property, (Object) searchSupplier);
        from(searchSupplier.get().compose(currentContent()));
    }


    /**
     * Creates an instance of {@link SequentialGetCSSValueSupplier} for the taking of value of the css property.
     *
     * @param property is the name of the target css property
     * @param e        is the element to get css property value from
     * @return an instance of {@link SequentialGetCSSValueSupplier}
     */
    public static SequentialGetCSSValueSupplier cssValue(String property, WebElement e) {
        return new SequentialGetCSSValueSupplier(property, e);
    }

    /**
     * Creates an instance of {@link SequentialGetCSSValueSupplier} for the taking of value of the css property.
     *
     * @param property is the name of the target css property
     * @param e        is the element to get css property value from
     * @return an instance of {@link SequentialGetCSSValueSupplier}
     */
    public static SequentialGetCSSValueSupplier cssValue(String property, Widget e) {
        return new SequentialGetCSSValueSupplier(property, e);
    }

    /**
     * Creates an instance of {@link SequentialGetCSSValueSupplier} for the taking of value of the css property.
     *
     * @param property       is the name of the target css property
     * @param searchSupplier is how to find the element to get value of the css property.
     * @return an instance of {@link SequentialGetCSSValueSupplier}
     */
    public static SequentialGetCSSValueSupplier cssValue(String property, SearchSupplier<?> searchSupplier) {
        return new SequentialGetCSSValueSupplier(property, searchSupplier);
    }
}
