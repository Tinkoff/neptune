package ru.tinkoff.qa.neptune.selenium.functions.expand;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.api.widget.Expandable;
import ru.tinkoff.qa.neptune.selenium.captors.ImageCaptorAfterActionOnElement;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

@CaptureOnSuccess(by = ImageCaptorAfterActionOnElement.class)
@CaptureOnFailure(by = {WebElementImageCaptor.class, WebDriverImageCaptor.class})
@Description("Expand {toExpand}")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class ExpandActionSupplier extends
        SequentialActionSupplier<Object, Expandable, ExpandActionSupplier> {

    @DescriptionFragment("toExpand")
    final Object toExpand;

    private ExpandActionSupplier(Object toExpand) {
        super();
        this.toExpand = toExpand;
    }

    private <R extends SearchContext & Expandable> ExpandActionSupplier(R toExpand) {
        this((Object) toExpand);
        performOn(toExpand);
    }

    private <R extends SearchContext & Expandable> ExpandActionSupplier(SearchSupplier<R> toExpand) {
        this((Object) toExpand);
        performOn(toExpand);
    }

    /**
     * Builds the expand action on some expandable element
     *
     * @param of  is how to find the expandable element
     * @param <R> is the type of the expandable element
     * @return built expand action
     */
    public static <R extends SearchContext & Expandable> ExpandActionSupplier expand(SearchSupplier<R> of) {
        return new ExpandActionSupplier(of);
    }

    /**
     * Builds the expand action on some expandable element
     *
     * @param of  is the expandable element
     * @param <R> is the type of the expandable element
     * @return built expand action
     */
    public static <R extends SearchContext & Expandable> ExpandActionSupplier expand(R of) {
        return new ExpandActionSupplier(of);
    }

    @Override
    protected void howToPerform(Expandable value) {
        if (!value.isExpanded()) {
            value.expand();
        }
    }
}
