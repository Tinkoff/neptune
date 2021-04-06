package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs click-and-hold at the location of the source element,
 * moves to the location of the target element, then releases the mouse.
 */
@Description("Drag {source} and drop at {destination}")
final class DragAndDropActionSupplier extends InteractiveAction {

    @DescriptionFragment("source")
    private final Object source;

    @DescriptionFragment("destination")
    private final Object target;


    DragAndDropActionSupplier(Object source, Object target) {
        super();
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
