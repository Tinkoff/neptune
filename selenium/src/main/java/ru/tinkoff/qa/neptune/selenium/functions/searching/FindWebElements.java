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
import static org.openqa.selenium.support.locators.RelativeLocator.with;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CGLibProxyBuilder.createProxy;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.ToStringFormer.getMultipleToString;

final class FindWebElements extends FindElementsBuilder<WebElement, List<WebElement>> {
    private By by;
    private By fullBy;
    private boolean isBuildStarted;

    private FindWebElements(By by) {
        checkArgument(nonNull(by), "Locator by-strategy should be defined.");
        this.by = by;
    }

    static FindWebElements webElements(By by) {
        return new FindWebElements(by);
    }


    @Override
    protected void buildLocator(SearchContext searchContext) {
        isBuildStarted = true;
        var relativeLocator = with(by);

        var aboveList = getAbove();
        var belowList = getBelow();
        var toLeftOfList = getToLeftOf();
        var toRightOfList = getToRightOf();
        var nearList = getNear();
        var nearWithDistance = getNearWithDistance();

        if (!aboveList.isEmpty()) {
            for (var element : aboveList) {
                if (element instanceof By) {
                    relativeLocator = relativeLocator.above((By) element);
                } else if (element instanceof WebElement) {
                    relativeLocator = relativeLocator.above((WebElement) element);
                } else if (element instanceof SearchSupplier<?>) {
                    relativeLocator = relativeLocator.above(performFind(searchContext, (SearchSupplier<?>) element));
                }
            }
        }

        if (!belowList.isEmpty()) {
            for (var element : belowList) {
                if (element instanceof By) {
                    relativeLocator = relativeLocator.below((By) element);
                } else if (element instanceof WebElement) {
                    relativeLocator = relativeLocator.below((WebElement) element);
                } else if (element instanceof SearchSupplier) {
                    relativeLocator = relativeLocator.below(performFind(searchContext, (SearchSupplier<?>) element));
                }
            }
        }

        if (!toLeftOfList.isEmpty()) {
            for (var element : toLeftOfList) {
                if (element instanceof By) {
                    relativeLocator = relativeLocator.toLeftOf((By) element);
                } else if (element instanceof WebElement) {
                    relativeLocator = relativeLocator.toLeftOf((WebElement) element);
                } else if (element instanceof SearchSupplier) {
                    relativeLocator = relativeLocator.toLeftOf(performFind(searchContext, (SearchSupplier<?>) element));
                }
            }
        }

        if (!toRightOfList.isEmpty()) {
            for (var element : toRightOfList) {
                if (element instanceof By) {
                    relativeLocator = relativeLocator.toRightOf((By) element);
                } else if (element instanceof WebElement) {
                    relativeLocator = relativeLocator.toRightOf((WebElement) element);
                } else if (element instanceof SearchSupplier) {
                    relativeLocator = relativeLocator.toRightOf(performFind(searchContext, (SearchSupplier<?>) element));
                }
            }
        }

        if (!nearList.isEmpty()) {
            for (var element : nearList) {
                if (element instanceof By) {
                    relativeLocator = relativeLocator.near((By) element);
                } else if (element instanceof WebElement) {
                    relativeLocator = relativeLocator.near((WebElement) element);
                } else if (element instanceof SearchSupplier) {
                    relativeLocator = relativeLocator.near(performFind(searchContext, (SearchSupplier<?>) element));
                }
            }
        }

        if (!nearWithDistance.isEmpty()) {
            for (var element : nearWithDistance.entrySet()) {
                if (element.getKey() instanceof By) {
                    relativeLocator = relativeLocator.near((By) element.getKey(), element.getValue());
                } else if (element.getKey() instanceof WebElement) {
                    relativeLocator = relativeLocator.near((WebElement) element.getKey(), element.getValue());
                } else if (element.getKey() instanceof SearchSupplier) {
                    relativeLocator = relativeLocator.near(performFind(searchContext, (SearchSupplier<?>) element.getKey()), element.getValue());
                }
            }
        }

        fullBy = relativeLocator;
    }

    @Override
    public List<WebElement> apply(SearchContext searchContext) {
        if (!isBuildStarted) {
            buildLocator(searchContext);
        }
        return new ArrayList<>(searchContext.findElements(fullBy).stream()
                .map(webElement -> createProxy(webElement.getClass(), new WebElementInterceptor(webElement, fullBy))).collect(toList())) {

            public String toString() {
                if (size() == 0) {
                    return "<...>";
                }

                return getMultipleToString(this);
            }
        };
    }
}
