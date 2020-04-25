package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.time.Duration;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.time.DurationFormatUtils.formatDurationHMS;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_ALERT_TIME_DURATION;

public final class GetAlertSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, Alert, WebDriver, GetAlertSupplier>
        implements TargetLocatorSupplier<Alert> {

    private GetAlertSupplier() {
        super("Alert", webDriver -> webDriver.switchTo().alert());
        throwOnEmptyResult(noSuchAlert());
        timeOut(WAITING_ALERT_TIME_DURATION.get());
    }

    private Supplier<NoAlertPresentException> noSuchAlert() {
        return () -> {
            String description = ofNullable(getCriteria())
                    .map(Criteria::toString)
                    .orElse(EMPTY);
            if (!isBlank(description)) {
                return new NoAlertPresentException(format("No alert that suits criteria '%s' has been found", description));
            }
            return new NoAlertPresentException("No alert has been found");
        };
    }

    protected Map<String, String> formTimeoutForReport(Duration timeOut) {
        var result = new LinkedHashMap<String, String>();
        result.put("Time of the waiting for the alert", formatDurationHMS(timeOut.toMillis()));
        return result;
    }

    @Override
    public GetAlertSupplier criteria(Criteria<? super Alert> condition) {
        return super.criteria(condition);
    }

    @Override
    public GetAlertSupplier criteria(String description, Predicate<? super Alert> condition) {
        return super.criteria(description, condition);
    }

    @Override
    public GetAlertSupplier timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public GetAlertSupplier pollingInterval(Duration pollingTime) {
        return super.pollingInterval(pollingTime);
    }

    /**
     * This method builds a function which waits for alert any alert.
     *
     * @return a function which waits for any alert and returns it or throws {@link NoAlertPresentException}
     * when alert is not appear.
     */
    public static GetAlertSupplier alert() {
        return new GetAlertSupplier().from(currentContent());
    }
}
