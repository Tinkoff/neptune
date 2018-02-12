package com.github.toy.constructor.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.FluentWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static java.lang.String.format;
import static java.util.List.of;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;
    private final TimeUnit timeUnit;
    private final long time;
    private final String conditionString;

    private FindWebElements(By by, TimeUnit timeUnit, long time, String conditionString) {
        this.by = by;
        this.timeUnit = timeUnit;
        this.time = time;
        this.conditionString = conditionString;
    }

    static Function<SearchContext, List<WebElement>> webElements(By by, TimeUnit timeUnit, long time, String conditionString) {
        return toGet(format("Web elements located by %s", by), new FindWebElements(by, timeUnit, time, conditionString));
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
                    .withTimeout(time, timeUnit)
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
