package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that moves the mouse to an offset from the top-left corner of a web element.
 */
final class MoveToElementActionSupplier extends InteractiveAction {

    @StepParameter("Element")
    private final Object e;

    @StepParameter(value = "X offset", doNotReportNullValues = true)
    final Integer x;

    @StepParameter(value = "Y offset", doNotReportNullValues = true)
    final Integer y;

    MoveToElementActionSupplier(Object e, Integer x, Integer y) {
        super("Mouse move to element");
        this.x = x;
        this.y = y;
        checkNotNull(e);
        this.e = e;
    }

    @Override
    protected void performActionOn(Actions value) {
        if (x == null || y == null) {
            value.moveToElement(getElement(e)).perform();
        } else {
            value.moveToElement(getElement(e), x, y).perform();
        }
    }
}
