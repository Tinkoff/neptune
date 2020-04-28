package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that moves the mouse to an offset from the top-left corner of a web element.
 */
public abstract class MoveToElementActionSupplier extends InteractiveAction {

    @StepParameter(value = "X offset", doNotReportNullValues = true)
    final Integer x;

    @StepParameter(value = "Y offset", doNotReportNullValues = true)
    final Integer y;

    MoveToElementActionSupplier(Integer x, Integer y) {
        super("Move to element");
        this.x = x;
        this.y = y;
    }

    static final class MoveToAFoundElement extends MoveToElementActionSupplier {

        @StepParameter("Element")
        private final SearchContext found;

        MoveToAFoundElement(SearchContext found, Integer x, Integer y) {
            super(x, y);
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


            if (x == null || y == null) {
                value.moveToElement(e).perform();
            } else {
                value.moveToElement(e, x, y).perform();
            }
        }
    }

    static final class MoveToAnElementToBeFound extends MoveToElementActionSupplier {

        @StepParameter("Element")
        private final SearchSupplier<?> toFind;

        MoveToAnElementToBeFound(SearchSupplier<?> toFind, Integer x, Integer y) {
            super(x, y);
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


            if (x == null || y == null) {
                value.moveToElement(e).perform();
            } else {
                value.moveToElement(e, x, y).perform();
            }
        }
    }
}
