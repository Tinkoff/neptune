package ru.tinkoff.qa.neptune.selenium.functions.intreraction;

import org.openqa.selenium.interactions.Actions;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;

import static com.google.common.base.Preconditions.checkNotNull;

/**
 * Builds an action that performs the clicking by left mouse button.
 */
@Description("Click by left mouse button")
abstract class ClickActionSupplier extends InteractiveAction {

    ClickActionSupplier() {
        super();
    }

    static final class ClickSimpleActionSupplier extends ClickActionSupplier {

        @Override
        void addAction(Actions value) {
            value.click();
        }
    }

    @Description("Click by left mouse button {target}")
    static final class ClickOnElementActionSupplier extends ClickActionSupplier {

        @DescriptionFragment("target")
        private final Object e;

        ClickOnElementActionSupplier(Object e) {
            super();
            checkNotNull(e);
            this.e = e;
        }

        @Override
        void addAction(Actions value) {
            value.click(getElement(e));
        }
    }
}
