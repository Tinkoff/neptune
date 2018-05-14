package com.github.toy.constructor.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openqa.selenium.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;
import static org.apache.commons.lang3.StringUtils.isBlank;

final class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;
    private final String conditionString;

    private FindWebElements(By by, String conditionString) {
        checkArgument(by != null, "Locator by-strategy should be defined.");
        checkArgument(!isBlank(conditionString), "Description of the condition should not be empty.");
        this.by = by;
        this.conditionString = conditionString;
    }

    static Function<SearchContext, List<WebElement>> webElements(By by, String conditionString) {
        return toGet(format("Web elements located [%s]", by), new FindWebElements(by, conditionString));
    }

    private WebElement createWebElement(WebElement webElement) {
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
        return searchContext.findElements(by)
                .stream().map(this::createWebElement)
                .collect(Collectors.toList());
    }
}
