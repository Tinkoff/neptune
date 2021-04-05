package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that releases the pressed left mouse button.
 */
abstract class ReleaseActionSupplier extends InteractiveAction {

    ReleaseActionSupplier() {
        super();
    }

    static final class ReleaseSimpleActionSupplier extends ReleaseActionSupplier {

        @Override
        void addAction(Actions value) {
            value.release();
        }
    }

    static final class ReleaseElementActionSupplier extends ReleaseActionSupplier {

        private final Object e;

        ReleaseElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.release(getElement(e));
        }
    }
}
