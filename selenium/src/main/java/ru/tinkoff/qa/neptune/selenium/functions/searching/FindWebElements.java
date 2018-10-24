package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.*;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchingProxyBuilder.createProxy;

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

    @Override
    public List<WebElement> apply(SearchContext searchContext) {
        return new ElementList<>(searchContext.findElements(by)
                .stream().map(webElement -> {
                    var stringDescription = format("Web element found %s", by);
                    if (!isBlank(conditionString)) {
                        stringDescription = format("%s on conditions '%s'", stringDescription, conditionString);
                    }
                    return createProxy(webElement.getClass(), new WebElementInterceptor(webElement, stringDescription));
                })
                .collect(Collectors.toList())) {
            @Override
            public String toString() {
                var stringDescription = format("%s web elements found %s", size(), by);
                if (!isBlank(conditionString)) {
                    stringDescription = format("%s on conditions '%s'", stringDescription, conditionString);
                }
                return stringDescription;
            }
        };
    }
}
