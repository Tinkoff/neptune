package ru.tinkoff.qa.neptune.selenium.content.management;

import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.GetWindowSupplier.window;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowCriteria.titleMatches;
import static ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.WindowCriteria.urlMatches;

/**
 * Defines a browser window/tab to switch to
 */
@Retention(RUNTIME)
@Target({TYPE, METHOD})
public @interface SwitchToWindow {

    /**
     * @return an index of a browser window/tab to switch to. The minimal value of 0
     */
    int index() default -1;

    /**
     * @return a title/fragment or regexp of a title of a browser window/tab to switch to
     */
    String title() default EMPTY;

    /**
     * @return a url/fragment or regexp of a url which is loaded in a browser window/tab to switch to
     */
    String url() default EMPTY;

    /**
     * @return a unit of a time to wait for a browser window/tab to switch to. Default is {@link ChronoUnit#SECONDS}
     */
    ChronoUnit waitingTimeUnit() default SECONDS;

    /**
     * @return value of a time to wait for a browser window/tab to switch to
     */
    long waitingTime() default 0;

    /**
     * Util class which is used to read metadata of some class/method and create an instance of {@link GetWindowSupplier}
     */
    class SwitchToWindowReader {

        static GetWindowSupplier getWindow(SwitchToWindow switchToWindow) {
            GetWindowSupplier result;

            if (switchToWindow.index() <= 0) {
                result = window();
            } else {
                result = window(switchToWindow.index());
            }

            if (isNotBlank(switchToWindow.title())) {
                result = result.criteria(titleMatches(switchToWindow.title()));
            }

            if (isNotBlank(switchToWindow.url())) {
                result = result.criteria(urlMatches(switchToWindow.url()));
            }

            if (switchToWindow.waitingTime() > 0) {
                result = result.timeOut(Duration.of(switchToWindow.waitingTime(), switchToWindow.waitingTimeUnit()));
            }

            return result;
        }
    }
}
