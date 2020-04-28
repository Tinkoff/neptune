package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the sending keys.
 */
public abstract class SendKeysActionSupplier extends InteractiveAction {

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

    static final class SendKeysToAFoundElement extends SendKeysActionSupplier {

        @StepParameter("Element")
        private final SearchContext found;

        SendKeysToAFoundElement(SearchContext found, CharSequence... keys) {
            super(keys);
            checkNotNull(found);
            this.found = found;
        }

        @Override
        protected void performActionOn(Actions value) {
            WebElement e;
            if (WebElement.class.isAssignableFrom(found.getClass())) {
                e = (WebElement) found;
            } else {
                e = ((Widget) found).getWrappedElement();
            }

            value.sendKeys(e, keys).perform();
        }
    }

    static final class SendKeysToElementToBeFound extends SendKeysActionSupplier {

        @StepParameter("Element")
        private final SearchSupplier<?> toFind;

        SendKeysToElementToBeFound(SearchSupplier<?> toFind, CharSequence... keys) {
            super(keys);
            checkNotNull(toFind);
            this.toFind = toFind;
        }

        @Override
        protected void performActionOn(Actions value) {
            var found = toFind.get().apply(getDriver());

            WebElement e;
            if (WebElement.class.isAssignableFrom(found.getClass())) {
                e = (WebElement) found;
            } else {
                e = ((Widget) found).getWrappedElement();
            }

            value.sendKeys(e, keys).perform();
        }
    }
}
