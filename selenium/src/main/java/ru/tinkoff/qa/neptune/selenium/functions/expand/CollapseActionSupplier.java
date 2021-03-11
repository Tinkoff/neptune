package ru.tinkoff.qa.neptune.selenium.functions.expand;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Expandable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Element to collapse"
)
public final class CollapseActionSupplier extends
        SequentialActionSupplier<SeleniumStepContext, Expandable, CollapseActionSupplier> {

    private CollapseActionSupplier(String description) {
        super(description);
    }

    /**
     * Builds the collapse action on some expandable/collapsable element
     *
     * @param of  is how to find the expandable/collapsable element
     * @param <R> is the type of the expandable/collapsable element
     * @return built collapse action
     */
    public static <R extends SearchContext & Expandable> CollapseActionSupplier collapse(
            SearchSupplier<R> of) {
        return new CollapseActionSupplier("Collapse element " + of)
                .performOn(of.get().compose(currentContent()));
    }

    /**
     * Builds the collapse action on some expandable/collapsable element
     *
     * @param of  is the expandable/collapsable element
     * @param <R> is the type of the expandable/collapsable element
     * @return built collapse action
     */
    public static <R extends SearchContext & Expandable> CollapseActionSupplier collapse(R of) {
        return new CollapseActionSupplier("Collapse element " + of)
                .performOn(of);
    }

    @Override
    protected void performActionOn(Expandable value) {
        if (value.isExpanded()) {
            value.collapse();
        }
    }
}
