package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the double clicking.
 */
@Description("Double click")
abstract class DoubleClickActionSupplier extends InteractiveAction {

    DoubleClickActionSupplier() {
        super();
    }

    @MaxDepthOfReporting(0)
    static final class DoubleClickSimpleActionSupplier extends DoubleClickActionSupplier {
        @Override
        void addAction(Actions value) {
            value.doubleClick();
        }
    }

    @MaxDepthOfReporting(0)
    @Description("Double click {target}")
    static final class DoubleClickOnElementActionSupplier extends DoubleClickActionSupplier {

        @DescriptionFragment("target")
        private final Object e;

        DoubleClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.doubleClick(getElement(e));
        }
    }
}
