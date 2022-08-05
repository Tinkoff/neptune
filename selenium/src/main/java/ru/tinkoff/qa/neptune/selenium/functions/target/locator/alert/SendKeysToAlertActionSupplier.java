package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.StepParameter;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;

/**
 * This class is designed to build an send keys-action. This action is performed on a browser alert.
 */
@Description("Send keys to the alert")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class SendKeysToAlertActionSupplier extends SequentialActionSupplier<SeleniumStepContext, Alert, SendKeysToAlertActionSupplier> {

    @StepParameter("Keys to send")
    private final String keysToSend;

    private SendKeysToAlertActionSupplier(String keysToSend) {
        super();
        this.keysToSend = keysToSend;
    }

    /**
     * Builds an send keys-action. This action is performed on a browser alert.
     *
     * @param getAlert is description of an alert to send keys
     * @param keys     is string/keys to send
     * @return an instance of {@link SendKeysToAlertActionSupplier}
     */
    public static SendKeysToAlertActionSupplier sendKeysToAlert(GetAlertSupplier getAlert, String keys) {
        return new SendKeysToAlertActionSupplier(keys).performOn(getAlert);
    }

    /**
     * Builds an send keys-action. This action is performed on a browser alert.
     *
     * @param alert is an alert to send keys
     * @param keys  is string/keys to send
     * @return an instance of {@link SendKeysToAlertActionSupplier}
     */
    public static SendKeysToAlertActionSupplier sendKeysToAlert(Alert alert, String keys) {
        return new SendKeysToAlertActionSupplier(keys).performOn(alert);
    }

    @Override
    protected void howToPerform(Alert value) {
        value.sendKeys(keysToSend);
    }
}
