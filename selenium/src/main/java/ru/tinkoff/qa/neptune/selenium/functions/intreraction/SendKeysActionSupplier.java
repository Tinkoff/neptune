package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

abstract class SendKeysActionSupplier extends InteractiveAction {

    @DescriptionFragment(
            value = "keysToSend",
            makeReadableBy = CharSequencesParameterValueGetter.class)
    final CharSequence[] keys;

    SendKeysActionSupplier(CharSequence... keys) {
        super();
        checkNotNull(keys);
        checkArgument(keys.length > 0, "Should be defined at least one key to be sent");
        this.keys = keys;
    }

    @Description("Send keys '{keysToSend}'")
    @MaxDepthOfReporting(0)
    static final class SendKeysSimpleActionSupplier extends SendKeysActionSupplier {

        SendKeysSimpleActionSupplier(CharSequence... keys) {
            super(keys);
        }

        @Override
        void addAction(Actions value) {
            value.sendKeys(keys);
        }
    }

    @Description("Send keys '{keysToSend}' to {target}")
    @MaxDepthOfReporting(0)
    static final class SendKeysToElementActionSupplier extends SendKeysActionSupplier {

        @StepParameter("Element")
        private final Object e;

        SendKeysToElementActionSupplier(Object e, CharSequence... keys) {
            super(keys);
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.sendKeys(getElement(e), keys);
        }
    }
}
