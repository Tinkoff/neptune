package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;

/**
 * Builds an action that moves the mouse from the current position (or 0,0) by the given offset.
 */
final class MouseMoveActionSupplier extends InteractiveAction {

    final int x;

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
