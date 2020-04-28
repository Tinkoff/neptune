package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the MODIFIER key pressing.
 */
public abstract class KeyDownActionSupplier extends InteractiveAction {

    @StepParameter(value = "Modifier key", makeReadableBy = CharSequenceParameterValueGetter.class)
    final CharSequence modifierKey;

    KeyDownActionSupplier(CharSequence modifierKey) {
        super("Press modifier key down");
        checkNotNull(modifierKey);
        this.modifierKey = modifierKey;
    }

    static final class KeyDownSimpleActionSupplier extends KeyDownActionSupplier {

        KeyDownSimpleActionSupplier(CharSequence modifierKey) {
            super(modifierKey);
        }

        @Override
        protected void performActionOn(Actions value) {
            value.keyDown(modifierKey).perform();
        }
    }

    static final class KeyDownOnAFoundElement extends KeyDownActionSupplier {

        @StepParameter("Element")
        private final SearchContext found;

        private KeyDownOnAFoundElement(CharSequence modifierKey, SearchContext found) {
            super(modifierKey);
            checkNotNull(found);
            this.found = found;
        }

        KeyDownOnAFoundElement(CharSequence modifierKey, WebElement found) {
            this(modifierKey, (SearchContext) found);
        }

        KeyDownOnAFoundElement(CharSequence modifierKey, Widget found) {
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

            value.keyDown(e, modifierKey).perform();
        }
    }

    static final class KeyDownOnElementToBeFound extends KeyDownActionSupplier {

        @StepParameter("Element")
        private final SearchSupplier<?> toFind;

        KeyDownOnElementToBeFound(CharSequence modifierKey, SearchSupplier<?> toFind) {
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

            value.keyDown(e, modifierKey).perform();
        }
    }
}
