package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the MODIFIER key pressing.
 */
abstract class KeyDownActionSupplier extends InteractiveAction {

    @StepParameter(value = "Modifier key", makeReadableBy = CharSequenceParameterValueGetter.class)
    final CharSequence modifierKey;

    KeyDownActionSupplier(CharSequence modifierKey) {
        super();
        checkNotNull(modifierKey);
        this.modifierKey = modifierKey;
    }

    @Description("Press modifier key down")
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

        @StepParameter("Element")
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
