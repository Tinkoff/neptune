package ru.tinkoff.qa.neptune.selenium.functions.expand;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.selenium.api.widget.Expandable;
import ru.tinkoff.qa.neptune.selenium.captors.ImageCaptorAfterActionOnElement;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

@CaptureOnSuccess(by = ImageCaptorAfterActionOnElement.class)
@CaptureOnFailure(by = {WebElementImageCaptor.class, WebDriverImageCaptor.class})
@Description("Collapse {toCollapse}")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class CollapseActionSupplier extends
        SequentialActionSupplier<Object, Expandable, CollapseActionSupplier> {

    @DescriptionFragment("toCollapse")
    final Object toCollapse;

    private CollapseActionSupplier(Object toCollapse) {
        super();
        this.toCollapse = toCollapse;
    }

    private <R extends SearchContext & Expandable> CollapseActionSupplier(R toCollapse) {
        this((Object) toCollapse);
        performOn(toCollapse);
    }

    private <R extends SearchContext & Expandable> CollapseActionSupplier(SearchSupplier<R> toCollapse) {
        this((Object) toCollapse);
        performOn(toCollapse);
    }

    /**
     * Builds the collapse action on some expandable/collapsable element
     *
     * @param of  is how to find the expandable/collapsable element
     * @param <R> is the type of the expandable/collapsable element
     * @return built collapse action
     */
    public static <R extends SearchContext & Expandable> CollapseActionSupplier collapse(SearchSupplier<R> of) {
        return new CollapseActionSupplier(of);
    }

    /**
     * Builds the collapse action on some expandable/collapsable element
     *
     * @param of  is the expandable/collapsable element
     * @param <R> is the type of the expandable/collapsable element
     * @return built collapse action
     */
    public static <R extends SearchContext & Expandable> CollapseActionSupplier collapse(R of) {
        return new CollapseActionSupplier(of);
    }

    @Override
    protected void howToPerform(Expandable value) {
        if (value.isExpanded()) {
            value.collapse();
        }
    }
}
