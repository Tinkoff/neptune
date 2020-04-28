package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the context clicking.
 */
public abstract class ContextClickActionSupplier extends InteractiveAction {

    ContextClickActionSupplier() {
        super("Context click");
    }

    static final class ContextClickSimpleActionSupplier extends ContextClickActionSupplier {

        @Override
        protected void performActionOn(Actions value) {
            value.contextClick().perform();
        }
    }

    static final class ContextClickOnAFoundElement extends ContextClickActionSupplier {

        @StepParameter("Element to perform the context clicking on")
        private final SearchContext found;

        ContextClickOnAFoundElement(SearchContext found) {
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

            value.contextClick(e).perform();
        }
    }

    static final class ContextClickOnAnElementToBeFound extends ContextClickActionSupplier {

        @StepParameter("Element to perform the context clicking on")
        private final SearchSupplier<?> toFind;

        ContextClickOnAnElementToBeFound(SearchSupplier<?> toFind) {
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

            value.contextClick(e).perform();
        }
    }
}
