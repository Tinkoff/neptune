package ru.tinkoff.qa.neptune.selenium.functions.elements;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasSize;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

@Description("Size of the {element}")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class GetElementSizeSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<Object, Dimension, SearchContext, GetElementSizeSupplier> {

    @DescriptionFragment("element")
    final Object element;

    private GetElementSizeSupplier(Object element) {
        super(s -> {
            var cls = s.getClass();

            if (WebElement.class.isAssignableFrom(cls)) {
                return ((WebElement) s).getSize();
            }

            if (HasSize.class.isAssignableFrom(cls)) {
                return ((HasSize) s).getSize();
            }

            throw new UnsupportedOperationException("It is not possible to get size of " + s.toString());
        });
        this.element = element;
    }

    private GetElementSizeSupplier(SearchContext context) {
        this((Object) context);
        from(context);
    }

    private <T extends SearchContext> GetElementSizeSupplier(SearchSupplier<T> supplier) {
        this((Object) supplier);
        from(supplier);
    }

    /**
     * Builds a function that retrieves size of a web element|widget.
     *
     * @param supplier is how to get the web element/widget to get size from
     * @return Supplier of a function which gets size.
     */
    public static <T extends SearchContext> GetElementSizeSupplier elementSize(SearchSupplier<T> supplier) {
        return new GetElementSizeSupplier(supplier);
    }

    /**
     * Builds a function that retrieves size of a web element/widget.
     *
     * @param context is the web element/widget to get size from
     * @return Supplier of a function which gets size.
     */
    public static GetElementSizeSupplier elementSize(SearchContext context) {
        return new GetElementSizeSupplier(context);
    }
}
