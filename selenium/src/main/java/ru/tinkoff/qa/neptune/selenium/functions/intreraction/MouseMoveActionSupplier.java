package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.StepParameter;

/**
 * Builds an action that moves the mouse from the current position (or 0,0) by the given offset.
 */
public final class MouseMoveActionSupplier extends InteractiveAction {

    @StepParameter(value = "X offset")
    final int x;

    @StepParameter(value = "Y offset")
    final int y;

    MouseMoveActionSupplier(int x, int y) {
        super("Mouse move");
        this.x = x;
        this.y = y;
    }

    @Override
    protected void performActionOn(Actions value) {
        value.moveByOffset(x, y).perform();
    }
}
