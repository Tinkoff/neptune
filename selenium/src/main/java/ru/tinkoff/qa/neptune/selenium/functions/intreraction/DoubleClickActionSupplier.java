package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the double clicking.
 */
abstract class DoubleClickActionSupplier extends InteractiveAction {

    DoubleClickActionSupplier() {
        super();
    }

    static final class DoubleClickSimpleActionSupplier extends DoubleClickActionSupplier {
        @Override
        void addAction(Actions value) {
            value.doubleClick();
        }
    }

    static final class DoubleClickOnElementActionSupplier extends DoubleClickActionSupplier {

        private final Object e;

        DoubleClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.doubleClick(getElement(e));
        }
    }
}
