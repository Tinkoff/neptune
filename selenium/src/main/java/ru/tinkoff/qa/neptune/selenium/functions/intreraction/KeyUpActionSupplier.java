package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the releasing of MODIFIER key.
 */
abstract class KeyUpActionSupplier extends InteractiveAction {

    @StepParameter(value = "Modifier key", makeReadableBy = CharSequenceParameterValueGetter.class)
    final CharSequence modifierKey;

    KeyUpActionSupplier(CharSequence modifierKey) {
        super("Release modifier key");
        checkNotNull(modifierKey);
        this.modifierKey = modifierKey;
    }

    static final class KeyUpSimpleActionSupplier extends KeyUpActionSupplier {

        KeyUpSimpleActionSupplier(CharSequence modifierKey) {
            super(modifierKey);
        }

        @Override
        protected void performActionOn(Actions value) {
            value.keyUp(modifierKey).perform();
        }
    }

    static final class KeyUpOnElementActionSupplier extends KeyUpActionSupplier {

        @StepParameter("Element")
        private final Object e;

        KeyUpOnElementActionSupplier(CharSequence modifierKey, Object e) {
            super(modifierKey);
            checkNotNull(e);
            this.e = e;
        }

        @Override
        protected void performActionOn(Actions value) {
            value.keyUp(getElement(e), modifierKey).perform();
        }
    }
}
