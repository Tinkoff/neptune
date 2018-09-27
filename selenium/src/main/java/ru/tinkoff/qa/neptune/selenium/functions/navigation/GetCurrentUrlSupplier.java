package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Date;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.StoryWriter.toGet;
import static ru.tinkoff.qa.neptune.core.api.conditions.ToGetSingleCheckedObject.getSingle;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.WAITING_FOR_PAGE_LOADED_DURATION;

public final class GetCurrentUrlSupplier extends SequentialGetStepSupplier<SeleniumSteps, String, Window, GetCurrentUrlSupplier> {

    private final Function<Window, String> GET_URL = Window::getCurrentUrl;
    private final String DESCRIPTION = "URL of the loaded page";

    private final Object from;

    private Predicate<String> condition;
    private Duration waitFor = WAITING_FOR_PAGE_LOADED_DURATION.get();

    private GetCurrentUrlSupplier(Object from) {
        checkArgument(from != null, "It is not defined how to to get current URL, " +
                "from taken window or function that searches for a window");
        this.from = from;
    }

    /**
     * Builds a function which returns url of the page loaded in the first window/tab.
     *
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the current window/tab.
     */
    public static GetCurrentUrlSupplier currentUrl() {
        return new GetCurrentUrlSupplier(window());
    }

    /**
     * Builds a function which returns url of the page loaded in some window/tab. This window/tab is supposed to be found.
     *
     * @param from is how to find the window where loaded url should be taken
     * @return an instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in some window/tab which should be got by criteria.
     */
    public static GetCurrentUrlSupplier currentUrlOf(GetWindowSupplier from) {
        return new GetCurrentUrlSupplier(from);
    }

    /**
     * Builds a function which returns url of the page loaded in the window/tab.
     * @param from is the window where loaded url should be taken
     * @return n instance of {@link GetCurrentUrlSupplier} which wraps a function. This function
     * returns url of the page loaded in the window/tab.
     */
    public static GetCurrentUrlSupplier currentUrlOf(Window from) {
        return new GetCurrentUrlSupplier(from);
    }

    /**
     * When it is expected that current url should be changed/suitable to some criteria then
     * it is possible to define some described condition. It waits for time defined by
     * {@link ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties#WAITING_FOR_PAGE_LOADED_DURATION} or
     * {@link #waitFor(Duration)}. If current url is not matched the criteria after defined time then
     * {@link IllegalStateException} is thrown.
     *
     * @param condition is a criteria than current url should meet.
     * @return self-reference.
     */
    public GetCurrentUrlSupplier onCondition(Predicate<String> condition) {
        this.condition = condition;
        return this;
    }

    /**
     * When it is expected that current url should be changed/suitable to some criteria then
     * it is possible to define some described condition. It waits for time defined by the method.
     * It has sense when some condition is defined by {@link #onCondition(Predicate)}.
     *
     * @param waitFor is time to wait for current url meets some criteria defined by {@link #onCondition(Predicate)}
     * @return self-reference.
     */
    public GetCurrentUrlSupplier waitFor(Duration waitFor) {
        this.waitFor = waitFor;
        return this;
    }

    @Override
    public Function<SeleniumSteps, String> get() {
        if (GetWindowSupplier.class.isAssignableFrom(from.getClass())) {
            super.from((GetWindowSupplier) from);
        }
        else {
            super.from((Window) from);
        }
        return super.get();
    }

    @Override
    protected Function<Window, String> getEndFunction() {
        return ofNullable(condition).map(stringPredicate ->
                getSingle(DESCRIPTION, GET_URL, stringPredicate, waitFor, false,
                        () -> new IllegalStateException(format("Page was not loaded. Expected condition %s. Waiting time %s",
                                condition, new SimpleDateFormat("mm:ss.SSS").format(new Date(waitFor.toMillis()))))))

                .orElseGet(() -> toGet(DESCRIPTION, GET_URL));
    }
}
