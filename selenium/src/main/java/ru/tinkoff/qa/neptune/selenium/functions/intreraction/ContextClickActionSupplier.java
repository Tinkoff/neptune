package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the context clicking.
 */
abstract class ContextClickActionSupplier extends InteractiveAction {

    ContextClickActionSupplier() {
        super();
    }

    static final class ContextClickSimpleActionSupplier extends ContextClickActionSupplier {
        @Override
        void addAction(Actions value) {
            value.contextClick();
        }
    }

    static final class ContextClickOnElementActionSupplier extends ContextClickActionSupplier {

        @StepParameter("Element to perform the context clicking on")
        private final Object e;

        ContextClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.contextClick(getElement(e));
        }
    }
}
