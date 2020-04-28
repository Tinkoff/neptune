package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that releases the pressed left mouse button.
 */
abstract class ReleaseActionSupplier extends InteractiveAction {

    ReleaseActionSupplier() {
        super("Release left mouse button");
    }

    static final class ReleaseSimpleActionSupplier extends ReleaseActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.release().perform();
        }
    }

    static final class ReleaseElementActionSupplier extends ReleaseActionSupplier {

        @StepParameter("Element")
        private final Object e;

        ReleaseElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        protected void performActionOn(Actions value) {
            value.release(getElement(e)).perform();
        }
    }
}
