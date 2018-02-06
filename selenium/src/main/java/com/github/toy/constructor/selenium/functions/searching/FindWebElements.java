package com.github.toy.constructor.selenium.functions.searching;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;
import static java.util.List.of;

class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;
    private final TimeUnit timeUnit;
    private final long time;

    private FindWebElements(By by, TimeUnit timeUnit, long time) {
        this.by = by;
        this.timeUnit = timeUnit;
        this.time = time;
    }

    static Function<SearchContext, List<WebElement>> webElements(By by, TimeUnit timeUnit, long time) {
        return toGet(format("Web elements located by %s", by), new FindWebElements(by, timeUnit, time));
    }

    @Override
    public List<WebElement> apply(SearchContext searchContext) {
        FluentWait<SearchContext> wait = new FluentWait<>(searchContext);
        try {
            return wait.ignoring(StaleElementReferenceException.class)
                    .withTimeout(time, timeUnit)
                    .until(searchContextParam -> {
                        List<WebElement> found = searchContext.findElements(by);
                        if (found.size() > 0) {
                            return found;
                        }
                        else {
                            return null;
                        }
                    });
        }
        catch (TimeoutException e) {
            return of();
        }
    }
}
