package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.*;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static java.util.Optional.ofNullable;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.apache.commons.lang3.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;

@SuppressWarnings("unchecked")
final class FindWebElements implements Function<SearchContext, List<WebElement>> {

    private final By by;
    private Supplier<String> criteriaDescription;

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
                    var stringDescription = format("Web element found %s", by);
                    var criteria = ofNullable(criteriaDescription)
                            .map(Supplier::get)
                            .orElse(EMPTY);
                    if (!isBlank(criteria)) {
                        stringDescription = format("%s ['%s']", stringDescription, criteria);
                    }
                    return createProxy(webElement.getClass(), new WebElementInterceptor(webElement, stringDescription));
                })
                .collect(toList())) {
            @Override
            public String toString() {
                var stringDescription = format("%s web elements found %s", size(), by);
                var criteria = ofNullable(criteriaDescription)
                        .map(Supplier::get)
                        .orElse(EMPTY);
                if (!isBlank(criteria)) {
                    stringDescription = format("%s and meet criteria ['%s']", stringDescription, criteria);
                }
                return stringDescription;
            }
        };
    }

    void setCriteriaDescription(Supplier<String> criteriaDescription) {
        checkNotNull(criteriaDescription);
        this.criteriaDescription = criteriaDescription;
    }
}
