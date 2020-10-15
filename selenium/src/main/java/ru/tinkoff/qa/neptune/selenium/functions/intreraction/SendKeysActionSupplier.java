package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the sending keys.
 */
abstract class SendKeysActionSupplier extends InteractiveAction {

    @StepParameter(value = "Keys to send", makeReadableBy = CharSequencesParameterValueGetter.class)
    final CharSequence[] keys;

    SendKeysActionSupplier(CharSequence... keys) {
        super("Send keys");
        checkNotNull(keys);
        checkArgument(keys.length > 0, "Should be defined at least one key to be sent");
        this.keys = keys;
    }

    static final class SendKeysSimpleActionSupplier extends SendKeysActionSupplier {

        SendKeysSimpleActionSupplier(CharSequence... keys) {
            super(keys);
        }

        @Override
        protected void performActionOn(Actions value) {
            value.sendKeys(keys).perform();
        }
    }

    static final class SendKeysToElementActionSupplier extends SendKeysActionSupplier {

        @StepParameter("Element")
        private final Object e;

        SendKeysToElementActionSupplier(Object e, CharSequence... keys) {
            super(keys);
            checkNotNull(e);
            this.e = e;
        }

        @Override
        protected void performActionOn(Actions value) {
            value.sendKeys(getElement(e), keys).perform();
        }
    }
}
