package com.github.toy.constructor.selenium.functions.target.locator.alert;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.Alert;

import static com.google.common.base.Preconditions.checkArgument;

public abstract class AlertActionSupplier extends SequentialActionSupplier<SeleniumSteps, Alert, AlertActionSupplier> {

    /**
     * Builds the dismiss action on some alert that is supposed to be appeared.
     *
     * @param supplier is which alert should appear
     * @return built dismiss action
     */
    public static AlertActionSupplier dismiss(GetAlertSupplier supplier) {
        return new AlertActionSupplier() {
            @Override
            protected void performActionOn(Alert value, Object... ignored) {
                value.dismiss();
            }
        }.andThen("Dismiss", supplier.get());
    }

    /**
     * Builds the dismiss action on some alert that is appeared.
     *
     * @param alert is the alert which is appeared previously
     * @return built dismiss action
     */
    public static AlertActionSupplier dismiss(Alert alert) {
        checkArgument(alert != null, "The alert which appeared previously should not be null value");
        return new AlertActionSupplier() {
            @Override
            protected void performActionOn(Alert value, Object... ignored) {
                value.dismiss();
            }
        }.andThen("Dismiss", alert);
    }

    /**
     * Builds the accept action on some alert that is supposed to be appeared.
     *
     * @param supplier is which alert should appear
     * @return built accept action
     */
    public static AlertActionSupplier accept(GetAlertSupplier supplier) {
        return new AlertActionSupplier() {
            @Override
            protected void performActionOn(Alert value, Object... ignored) {
                value.accept();
            }
        }.andThen("Accept", supplier.get());
    }

    /**
     * Builds the accept action on some alert that is appeared.
     *
     * @param alert is the alert which is appeared previously
     * @return built accept action
     */
    public static AlertActionSupplier accept(Alert alert) {
        checkArgument(alert != null, "The alert which appeared previously should not be null value");
        return new AlertActionSupplier() {
            @Override
            protected void performActionOn(Alert value, Object... ignored) {
                value.accept();
            }
        }.andThen("Accept", alert);
    }

    /**
     * Builds the send keys action on some alert that is supposed to be appeared.
     *
     * @param supplier is which alert should appear
     * @return built send keys action
     */
    public static AlertActionSupplier sendKeys(GetAlertSupplier supplier, String keysToSend) {
        checkArgument(keysToSend != null, "Keys to send value should not be null");
        return new AlertActionSupplier() {
            @Override
            protected void performActionOn(Alert value, Object... keys) {
                value.sendKeys(keys[0].toString());
            }
        }.andThen("Send keys", supplier.get(), keysToSend);
    }

    /**
     * Builds the send keys action on some alert that is appeared.
     *
     * @param alert is the alert which is appeared previously
     * @return built send keys action
     */
    public static AlertActionSupplier sendKeys(Alert alert, String keysToSend) {
        checkArgument(alert != null, "The alert which was taken previously should not be null value");
        checkArgument(keysToSend != null, "Keys to send value should not be null");
        return new AlertActionSupplier() {
            @Override
            protected void performActionOn(Alert value, Object... keys) {
                value.sendKeys(keys[0].toString());
            }
        }.andThen("Send keys", alert, keysToSend);
    }
}
