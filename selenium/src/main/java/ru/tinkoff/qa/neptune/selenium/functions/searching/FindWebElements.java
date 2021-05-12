package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ToStringFormer.getMultipleToString;

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
        return new ArrayList<>(searchContext.findElements(by)
                .stream().map(webElement -> createProxy(webElement.getClass(), new WebElementInterceptor(webElement, by)))
                .collect(toList())) {

            public String toString() {
                if (size() == 0) {
                    return "<...>";
                }

                return getMultipleToString(this);
            }
        };
    }
}
