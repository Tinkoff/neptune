package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the releasing of MODIFIER key.
 */
public abstract class KeyUpActionSupplier extends InteractiveAction {

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

    static final class KeyUpOnAFoundElement extends KeyUpActionSupplier {

        @StepParameter("Element")
        private final SearchContext found;

        private KeyUpOnAFoundElement(CharSequence modifierKey, SearchContext found) {
            super(modifierKey);
            checkNotNull(found);
            this.found = found;
        }

        KeyUpOnAFoundElement(CharSequence modifierKey, WebElement found) {
            this(modifierKey, (SearchContext) found);
        }

        KeyUpOnAFoundElement(CharSequence modifierKey, Widget found) {
            this(modifierKey, (SearchContext) found);
        }

        @Override
        protected void performActionOn(Actions value) {
            WebElement e;
            if (WebElement.class.isAssignableFrom(found.getClass())) {
                e = (WebElement) found;
            } else {
                e = ((Widget) found).getWrappedElement();
            }

            value.keyUp(e, modifierKey).perform();
        }
    }

    static final class KeyUpOnElementToBeFound extends KeyUpActionSupplier {

        @StepParameter("Element")
        private final SearchSupplier<?> toFind;

        KeyUpOnElementToBeFound(CharSequence modifierKey, SearchSupplier<?> toFind) {
            super(modifierKey);
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

            value.keyUp(e, modifierKey).perform();
        }
    }
}
