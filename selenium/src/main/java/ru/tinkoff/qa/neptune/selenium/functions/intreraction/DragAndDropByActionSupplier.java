package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs click-and-hold at the location of the source element,
 * moves by a given offset, then releases the mouse.
 */
final class DragAndDropByActionSupplier extends InteractiveAction {

    @StepParameter(value = "X offset")
    final int x;
    @StepParameter(value = "Y offset")
    final int y;
    @StepParameter("Element to drag & drop")
    private final Object e;

    DragAndDropByActionSupplier(Object e, int x, int y) {
        super("Drag and drop");
        checkNotNull(e);
        this.e = e;
        this.x = x;
        this.y = y;
    }

    @Override
    void addAction(Actions value) {
        value.moveToElement(getElement(e), x, y);
    }
}
