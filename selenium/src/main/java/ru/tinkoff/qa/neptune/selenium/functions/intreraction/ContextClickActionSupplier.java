package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the context clicking.
 */
@Description("Context click")
abstract class ContextClickActionSupplier extends InteractiveAction {

    ContextClickActionSupplier() {
        super();
    }

    @MaxDepthOfReporting(0)
    static final class ContextClickSimpleActionSupplier extends ContextClickActionSupplier {
        @Override
        void addAction(Actions value) {
            value.contextClick();
        }
    }

    @Description("Context click on {target}")
    @MaxDepthOfReporting(0)
    static final class ContextClickOnElementActionSupplier extends ContextClickActionSupplier {

        @DescriptionFragment("target")
        private final Object e;

        ContextClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.contextClick(getElement(e));
        }
    }
}
