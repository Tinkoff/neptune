package com.github.toy.constructor.selenium.functions.alert;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;

import javax.annotation.Nullable;
import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSingleOnCondition;
import static com.github.toy.constructor.selenium.functions.alert.GetAlert.getAlert;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.WAITING_ALERT_TIME_DURATION;
import static com.google.common.base.Preconditions.checkArgument;

public final class GetAlertSupplier extends GetSupplier<SeleniumSteps, Alert, GetAlertSupplier> {

    private static final Predicate<Alert> ALERT_AS_IS = condition("as is", alert -> true);

    private GetAlertSupplier() {
        super();
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it.
     *
     * @param criteria used to check the found alert
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result. This value may by {@code null}
     * @param supplier which returns the exception to be thrown when alert that suits criteria is not found
     *                 and time is expired. This value may be {@code null} so exception is not thrown and
     *                 result is {@code null}
     * @return a function which waits for alert, checks the result by criteria and returns it.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Duration duration,
                                         @Nullable Duration sleeping,
                                         @Nullable Supplier<? extends NoAlertPresentException> supplier) {
        checkArgument(criteria != null, "Criteria to check an alert should be defined");
        checkArgument(duration != null, "Duration of the waiting for an alert should be defined");
        return new GetAlertSupplier().set(getSingleOnCondition("Alert", getAlert(), criteria,
                duration, sleeping, true, supplier));
    }

    /**
     * This method builds a function which waits for any alert.
     *
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @param supplier which returns the exception to be thrown when alert that suits criteria is not found
     *                 and time is expired.
     * @return a function which waits for any alert and returns it.
     */
    public static GetAlertSupplier alert(Duration duration,
                                         Duration sleeping,
                                         Supplier<? extends NoAlertPresentException> supplier) {
        checkArgument(sleeping != null, "Duration of the sleeping between attempts to get alert " +
                "should be defined here");
        checkArgument(supplier != null, "The supplier of the NoAlertPresentException should be " +
                "defined here");
        return alert(ALERT_AS_IS, duration, sleeping, supplier);
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it. When time
     * is expired then the function should return {@null}.
     *
     * @param criteria used to check the found alert
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @return a function which waits for alert, checks the result by criteria and returns it or returns {@null} when
     * alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Duration duration,
                                         Duration sleeping) {
        checkArgument(sleeping != null, "Duration of the sleeping between attempts to get alert " +
                "should be defined here");
        return alert(criteria, duration, sleeping, null);
    }

    /**
     * This method builds a function which waits for any alert. When time is expired then the function should
     * return {@null}.
     *
     * @param duration of the waiting for alert
     * @param sleeping time for the sleeping between attempts to get the desired result.
     * @return a function which waits for any alert and returns it or returns {@null} when
     * alert is not appear.
     */
    public static GetAlertSupplier alert(Duration duration,
                                         Duration sleeping) {
        checkArgument(sleeping != null, "Duration of the sleeping between attempts to get alert " +
                "should be defined here");
        return alert(ALERT_AS_IS, duration, sleeping, null);
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it. About time of
     * the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @param criteria used to check the found alert
     * @param supplier which returns the exception to be thrown when alert that suits criteria is not found
     *                 and time is expired.
     * @return a function which waits for alert, checks the result by criteria and returns it.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Supplier<? extends NoAlertPresentException> supplier) {
        checkArgument(supplier != null, "The supplier of the NoAlertPresentException should be " +
                "defined here");
        return alert(criteria, WAITING_ALERT_TIME_DURATION.get(), null, supplier);
    }

    /**
     * This method builds a function which waits for any alert. About time of the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @param supplier which returns the exception to be thrown when alert is not found and time is expired.
     * @return a function which waits for any alert and returns it.
     */
    public static GetAlertSupplier alert(Supplier<? extends NoAlertPresentException> supplier) {
        checkArgument(supplier != null, "The supplier of the NoAlertPresentException should be " +
                "defined here");
        return alert(ALERT_AS_IS,
                WAITING_ALERT_TIME_DURATION.get(), null, supplier);
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it. When time
     * is expired then the function should return {@null}.
     *
     * @param criteria used to check the found alert
     * @param duration of the waiting for alert
     * @return a function which waits for alert, checks the result by criteria and returns it or returns {@null} when
     * alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria,
                                         Duration duration) {
        return alert(criteria, duration, null, null);
    }

    /**
     * This method builds a function which waits for any alert.
     *
     * @param duration of the waiting for alert
     * @return a function which waits for any alert and returns it.
     */
    public static GetAlertSupplier alert(Duration duration) {
        return alert(ALERT_AS_IS, duration, null, null);
    }

    /**
     * This method builds a function which waits for alert, checks the result by criteria and returns it. When time
     * is expired then the function should return {@null}. About time of
     * the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @param criteria used to check the found alert
     * @return a function which waits for alert, checks the result by criteria and returns it or returns {@null} when
     * alert is not appear or it doesn't suit the criteria.
     */
    public static GetAlertSupplier alert(Predicate<Alert> criteria) {
        return alert(criteria, WAITING_ALERT_TIME_DURATION.get(), null, null);
    }

    /**
     * This method builds a function which waits for alert any alert. When time
     * is expired then the function should return {@null}. About time of
     * the waiting for alert
     * @see com.github.toy.constructor.selenium.properties.WaitingProperties#WAITING_ALERT_TIME_DURATION
     *
     * @return a function which waits for any alert and returns it or returns {@null} when
     * alert is not appear.
     */
    public static GetAlertSupplier alert() {
        return alert(ALERT_AS_IS,
                WAITING_ALERT_TIME_DURATION.get(), null, null);
    }
}
