package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.StepParameter;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs click-and-hold at the location of the source element,
 * moves to the location of the target element, then releases the mouse.
 */
final class DragAndDropActionSupplier extends InteractiveAction {

    @StepParameter("Element to drag & drop")
    private final Object source;

    @StepParameter("Target element")
    private final Object target;


    DragAndDropActionSupplier(Object source, Object target) {
        super("Drag and drop");
        checkNotNull(source);
        this.source = source;
        checkNotNull(target);
        this.target = target;
    }

    @Override
    void addAction(Actions value) {
        var s = getElement(source);
        var t = getElement(target);
        value.dragAndDrop(s, t);
    }
}
