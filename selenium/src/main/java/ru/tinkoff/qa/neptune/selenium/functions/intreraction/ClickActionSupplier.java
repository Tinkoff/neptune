package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the clicking by left mouse button.
 */
abstract class ClickActionSupplier extends InteractiveAction {

    ClickActionSupplier() {
        super();
    }

    static final class ClickSimpleActionSupplier extends ClickActionSupplier {

        @Override
        void addAction(Actions value) {
            value.click();
        }
    }

    static final class ClickOnElementActionSupplier extends ClickActionSupplier {

        private final Object e;

        ClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.click(getElement(e));
        }
    }
}
