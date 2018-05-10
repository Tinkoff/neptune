package com.github.toy.constructor.selenium.functions.target.locator.alert;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.TargetLocatorSupplier;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.ToGetSingleCheckedObject.getSingle;
import static com.github.toy.constructor.selenium.functions.target.locator.alert.GetAlert.getAlert;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.WAITING_ALERT_TIME_DURATION;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class GetAlertSupplier extends GetSupplier<SeleniumSteps, Alert, GetAlertSupplier>
        implements TargetLocatorSupplier<Alert> {

    private static Supplier<NoAlertPresentException> noSuchAlert(Predicate<Alert> predicate) {
        return ofNullable(predicate)
                .map(condition ->
                        (Supplier<NoAlertPresentException>) () ->
                                new NoAlertPresentException(
                                        format("No alert which suits criteria '%s' has been found", condition)))
                .orElseGet(() -> () -> new NoAlertPresentException("No alert has been found"));
    }

    private GetAlertSupplier() {
        super();
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it.
     *
     * @param criteria used to check the found alert
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @param supplier which returns the exception to be thrown when alert that suits criteria is not found
     *                 and time is expired.
     * @return a function which waits for alert, checks the result by criteria and returns it or throws
     * {@link NoAlertPresentException} when alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Duration duration,
                                         Duration sleeping,
                                         Supplier<? extends NoAlertPresentException> supplier) {
        return new GetAlertSupplier().set(getSingle(getAlert(), criteria, duration, sleeping,
                true, supplier));
    }

    /**
     * This method builds a function which waits for any alert.
     *
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @param supplier which returns the exception to be thrown when alert that suits criteria is not found
     *                 and time is expired.
     * @return a function which waits for any alert and returns it or throws {@link NoAlertPresentException} when alert
     * is not appear.
     */
    public static GetAlertSupplier alert(Duration duration,
                                         Duration sleeping,
                                         Supplier<? extends NoAlertPresentException> supplier) {
        return new GetAlertSupplier().set(getSingle(getAlert(), duration, sleeping, supplier));
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it.
     *
     * @param criteria used to check the found alert
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @return a function which waits for alert, checks the result by criteria and returns it or throws
     * {@link NoAlertPresentException} when alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Duration duration,
                                         Duration sleeping) {
        return new GetAlertSupplier().set(getSingle(getAlert(), criteria, duration, sleeping,
                true, noSuchAlert(criteria)));
    }

    /**
     * This method builds a function which waits for any alert.
     *
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @return a function which waits for any alert and returns it or throws {@link NoAlertPresentException}
     * when alert is not appear.
     */
    public static GetAlertSupplier alert(Duration duration,
                                         Duration sleeping) {
        return new GetAlertSupplier().set(getSingle(getAlert(), duration, sleeping, noSuchAlert(null)));
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it. About time of
     * the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @param criteria used to check the found alert
     * @param supplier which returns the exception to be thrown when alert that suits criteria is not found
     *                 and time is expired.
     * @return a function which waits for alert, checks the result by criteria and returns it or throws
     * {@link NoAlertPresentException} when alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Supplier<? extends NoAlertPresentException> supplier) {
        return new GetAlertSupplier()
                .set(getSingle(getAlert(), criteria, WAITING_ALERT_TIME_DURATION.get(), true, supplier));
    }

    /**
     * This method builds a function which waits for any alert. About time of the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @param supplier which returns the exception to be thrown when alert is not found and time is expired.
     * @return a function which waits for any alert and returns it or throws {@link NoAlertPresentException}
     * when alert is not appear.
     */
    public static GetAlertSupplier alert(Supplier<? extends NoAlertPresentException> supplier) {
        return new GetAlertSupplier().set(getSingle(getAlert(), WAITING_ALERT_TIME_DURATION.get(), supplier));
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it.
     *
     * @param criteria used to check the found alert
     * @param duration of the waiting for alert
     * @return a function which waits for alert, checks the result by criteria and returns it or throws
     * {@link NoAlertPresentException} when alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Duration duration) {
        return new GetAlertSupplier().set(getSingle(getAlert(), criteria, duration,
                true, noSuchAlert(criteria)));
    }

    /**
     * This method builds a function which waits for any alert.
     *
     * @param duration of the waiting for alert
     * @return a function which waits for any alert and returns it or throws {@link NoAlertPresentException}
     * when alert is not appear.
     */
    public static GetAlertSupplier alert(Duration duration) {
        return new GetAlertSupplier().set(getSingle(getAlert(), duration, noSuchAlert(null)));
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it. About time of
     * the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @param criteria used to check the found alert
     * @return a function which waits for alert, checks the result by criteria and returns it or throws
     * {@link NoAlertPresentException} when alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria) {
        return new GetAlertSupplier().set(getSingle(getAlert(), criteria, WAITING_ALERT_TIME_DURATION.get(),
                true, noSuchAlert(criteria)));
    }

    /**
     * This method builds a function which waits for alert any alert. About time of
     * the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @return a function which waits for any alert and returns it or throws {@link NoAlertPresentException}
     * when alert is not appear.
     */
    public static GetAlertSupplier alert() {
        return new GetAlertSupplier().set(getSingle(getAlert(),  WAITING_ALERT_TIME_DURATION.get(),
                noSuchAlert(null)));
    }
}
