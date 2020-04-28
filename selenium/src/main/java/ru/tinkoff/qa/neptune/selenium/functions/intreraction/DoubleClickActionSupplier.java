package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the double clicking.
 */
public abstract class DoubleClickActionSupplier extends InteractiveAction {

    DoubleClickActionSupplier() {
        super("Double click");
    }

    static final class DoubleClickSimpleActionSupplier extends DoubleClickActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.doubleClick().perform();
        }
    }

    static final class DoubleClickOnAFoundElement extends DoubleClickActionSupplier {

        @StepParameter("Element to perform the double clicking on")
        private final SearchContext found;

        DoubleClickOnAFoundElement(SearchContext found) {
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

            value.doubleClick(e).perform();
        }
    }

    static final class DoubleClickOnAnElementToBeFound extends DoubleClickActionSupplier {

        @StepParameter("Element to perform the double clicking on")
        private final SearchSupplier<?> toFind;

        DoubleClickOnAnElementToBeFound(SearchSupplier<?> toFind) {
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

            value.doubleClick(e).perform();
        }
    }
}
