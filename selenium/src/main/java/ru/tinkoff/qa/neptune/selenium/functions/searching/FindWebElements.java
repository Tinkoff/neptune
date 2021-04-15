package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;

final class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;

    private FindWebElements(By by) {
        checkArgument(nonNull(by), "Locator by-strategy should be defined.");
        this.by = by;
    }

    static FindWebElements webElements(By by) {
        return new FindWebElements(by);
    }

    @Override
    public List<WebElement> apply(SearchContext searchContext) {
        return new LoggableElementList<>(searchContext.findElements(by)
                .stream().map(webElement -> {
                    var stringDescription = format("web element [%s]", by);
                    return createProxy(webElement.getClass(), new WebElementInterceptor(webElement, stringDescription));
                })
                .collect(toList())) {
            @Override
            public String toString() {
                return format("%s web elements found %s", size(), by);
            }
        };
    }
}
