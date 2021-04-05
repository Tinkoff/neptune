package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;

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
