package ru.tinkoff.qa.neptune.selenium.functions.click;

import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialActionSupplier.DefaultParameterNames(
        performOn = "Element to click"
)
public final class ClickActionSupplier extends SequentialActionSupplier<SeleniumStepContext, SearchContext, ClickActionSupplier> {

    private static final String DESCRIPTION = "Click on %s";

    private ClickActionSupplier(String description) {
        super(description);
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
        return new ClickActionSupplier(format(DESCRIPTION, on))
                .performOn(on.get().compose(currentContent()));
    }

    /**
     * Builds the click action on some web element
     *
     * @param on is how to find the web element
     * @return built click action
     */
    public static ClickActionSupplier on(SequentialGetStepSupplier<SearchContext, WebElement, ?, ?, ?> on) {
        checkNotNull(on);
        return new ClickActionSupplier(format(DESCRIPTION, on))
                .performOn(on.get().compose(currentContent()));
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
        return new ClickActionSupplier(format(DESCRIPTION, on)).performOn(on);
    }

    /**
     * Builds the click action on some web element
     *
     * @param on is the target web element
     * @return built click action
     */
    public static ClickActionSupplier on(WebElement on) {
        checkNotNull(on);
        return new ClickActionSupplier(format(DESCRIPTION, on))
                .performOn(on);
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
