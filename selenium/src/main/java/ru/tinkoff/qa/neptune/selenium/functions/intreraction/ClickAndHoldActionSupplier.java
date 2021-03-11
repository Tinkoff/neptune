package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs clicking (without releasing) by left moise button.
 */
@Description("Click left mouse button and hold")
abstract class ClickAndHoldActionSupplier extends InteractiveAction {

    ClickAndHoldActionSupplier() {
        super("Click left mouse button and hold");
    }

    static final class ClickAndHoldSimpleActionSupplier extends ClickAndHoldActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.clickAndHold().perform();
        }
    }

    static final class ClickAndHoldOnElementActionSupplier extends ClickAndHoldActionSupplier {

        @StepParameter("Element")
        private final Object e;

        ClickAndHoldOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        protected void performActionOn(Actions value) {
            value.clickAndHold(getElement(e)).perform();
        }
    }
}
