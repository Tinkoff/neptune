package com.github.toy.constructor.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.List.of;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;
import static org.apache.commons.lang3.StringUtils.isBlank;

class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;
    private final Duration duration;
    private final String conditionString;

    private FindWebElements(By by, Duration duration, String conditionString) {
        checkArgument(by != null, "Locator by-strategy should be defined.");
        checkArgument(duration != null, "Duration should be defined.");
        checkArgument(!isBlank(conditionString), "Description of the condition should not be empty.");
        this.by = by;
        this.duration = duration;
        this.conditionString = conditionString;
    }

    static Function<SearchContext, List<WebElement>> webElements(By by, Duration duration, String conditionString) {
        return toGet(format("Web elements located by %s", by), new FindWebElements(by, duration, conditionString));
    }

    private WebElement createWidget(WebElement webElement) {
        Enhancer enhancer = new Enhancer();
        WebElementInterceptor interceptor = new WebElementInterceptor(webElement, by, conditionString);

        enhancer.setUseCache(false);
        enhancer.setCallbackType(WebElementInterceptor.class);
        enhancer.setSuperclass(webElement.getClass());
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(webElement.getClass().getClassLoader());

        Objenesis objenesis = new ObjenesisStd();
        Object proxy = objenesis.newInstance(proxyClass);
        return (WebElement) proxy;
    }

    @Override
    public List<WebElement> apply(SearchContext searchContext) {
        FluentWait<SearchContext> wait = new FluentWait<>(searchContext);
        try {
            return wait.ignoring(StaleElementReferenceException.class)
                    .withTimeout(duration)
                    .until(searchContextParam -> {
                        List<WebElement> found = searchContext.findElements(by)
                                .stream().map(this::createWidget)
                                .collect(Collectors.toList());
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
