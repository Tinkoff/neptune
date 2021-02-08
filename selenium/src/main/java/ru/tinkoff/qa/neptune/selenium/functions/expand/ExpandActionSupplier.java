package ru.tinkoff.qa.neptune.selenium.functions.expand;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Expandable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Element to expand"
)
public final class ExpandActionSupplier extends
        SequentialActionSupplier<SeleniumStepContext, Expandable, ExpandActionSupplier> {

    private ExpandActionSupplier(String description) {
        super(description);
    }

    /**
     * Builds the expand action on some expandable element
     *
     * @param of  is how to find the expandable element
     * @param <R> is the type of the expandable element
     * @return built expand action
     */
    public static <R extends SearchContext & Expandable> ExpandActionSupplier expand(
            SearchSupplier<R> of) {
        return new ExpandActionSupplier("Expand element " + of)
                .performOn(of.get().compose(currentContent()));
    }

    /**
     * Builds the expand action on some expandable element
     *
     * @param of  is the expandable element
     * @param <R> is the type of the expandable element
     * @return built expand action
     */
    public static <R extends SearchContext & Expandable> ExpandActionSupplier expand(R of) {
        return new ExpandActionSupplier("Expand element " + of)
                .performOn(of);
    }

    @Override
    protected void performActionOn(Expandable value) {
        if (!value.isExpanded()) {
            value.expand();
        }
    }
}
