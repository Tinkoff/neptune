package ru.tinkoff.qa.neptune.selenium.functions.searching;

import net.sf.cglib.proxy.Callback;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import org.objenesis.Objenesis;
import org.objenesis.ObjenesisStd;
import org.openqa.selenium.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static net.sf.cglib.proxy.Enhancer.registerCallbacks;

@SuppressWarnings("unchecked")
final class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;
    private final String conditionString;

    private FindWebElements(By by, String conditionString) {
        checkArgument(by != null, "Locator by-strategy should be defined.");
        checkArgument(conditionString != null, "Description of the conditions should be defined.");
        this.by = by;
        this.conditionString = conditionString;
    }

    static Function<SearchContext, List<WebElement>> webElements(By by, String conditionString) {
        return new FindWebElements(by, conditionString);
    }

    private <T> T createProxy(Class<T> tClass, MethodInterceptor interceptor) {
        Enhancer enhancer = new Enhancer();

        enhancer.setUseCache(false);
        enhancer.setCallbackType(interceptor.getClass());
        enhancer.setSuperclass(tClass);
        Class<?> proxyClass = enhancer.createClass();
        registerCallbacks(proxyClass, new Callback[]{interceptor});
        enhancer.setClassLoader(tClass.getClass().getClassLoader());

        Objenesis objenesis = new ObjenesisStd();
        Object proxy = objenesis.newInstance(proxyClass);
        return (T) proxy;
    }

    @Override
    public List<WebElement> apply(SearchContext searchContext) {
        List<WebElement> elements = searchContext.findElements(by)
                .stream().map(webElement -> createProxy(webElement.getClass(),
                        new WebElementInterceptor(webElement, by, conditionString)))
                .collect(Collectors.toList());
        return createProxy(elements.getClass(), new WebElementListInterceptor(elements, by, conditionString));
    }
}
