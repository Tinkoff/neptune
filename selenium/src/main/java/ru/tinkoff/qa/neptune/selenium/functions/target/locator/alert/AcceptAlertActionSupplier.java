package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

/**
 * This class is designed to build an accept-action. This action is performed on a browser alert.
 */
@Description("Accept alert")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class AcceptAlertActionSupplier extends SequentialActionSupplier<SeleniumStepContext, Alert, AcceptAlertActionSupplier> {

    private AcceptAlertActionSupplier() {
        super();
    }

    /**
     * Builds an accept-action. This action is performed on a browser alert.
     *
     * @param getAlert is description of an alert to accept
     * @return an instance of {@link AcceptAlertActionSupplier}
     */
    public static AcceptAlertActionSupplier acceptAlert(GetAlertSupplier getAlert) {
        return new AcceptAlertActionSupplier().performOn(getAlert);
    }

    /**
     * Builds an accept-action. This action is performed on a browser alert.
     *
     * @param alert is an alert to accept
     * @return an instance of {@link AcceptAlertActionSupplier}
     */
    public static AcceptAlertActionSupplier acceptAlert(Alert alert) {
        return new AcceptAlertActionSupplier().performOn(alert);
    }

    @Override
    protected void howToPerform(Alert value) {
        value.accept();
    }
}
