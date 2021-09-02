package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs clicking (without releasing) by left moise button.
 */
@Description("Click left mouse button and hold")
abstract class ClickAndHoldActionSupplier extends InteractiveAction {

    ClickAndHoldActionSupplier() {
        super();
    }

    @MaxDepthOfReporting(0)
    static final class ClickAndHoldSimpleActionSupplier extends ClickAndHoldActionSupplier {

        @Override
        void addAction(Actions value) {
            value.clickAndHold();
        }
    }

    @Description("Click left mouse button and hold on {target}")
    @MaxDepthOfReporting(0)
    static final class ClickAndHoldOnElementActionSupplier extends ClickAndHoldActionSupplier {

        @DescriptionFragment("target")
        private final Object e;

        ClickAndHoldOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.clickAndHold(getElement(e));
        }
    }
}
