package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;

import static java.lang.String.format;

@SuppressWarnings("unchecked")
public final class SwitchActionSupplier extends SequentialActionSupplier<SeleniumSteps, Object, SwitchActionSupplier> {

    private static final String DESCRIPTION = "Switch to %s";

    private SwitchActionSupplier(String description) {
        super(description);
    }

    /**
     * Builds the action which performs the switching to some target locator: window, alert, frame etc.
     *
     * @param to is how to get some target locator
     * @return built `switch to` action
     */
    public static SwitchActionSupplier to(TargetLocatorSupplier<?> to) {
        return new SwitchActionSupplier(format(DESCRIPTION, to)).performOn((GetStepSupplier) to);
    }

    /**
     * Builds the action which performs the switching to some target locator: window or frame.
     *
     * @param to is the target locator to be switched
     * @return built `switch to` action
     */
    public static SwitchActionSupplier to(SwitchesToItself to) {
        return new SwitchActionSupplier(format(DESCRIPTION, to)).performOn(to);
    }

    @Override
    protected void performActionOn(Object value) {
        if (SwitchesToItself.class.isAssignableFrom(value.getClass())) {
            ((SwitchesToItself) value).switchToMe();
        }
    }
}
