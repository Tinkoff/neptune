package ru.tinkoff.qa.neptune.selenium.functions.click;

import ru.tinkoff.qa.neptune.core.api.GetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;

import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.selenium.CurrentContentFunction.currentContent;
import static com.google.common.base.Preconditions.checkArgument;

public final class ClickActionSupplier extends SequentialActionSupplier<SeleniumSteps, Clickable, ClickActionSupplier> {

    private ClickActionSupplier() {
        super();
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
        return new ClickActionSupplier().andOn(on);
    }

    /**
     * Builds the click action on some web element
     * @param on is how to find the web element
     * @return built click action
     */
    public static ClickActionSupplier on(GetStepSupplier<SearchContext, WebElement, ?> on) {
        return new ClickActionSupplier().andOn(on);
    }

    /**
     * Builds the click action on some clickable element
     * @param on is the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public static <R extends SearchContext & Clickable> ClickActionSupplier on(R on) {
        return new ClickActionSupplier().andOn(on);
    }

    /**
     * Builds the click action on some web element
     * @param on is the target web element
     * @return built click action
     */
    public static ClickActionSupplier on(WebElement on) {
        return new ClickActionSupplier().andOn(on);
    }

    /**
     * Adds the click action on some another clickable element
     * @param on is how to find the clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(SearchSupplier<R> on) {
        checkArgument(on != null, "The searching for the clickable element should be defined");
        return andThen("Click", on.get().compose(currentContent()));
    }

    /**
     * Adds the click action on some another web element
     * @param on is how to find the web element
     * @return built click action
     */
    public ClickActionSupplier andOn(GetStepSupplier<SearchContext, WebElement, ?> on) {
        checkArgument(on != null, "The searching for the clickable element should be defined");
        return andThen("Click", currentContent()
                .andThen(toGet(on.toString(), webDriver -> getClickableFromElement(on.get().apply(webDriver)))));
    }

    /**
     * Adds the click action on some another clickable element
     * @param on s the target clickable element
     * @param <R> is the type of the clickable element
     * @return built click action
     */
    public <R extends SearchContext & Clickable> ClickActionSupplier andOn(R on) {
        checkArgument(on != null, "The clickable element should be defined");
        return andThen("Click", on);
    }

    /**
     * Adds the click action on some another web element
     * @param on s the target web element
     * @return built click action
     */
    public ClickActionSupplier andOn(WebElement on) {
        checkArgument(on != null, "The element should be defined");
        return andThen("Click", getClickableFromElement(on));
    }

    @Override
    protected void performActionOn(Clickable value, Object... ignored) {
        value.click();
    }

    private interface ClickableWrapper extends SearchContext, Clickable {

    }
}
