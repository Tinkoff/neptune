package ru.tinkoff.qa.neptune.selenium.functions.click;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepPerformer;
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;

@MakeImageCapturesOnFinishing
public final class ClickActionSupplier extends SequentialActionSupplier<SeleniumStepPerformer, Clickable, ClickActionSupplier> {

    private static final String DESCRIPTION = "Click on %s";

    private static Function<WebElement, ClickableWrapper> GET_CLICKABLE_WRAPPER =
            ClickActionSupplier::getClickableFromElement;

    private ClickActionSupplier(String description) {
        super(description);
    }

    private static ClickableWrapper getClickableFromElement(WebElement element) {
        return new ClickableWrapper() {
            @Override
            public void click() {
                element.click();
            }

            @Override
            public List<WebElement> findElements(By by) {
                return element.findElements(by);
            }

            @Override
            public WebElement findElement(By by) {
                return element.findElement(by);
            }

            @Override
            public String toString() {
                return element.toString();
            }
        };
    }

    /**
     * Builds the click action on some clickable element
     * @param on is how to find the clickable element
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
     * @param on is how to find the web element
     * @return built click action
     */
    public static ClickActionSupplier on(GetStepSupplier<SearchContext, WebElement, ?> on) {
        checkNotNull(on);
        return new ClickActionSupplier(format(DESCRIPTION, on))
                .performOn(GET_CLICKABLE_WRAPPER.compose(on.get().compose(currentContent())));
    }

    /**
     * Builds the click action on some clickable element
     * @param on is the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public static <R extends SearchContext & Clickable> ClickActionSupplier on(R on) {
        checkNotNull(on);
        return new ClickActionSupplier(format(DESCRIPTION, on)).performOn(on);
    }

    /**
     * Builds the click action on some web element
     * @param on is the target web element
     * @return built click action
     */
    public static ClickActionSupplier on(WebElement on) {
        checkNotNull(on);
        return new ClickActionSupplier(format(DESCRIPTION, on))
                .performOn(getClickableFromElement(on));
    }

    /**
     * Adds the click action on some another clickable element
     * @param on is how to find the clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(SearchSupplier<R> on) {
        return mergeActionSequenceFrom(on(on));
    }

    /**
     * Adds the click action on some another web element
     * @param on is how to find the web element
     * @return built click action
     */
    public ClickActionSupplier andOn(GetStepSupplier<SearchContext, WebElement, ?> on) {
        return mergeActionSequenceFrom(on(on));
    }

    /**
     * Adds the click action on some another clickable element
     * @param on s the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(R on) {
        return mergeActionSequenceFrom(on(on));
    }

    /**
     * Adds the click action on some another web element
     * @param on s the target web element
     * @return built click action
     */
    public ClickActionSupplier andOn(WebElement on) {
        return mergeActionSequenceFrom(on(on));
    }

    @Override
    protected void performActionOn(Clickable value) {
        value.click();
    }

    private interface ClickableWrapper extends SearchContext, Clickable {

    }
}
