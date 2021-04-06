package ru.tinkoff.qa.neptune.selenium.functions.elements;

import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasLocation;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@Description("Position of the {element} on a page")
public final class GetElementPositionSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Point, SearchContext, GetElementPositionSupplier> {

    @DescriptionFragment("element")
    final Object element;

    private GetElementPositionSupplier(Object element) {
        super(s -> {
            var cls = s.getClass();

            if (WebElement.class.isAssignableFrom(cls)) {
                return ((WebElement) s).getLocation();
            }

            if (HasLocation.class.isAssignableFrom(cls)) {
                return ((HasLocation) s).getLocation();
            }

            throw new UnsupportedOperationException("It is not possible to get position of " + s.toString());
        });
        this.element = element;
    }

    private <T extends SearchContext> GetElementPositionSupplier(SearchSupplier<T> supplier) {
        this((Object) supplier);
        from(supplier.get().compose(currentContent()));
    }

    private GetElementPositionSupplier(SearchContext context) {
        this((Object) context);
        from(context);
    }

    /**
     * Builds a function that retrieves position of a web element|widget on a page.
     *
     * @param supplier is how to get the web element/widget to get position from
     * @return Supplier of a function that gets position.
     */
    public static <T extends SearchContext> GetElementPositionSupplier positionOfElement(SearchSupplier<T> supplier) {
        return new GetElementPositionSupplier(supplier);
    }

    /**
     * Builds a function that retrieves position of a web element/widget on a page.
     *
     * @param context is the web element/widget to get position from
     * @return Supplier of a function that gets position.
     */
    public static GetElementPositionSupplier positionOfElement(SearchContext context) {
        return new GetElementPositionSupplier(context);
    }
}
