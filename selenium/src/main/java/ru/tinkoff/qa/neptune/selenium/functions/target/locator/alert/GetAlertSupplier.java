package ru.tinkoff.qa.neptune.selenium.functions.target.locator.alert;

import org.openqa.selenium.Alert;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.TargetLocatorSupplier;

import java.time.Duration;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static ru.tinkoff.qa.neptune.selenium.SeleniumStepContext.CurrentContentFunction.currentContent;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_ALERT_TIME_DURATION;

@SequentialGetStepSupplier.DefineCriteriaParameterName("Alert criteria")
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for the alert")
@MaxDepthOfReporting(0)
@Description("Alert")
@SequentialGetStepSupplier.DefineGetImperativeParameterName("Wait for:")
public final class GetAlertSupplier extends SequentialGetStepSupplier.GetObjectChainedStepSupplier<SeleniumStepContext, Alert, WebDriver, GetAlertSupplier>
        implements TargetLocatorSupplier<Alert> {

    private GetAlertSupplier() {
        super(webDriver -> new AlertImpl(webDriver.switchTo().alert()));
        throwOnEmptyResult(noSuchAlert());
        timeOut(WAITING_ALERT_TIME_DURATION.get());
    }

    private Supplier<NoAlertPresentException> noSuchAlert() {
        return () -> {
            var description = new StringBuilder("No alert is present");
            getParameters().forEach((key, value) -> description.append("\r\n").append(key).append(":").append(value));
            return new NoAlertPresentException(description.toString());
        };
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
