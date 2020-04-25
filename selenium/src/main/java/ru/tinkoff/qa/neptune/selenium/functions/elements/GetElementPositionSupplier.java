package ru.tinkoff.qa.neptune.selenium.functions.elements;

import org.openqa.selenium.Point;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasLocation;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

public final class GetElementPositionSupplier extends SequentialGetStepSupplier
        .GetObjectChainedStepSupplier<SeleniumStepContext, Point, SearchContext, GetElementPositionSupplier> {

    private GetElementPositionSupplier() {
        super("Position of the element", s -> {
            var cls = s.getClass();

            if (WebElement.class.isAssignableFrom(cls)) {
                return ((WebElement) s).getLocation();
            }

            if (HasLocation.class.isAssignableFrom(cls)) {
                return ((HasLocation) s).getLocation();
            }

            throw new IllegalArgumentException("It is not possible to get position of " + s.toString());
        });
    }

    /**
     * Builds a function that retrieves position of a web element|widget on a page.
     *
     * @param supplier is how to get the web element/widget to get position from
     * @return Supplier of a function that gets position.
     */
    public static <T extends SearchContext> GetElementPositionSupplier positionOfElement(SearchSupplier<T> supplier) {
        return new GetElementPositionSupplier().from(supplier.get().compose(currentContent()));
    }

    /**
     * Builds a function that retrieves position of a web element/widget on a page.
     *
     * @param context is the web element/widget to get position from
     * @return Supplier of a function that gets position.
     */
    public static GetElementPositionSupplier positionOfElement(SearchContext context) {
        return new GetElementPositionSupplier().from(context);
    }
}
