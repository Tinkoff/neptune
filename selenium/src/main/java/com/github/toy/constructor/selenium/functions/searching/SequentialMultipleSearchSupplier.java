package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSubIterable;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeUnitProperty.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeValueProperty.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWebElementConditions.elementShouldBeDisplayed;
import static com.github.toy.constructor.selenium.functions.searching.FindWebElements.webElements;
import static java.util.Optional.ofNullable;

public final class SequentialMultipleSearchSupplier<R extends SearchContext>
        extends SequentialGetSupplier<SeleniumSteps, List<R>, SearchContext, SequentialMultipleSearchSupplier<R>> {
    
    private final Function<SearchContext, List<R>>  searching;
    private final Predicate<R> condition;

    private SequentialMultipleSearchSupplier(Function<SearchContext, List<R>> searching,
                                     Predicate<R> condition) {
        this.searching = searching;
        this.condition = condition;
    }

    private static <T> Predicate<T> withNoCondition() {
        return condition("with no restriction", t -> true);
    }

    public static <T extends SearchContext> SequentialMultipleSearchSupplier<T> items(
            Function<SearchContext,List<T>> transformation,
            Predicate<T> condition) {
        return new SequentialMultipleSearchSupplier<>(transformation, condition);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return items(webElements(by, timeUnit, time), predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, timeUnit, time, withNoCondition());
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by, Predicate<WebElement> predicate) {
        return elements(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by) {
        return elements(by, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static SequentialMultipleSearchSupplier<WebElement> displayedElements(By by,
                                                                        TimeUnit timeUnit,
                                                                        long time, Predicate<WebElement> predicate) {
        return elements(by,
                timeUnit,
                time,
                elementShouldBeDisplayed().and(predicate));
    }

    public static SequentialMultipleSearchSupplier<WebElement> displayedElements(By by,
                                                                        TimeUnit timeUnit, long time) {
        return displayedElements(by, timeUnit, time, withNoCondition());
    }

    public static SequentialMultipleSearchSupplier<WebElement> displayedElements(By by,
                                                                        Predicate<WebElement> predicate) {
        return displayedElements(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> displayedElements(By by) {
        return displayedElements(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 TimeUnit timeUnit,
                                                                                 long time,
                                                                                 Predicate<T> predicate) {
        return items(FindWidgets.widgets(tClass, timeUnit, time, predicate.toString()), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 TimeUnit timeUnit, long time) {
        return widgets(tClass, timeUnit, time, withNoCondition());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        Predicate<T> predicate) {
        return widgets(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass) {
        return widgets(tClass, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> visibleWidgets(Class<T> tClass,
                                                                                        TimeUnit timeUnit,
                                                                                        long time,
                                                                                        Predicate<T> predicate) {
        return widgets(tClass,
                timeUnit,
                time,
                DefaultWidgetConditions.<T>widgetShouldBeVisible().and(predicate));
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> visibleWidgets(Class<T> tClass,
                                                                               TimeUnit timeUnit, long time) {
        return visibleWidgets(tClass, timeUnit, time, withNoCondition());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> visibleWidgets(Class<T> tClass,
                                                                               Predicate<T> predicate) {
        return visibleWidgets(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> visibleWidgets(Class<T> tClass) {
        return visibleWidgets(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public SequentialMultipleSearchSupplier<R> from(SequentialSearchSupplier<? extends SearchContext> from) {
        return super.from(from);
    }

    @Override
    public <Q extends SearchContext> SequentialMultipleSearchSupplier<R> from(Q from) {
        return super.from(from);
    }

    @Override
    public Function<SeleniumSteps, List<R>> get() {
        return ofNullable(super.get())
                .orElseGet(() -> {
                    Function<SearchContext, List<R>> endFunction = getEndFunction();
                    return toGet(endFunction.toString(), seleniumSteps ->
                            endFunction.apply(seleniumSteps.getWrappedDriver()));
                });
    }

    @Override
    protected Function<SearchContext, List<R>> getEndFunction() {
        return getSubIterable("List of",
                searching,
                condition, true,
                true);
    }
}
