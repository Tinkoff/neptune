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

@SequentialGetStepSupplier.DefineFromParameterName("Element to get value of html attribute")
public final class SequentialGetAttributeValueSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, String, SearchContext, SequentialGetAttributeValueSupplier> {

    private SequentialGetAttributeValueSupplier(String attr) {
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
    }


    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the getting of value of the attribute.
     *
     * @param attr is the name of the target attribute
     * @param e    is the element to get attr value from
     * @return an instance of {@link SequentialGetAttributeValueSupplier}
     */
    @Description("Value of html attribute '{attr}' of the {element}")
    public static SequentialGetAttributeValueSupplier attributeValue(@DescriptionFragment("attr") String attr,
                                                                     @DescriptionFragment("element") WebElement e) {
        return new SequentialGetAttributeValueSupplier(attr).from(e);
    }

    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the getting of value of the attribute.
     *
     * @param attr is the name of the target attribute
     * @param e    is the element to get attr value from
     * @return an instance of {@link SequentialGetAttributeValueSupplier}
     */
    @Description("Value of html attribute '{attr}' of the {element}")
    public static SequentialGetAttributeValueSupplier attributeValue(@DescriptionFragment("attr") String attr,
                                                                     @DescriptionFragment("element") Widget e) {
        return new SequentialGetAttributeValueSupplier(attr).from(e);
    }


    /**
     * Creates an instance of {@link SequentialGetAttributeValueSupplier} for the getting of value of the attribute.
     *
     * @param attr           is the name of the target attribute
     * @param searchSupplier is how to find the element to get value of the attribute.
     * @return self-reference
     */
    @Description("Value of html attribute '{attr}' of the {element}")
    public static SequentialGetAttributeValueSupplier attributeValue(@DescriptionFragment("attr") String attr,
                                                                     @DescriptionFragment("element") SearchSupplier<?> searchSupplier) {
        return new SequentialGetAttributeValueSupplier(attr).from(searchSupplier.get().compose(currentContent()));
    }
}
