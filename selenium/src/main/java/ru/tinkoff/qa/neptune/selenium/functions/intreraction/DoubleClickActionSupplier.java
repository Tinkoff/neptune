package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the double clicking.
 */
abstract class DoubleClickActionSupplier extends InteractiveAction {

    DoubleClickActionSupplier() {
        super("Double click");
    }

    static final class DoubleClickSimpleActionSupplier extends DoubleClickActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.doubleClick().perform();
        }
    }

    static final class DoubleClickOnElementActionSupplier extends DoubleClickActionSupplier {

        @StepParameter("Element to perform the double clicking on")
        private final Object e;

        DoubleClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        protected void performActionOn(Actions value) {
            value.doubleClick(getElement(e)).perform();
        }
    }
}
