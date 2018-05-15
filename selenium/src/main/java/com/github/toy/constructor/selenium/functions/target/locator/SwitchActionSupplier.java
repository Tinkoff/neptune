package com.github.toy.constructor.selenium.functions.target.locator;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;

public final class SwitchActionSupplier extends SequentialActionSupplier<SeleniumSteps, Object, SwitchActionSupplier> {

    private SwitchActionSupplier() {
        super();
    }

    /**
     * Builds the action which performs the switching to some target locator: window, alert, frame etc.
     *
     * @param to is how to get some target locator
     * @return built `switch to` action
     */
    public static SwitchActionSupplier to(TargetLocatorSupplier<?> to) {
        return new SwitchActionSupplier().andThenSwitchTo(to);
    }

    /**
     * Builds the action which performs the switching to some target locator: window or frame.
     *
     * @param to is the target locator to be switched
     * @return built `switch to` action
     */
    public static SwitchActionSupplier to(SwitchesToItself to) {
        return new SwitchActionSupplier().andThenSwitchTo(to);
    }

    /**
     * Adds the next action which performs the switching to some target locator: window, alert, frame etc.
     *
     * @param to is how to get some target locator
     * @return built `switch to` action
     */
    public SwitchActionSupplier andThenSwitchTo(TargetLocatorSupplier<?> to) {
        return andThen("Switch to", to);
    }

    /**
     * Adds the next action which performs the switching to some target locator: window or frame.
     *
     * @param to is the target locator to be switched
     * @return built `switch to` action
     */
    public SwitchActionSupplier andThenSwitchTo(SwitchesToItself to) {
        return andThen("Switch to", to);
    }

    @Override
    protected void performActionOn(Object value, Object... additionalArgument) {
        if (SwitchesToItself.class.isAssignableFrom(value.getClass())) {
            SwitchesToItself.class.cast(value).switchToMe();
        }
    }
}
