package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that moves the mouse to an offset from the top-left corner of a web element.
 */
@MaxDepthOfReporting(0)
final class MoveToElementActionSupplier extends InteractiveAction {

    private final Object e;

    final Integer x;

    final Integer y;

    MoveToElementActionSupplier(Object e, Integer x, Integer y) {
        super();
        this.x = x;
        this.y = y;
        checkNotNull(e);
        this.e = e;
    }

    @Override
    void addAction(Actions value) {
        if (x == null || y == null) {
            value.moveToElement(getElement(e));
        } else {
            value.moveToElement(getElement(e), x, y);
        }
    }
}
