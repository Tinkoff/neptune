package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import org.openqa.selenium.Alert;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;

public abstract class AlertActionSupplier extends SequentialActionSupplier<SeleniumSteps, Alert, AlertActionSupplier> {

    private AlertActionSupplier(String description) {
        super(description);
    }

    /**
     * Builds the dismiss action on some alert that is supposed to be appeared.
     *
     * @param supplier is which alert should appear
     * @return built dismiss action
     */
    public static AlertActionSupplier dismiss(GetAlertSupplier supplier) {
        return new AlertActionSupplier("Dismiss alert") {
            @Override
            protected void performActionOn(Alert value) {
                value.dismiss();
            }
        }.performOn(supplier);
    }

    /**
     * Builds the dismiss action on some alert that is appeared.
     *
     * @param alert is the alert which is appeared previously
     * @return built dismiss action
     */
    public static AlertActionSupplier dismiss(Alert alert) {
        checkArgument(nonNull(alert), "The alert which appeared previously should not be null value");
        return new AlertActionSupplier("Dismiss alert") {
            @Override
            protected void performActionOn(Alert value) {
                value.dismiss();
            }
        }.performOn(alert);
    }

    /**
     * Builds the accept action on some alert that is supposed to be appeared.
     *
     * @param supplier is which alert should appear
     * @return built accept action
     */
    public static AlertActionSupplier accept(GetAlertSupplier supplier) {
        return new AlertActionSupplier("Accept alert") {
            @Override
            protected void performActionOn(Alert value) {
                value.accept();
            }
        }.performOn(supplier);
    }

    /**
     * Builds the accept action on some alert that is appeared.
     *
     * @param alert is the alert which is appeared previously
     * @return built accept action
     */
    public static AlertActionSupplier accept(Alert alert) {
        checkArgument(nonNull(alert), "The alert which appeared previously should not be null value");
        return new AlertActionSupplier("Accept alert") {
            @Override
            protected void performActionOn(Alert value) {
                value.accept();
            }
        }.performOn(alert);
    }

    /**
     * Builds the send keys action on some alert that is supposed to be appeared.
     *
     * @param supplier is which alert should appear
     * @param keysToSend string to be sent to the alert
     *
     * @return built send keys action
     */
    public static AlertActionSupplier sendKeys(GetAlertSupplier supplier, String keysToSend) {
        checkArgument(nonNull(keysToSend), "Keys to send value should not be null");
        return new AlertActionSupplier(format("Send keys %s to alert", keysToSend)) {
            @Override
            protected void performActionOn(Alert value) {
                value.sendKeys(keysToSend);
            }
        }.performOn(supplier);
    }

    /**
     * Builds the send keys action on some alert that is appeared.
     *
     * @param alert is the alert which is appeared previously
     * @param keysToSend string to be sent to the alert
     *
     * @return built send keys action
     */
    public static AlertActionSupplier sendKeys(Alert alert, String keysToSend) {
        checkArgument(nonNull(alert), "The alert which was taken previously should not be null value");
        checkArgument(nonNull(keysToSend), "Keys to send value should not be null");
        return new AlertActionSupplier(format("Send keys %s to alert", keysToSend)) {
            @Override
            protected void performActionOn(Alert value) {
                value.sendKeys(keysToSend);
            }
        }.performOn(alert);
    }
}
