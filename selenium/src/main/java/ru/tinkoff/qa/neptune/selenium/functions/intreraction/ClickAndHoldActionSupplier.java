package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs clicking (without releasing) by left moise button.
 */
public abstract class ClickAndHoldActionSupplier extends InteractiveAction {

    ClickAndHoldActionSupplier() {
        super("Click left mouse button and hold");
    }

    static final class ClickAndHoldSimpleActionSupplier extends ClickAndHoldActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.clickAndHold().perform();
        }
    }

    static final class ClickAndHoldOnAFoundElement extends ClickAndHoldActionSupplier {

        @StepParameter("Element")
        private final SearchContext found;

        ClickAndHoldOnAFoundElement(SearchContext found) {
            super();
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

            value.clickAndHold(e).perform();
        }
    }

    static final class ClickAndHoldOnElementToBeFound extends ClickAndHoldActionSupplier {

        @StepParameter("Element")
        private final SearchSupplier<?> toFind;

        ClickAndHoldOnElementToBeFound(SearchSupplier<?> toFind) {
            super();
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

            value.clickAndHold(e).perform();
        }
    }
}
