package ru.tinkoff.qa.neptune.selenium.functions.expand;

import org.openqa.selenium.SearchContext;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Expandable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Element to expand"
)
public final class ExpandActionSupplier extends
        SequentialActionSupplier<SeleniumStepContext, Expandable, ExpandActionSupplier> {

    private ExpandActionSupplier() {
        super();
    }

    /**
     * Builds the expand action on some expandable element
     *
     * @param of  is how to find the expandable element
     * @param <R> is the type of the expandable element
     * @return built expand action
     */
    @Description("Expand element {of}")
    public static <R extends SearchContext & Expandable> ExpandActionSupplier expand(
            @DescriptionFragment("of") SearchSupplier<R> of) {
        return new ExpandActionSupplier()
                .performOn(of.get().compose(currentContent()));
    }

    /**
     * Builds the expand action on some expandable element
     *
     * @param of  is the expandable element
     * @param <R> is the type of the expandable element
     * @return built expand action
     */
    @Description("Expand element {of}")
    public static <R extends SearchContext & Expandable> ExpandActionSupplier expand(@DescriptionFragment("of") R of) {
        return new ExpandActionSupplier()
                .performOn(of);
    }

    @Override
    protected void performActionOn(Expandable value) {
        if (!value.isExpanded()) {
            value.expand();
        }
    }
}
