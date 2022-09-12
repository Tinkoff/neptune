package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

/**
 * This class is designed to build a dismiss-action. This action is performed on a browser alert.
 */
@Description("Dismiss alert")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class DismissAlertActionSupplier extends SequentialActionSupplier<SeleniumStepContext, Alert, DismissAlertActionSupplier> {

    private DismissAlertActionSupplier() {
        super();
    }

    /**
     * Builds an dismiss-action. This action is performed on a browser alert.
     *
     * @param getAlert is description of an alert to dismiss
     * @return an instance of {@link DismissAlertActionSupplier}
     */
    public static DismissAlertActionSupplier dismissAlert(GetAlertSupplier getAlert) {
        return new DismissAlertActionSupplier().performOn(getAlert);
    }

    /**
     * Builds an dismiss-action. This action is performed on a browser alert.
     *
     * @param alert is an alert to dismiss
     * @return an instance of {@link DismissAlertActionSupplier}
     */
    public static DismissAlertActionSupplier dismissAlert(Alert alert) {
        return new DismissAlertActionSupplier().performOn(alert);
    }

    @Override
    protected void howToPerform(Alert value) {
        value.dismiss();
    }
}
