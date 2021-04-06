package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs click-and-hold at the location of the source element,
 * moves by a given offset, then releases the mouse.
 */
@Description("Drag {source} and drop at [x={x}, y={y}] from current position")
final class DragAndDropByActionSupplier extends InteractiveAction {

    @DescriptionFragment("x")
    final int x;
    @DescriptionFragment("y")
    final int y;
    @DescriptionFragment("source")
    private final Object e;

    DragAndDropByActionSupplier(Object e, int x, int y) {
        super();
        checkNotNull(e);
        this.e = e;
        this.x = x;
        this.y = y;
    }

    @Override
    void addAction(Actions value) {
        value.dragAndDropBy(getElement(e), x, y);
    }
}
