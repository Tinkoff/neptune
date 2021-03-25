package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

@SuppressWarnings("unchecked")
public final class SwitchActionSupplier extends SequentialActionSupplier<SeleniumStepContext, Object, SwitchActionSupplier> {

    private SwitchActionSupplier() {
        super();
    }

    /**
     * Builds the action which performs the switching to some target locator: window, alert, frame etc.
     *
     * @param to is how to get some target locator
     * @return built `switch to` action
     */
    @Description("Switch to {to}")
    public static SwitchActionSupplier to(@DescriptionFragment("to") TargetLocatorSupplier to) {
        return new SwitchActionSupplier().performOn((SequentialGetStepSupplier<SeleniumStepContext, Object, ?, ?, ?>) to);
    }

    /**
     * Builds the action which performs the switching to some target locator: window or frame.
     *
     * @param to is the target locator to be switched
     * @return built `switch to` action
     */
    @Description("Switch to {to}")
    public static SwitchActionSupplier to(@DescriptionFragment("to") SwitchesToItself to) {
        return new SwitchActionSupplier().performOn(to);
    }

    @Override
    protected void performActionOn(Object value) {
        if (SwitchesToItself.class.isAssignableFrom(value.getClass())) {
            ((SwitchesToItself) value).switchToMe();
        }
    }
}
