package ru.tinkoff.qa.neptune.selenium.functions.value;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasAttribute;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@Description("Value of html attribute '{attr}' of the {element}")
public final class SequentialGetAttributeValueSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, String, SearchContext, SequentialGetAttributeValueSupplier> {

    @DescriptionFragment("attr")
    final String attr;

    @DescriptionFragment("element")
    final Object element;

    private SequentialGetAttributeValueSupplier(String attr, Object element) {
        super(searchContext -> {
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

        this.attr = attr;
        this.element = element;
    }

    private SequentialGetAttributeValueSupplier(String attr, SearchContext element) {
        this(attr, (Object) element);
        from(element);
    }

    private SequentialGetAttributeValueSupplier(String attr, SearchSupplier<?> searchSupplier) {
        this(attr, (Object) searchSupplier);
        from(searchSupplier.get().compose(currentContent()));
    }


    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the getting of value of the attribute.
     *
     * @param attr is the name of the target attribute
     * @param e    is the element to get attr value from
     * @return an instance of {@link SequentialGetAttributeValueSupplier}
     */
    public static SequentialGetAttributeValueSupplier attributeValue(String attr, WebElement e) {
        return new SequentialGetAttributeValueSupplier(attr, e);
    }

    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the getting of value of the attribute.
     *
     * @param attr is the name of the target attribute
     * @param e    is the element to get attr value from
     * @return an instance of {@link SequentialGetAttributeValueSupplier}
     */
    public static SequentialGetAttributeValueSupplier attributeValue(String attr, Widget e) {
        return new SequentialGetAttributeValueSupplier(attr, e);
    }


    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the getting of value of the attribute.
     *
     * @param attr           is the name of the target attribute
     * @param searchSupplier is how to find the element to get value of the attribute.
     * @return self-reference
     */
    public static SequentialGetAttributeValueSupplier attributeValue(String attr, SearchSupplier<?> searchSupplier) {
        return new SequentialGetAttributeValueSupplier(attr, searchSupplier);
    }
}
