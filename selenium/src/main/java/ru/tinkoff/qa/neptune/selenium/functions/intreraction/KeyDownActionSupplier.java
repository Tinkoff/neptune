package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the MODIFIER key pressing.
 */
abstract class KeyDownActionSupplier extends InteractiveAction {

    final CharSequence modifierKey;

    KeyDownActionSupplier(CharSequence modifierKey) {
        super();
        checkNotNull(modifierKey);
        this.modifierKey = modifierKey;
    }

    static final class KeyDownSimpleActionSupplier extends KeyDownActionSupplier {

        KeyDownSimpleActionSupplier(CharSequence modifierKey) {
            super(modifierKey);
        }

        @Override
        void addAction(Actions value) {
            value.keyDown(modifierKey);
        }
    }

    static final class KeyDownOnElementActionSupplier extends KeyDownActionSupplier {

        private final Object e;

        KeyDownOnElementActionSupplier(CharSequence modifierKey, Object e) {
            super(modifierKey);
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.keyDown(getElement(e), modifierKey);
        }
    }
}
