package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

/**
 * Builds an action that moves the mouse from the current position (or 0,0) by the given offset.
 */
@Description("Move mouse to [x={x}, y={y}] from current position")
final class MouseMoveActionSupplier extends InteractiveAction {

    @DescriptionFragment("x")
    final int x;
    @DescriptionFragment("y")
    final int y;

    MouseMoveActionSupplier(int x, int y) {
        super();
        this.x = x;
        this.y = y;
    }

    @Override
    void addAction(Actions value) {
        value.moveByOffset(x, y);
    }
}
