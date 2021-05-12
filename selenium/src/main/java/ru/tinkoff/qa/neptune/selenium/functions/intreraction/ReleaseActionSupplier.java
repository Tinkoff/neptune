package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that releases the pressed left mouse button.
 */
@Description("Release left mouse button")
abstract class ReleaseActionSupplier extends InteractiveAction {

    ReleaseActionSupplier() {
        super();
    }

    @MaxDepthOfReporting(0)
    static final class ReleaseSimpleActionSupplier extends ReleaseActionSupplier {

        @Override
        void addAction(Actions value) {
            value.release();
        }
    }

    @Description("Release left mouse button at {target}")
    @MaxDepthOfReporting(0)
    static final class ReleaseElementActionSupplier extends ReleaseActionSupplier {

        @DescriptionFragment("target")
        private final Object e;

        ReleaseElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.release(getElement(e));
        }
    }
}
