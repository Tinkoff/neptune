package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the clicking by left mouse button.
 */
public abstract class ClickActionSupplier extends InteractiveAction {

    ClickActionSupplier() {
        super("Click by left mouse button");
    }

    static final class ClickSimpleActionSupplier extends ClickActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.click().perform();
        }
    }

    static final class ClickOnAFoundElement extends ClickActionSupplier {

        @StepParameter("Element to click on")
        private final SearchContext found;

        ClickOnAFoundElement(SearchContext found) {
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

            value.click(e).perform();
        }
    }

    static final class ClickOnAnElementToBeFound extends ClickActionSupplier {

        @StepParameter("Element to click on")
        private final SearchSupplier<?> toFind;

        ClickOnAnElementToBeFound(SearchSupplier<?> toFind) {
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

            value.click(e).perform();
        }
    }
}
