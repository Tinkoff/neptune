package ru.tinkoff.qa.neptune.selenium.functions.click;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.IncludeParamsOfInnerGetterStep;
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;

@CaptureOnFailure(by = WebElementImageCaptor.class)
@CaptureOnSuccess(by = WebElementImageCaptor.class)
@Description("Click on {on}")
@MaxDepthOfReporting(0)
@IncludeParamsOfInnerGetterStep
public final class ClickActionSupplier extends SequentialActionSupplier<Object, SearchContext, ClickActionSupplier> {

    @DescriptionFragment("on")
    final Object on;

    private ClickActionSupplier(Object on) {
        super();
        this.on = on;
    }

    private ClickActionSupplier(SearchContext on) {
        this((Object) on);
        performOn(on);
    }

    private ClickActionSupplier(SequentialGetStepSupplier<Object, ? extends SearchContext, ?, ?, ?> on) {
        this((Object) on);
        performOn(on);
    }

    /**
     * Builds the click action on some clickable element
     *
     * @param on  is how to find the clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public static <R extends SearchContext & Clickable> ClickActionSupplier on(SearchSupplier<R> on) {
        checkNotNull(on);
        return new ClickActionSupplier(on);
    }

    /**
     * Builds the click action on some web element
     *
     * @param on is how to find the web element
     * @return built click action
     */
    public static ClickActionSupplier on(SequentialGetStepSupplier<Object, WebElement, ?, ?, ?> on) {
        checkNotNull(on);
        return new ClickActionSupplier(on);
    }

    /**
     * Builds the click action on some clickable element
     *
     * @param on  is the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public static <R extends SearchContext & Clickable> ClickActionSupplier on(R on) {
        checkNotNull(on);
        return new ClickActionSupplier(on);
    }

    /**
     * Builds the click action on some web element
     *
     * @param on is the target web element
     * @return built click action
     */
    public static ClickActionSupplier on(WebElement on) {
        checkNotNull(on);
        return new ClickActionSupplier(on);
    }

    @Override
    protected void performActionOn(SearchContext value) {
        var cls = value.getClass();
        if (WebElement.class.isAssignableFrom(cls)) {
            ((WebElement) value).click();
        }

        if (Clickable.class.isAssignableFrom(cls)) {
            ((Clickable) value).click();
        }
    }
}
