package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the releasing of MODIFIER key.
 */
abstract class KeyUpActionSupplier extends InteractiveAction {

    @DescriptionFragment(value = "key", makeReadableBy = CharSequenceParameterValueGetter.class)
    final CharSequence modifierKey;

    KeyUpActionSupplier(CharSequence modifierKey) {
        super();
        checkNotNull(modifierKey);
        this.modifierKey = modifierKey;
    }

    @Description("Release modifier key '{key}'")
    @MaxDepthOfReporting(0)
    static final class KeyUpSimpleActionSupplier extends KeyUpActionSupplier {

        KeyUpSimpleActionSupplier(CharSequence modifierKey) {
            super(modifierKey);
        }

        @Override
        void addAction(Actions value) {
            value.keyUp(modifierKey);
        }
    }

    @Description("Release modifier key '{key}' with focus on {target}")
    @MaxDepthOfReporting(0)
    static final class KeyUpOnElementActionSupplier extends KeyUpActionSupplier {

        @DescriptionFragment("target")
        private final Object e;

        KeyUpOnElementActionSupplier(CharSequence modifierKey, Object e) {
            super(modifierKey);
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.keyUp(getElement(e), modifierKey);
        }
    }
}
