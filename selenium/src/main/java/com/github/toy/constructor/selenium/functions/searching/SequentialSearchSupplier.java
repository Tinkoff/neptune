package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;

import static com.github.toy.constructor.core.api.StoryWriter.condition;
import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getFromIterable;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeUnitProperty.ELEMENT_WAITING_TIME_UNIT;
import static com.github.toy.constructor.selenium.PropertySupplier.TimeValueProperty.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWebElementConditions.elementShouldBeDisplayed;
import static com.github.toy.constructor.selenium.functions.searching.FindWebElements.webElements;
import static com.github.toy.constructor.selenium.functions.searching.FindWidgets.widgets;
import static java.lang.String.format;
import static java.util.Optional.ofNullable;

public final class SequentialSearchSupplier<R extends SearchContext>
        extends SequentialGetSupplier<SeleniumSteps, R, SearchContext, SequentialSearchSupplier<R>> {

    private final Function<SearchContext, List<R>>  searching;
    private final Predicate<R> condition;

    private SequentialSearchSupplier(Function<SearchContext, List<R>> searching, Predicate<R> condition) {
        this.searching = searching;
        this.condition = condition;
    }

    private static <T> Predicate<T> withNoCondition() {
        return condition("with no restriction", t -> true);
    }

    public static <T extends SearchContext> SequentialSearchSupplier<T> item(
            Function<SearchContext,List<T>> transformation,
            Predicate<T> condition) {
        return new SequentialSearchSupplier<>(transformation, condition);
    }

    public static SequentialSearchSupplier<WebElement> element(By by,
                                                        TimeUnit timeUnit,
                                                        long time, Predicate<WebElement> predicate) {
        return item(webElements(by, timeUnit, time), predicate);
    }

    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               TimeUnit timeUnit, long time) {
        return element(by, timeUnit, time, withNoCondition());
    }

    public static SequentialSearchSupplier<WebElement> element(By by, Predicate<WebElement> predicate) {
        return element(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialSearchSupplier<WebElement> element(By by) {
        return element(by, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static SequentialSearchSupplier<WebElement> displayedElement(By by,
                                                                       TimeUnit timeUnit,
                                                                       long time, Predicate<WebElement> predicate) {
        return element(by,
                timeUnit,
                time,
                elementShouldBeDisplayed().and(predicate));
    }

    public static SequentialSearchSupplier<WebElement> displayedElement(By by,
                                                                        TimeUnit timeUnit, long time) {
        return displayedElement(by, timeUnit, time, withNoCondition());
    }

    public static SequentialSearchSupplier<WebElement> displayedElement(By by,
                                                                        Predicate<WebElement> predicate) {
        return displayedElement(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialSearchSupplier<WebElement> displayedElement(By by) {
        return displayedElement(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        TimeUnit timeUnit,
                                                                        long time,
                                                                        Predicate<T> predicate) {
        return item(widgets(tClass, timeUnit, time), predicate);
    }

    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                               TimeUnit timeUnit, long time) {
        return widget(tClass, timeUnit, time, withNoCondition());
    }

    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        Predicate<T> predicate) {
        return widget(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass) {
        return widget(tClass, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialSearchSupplier<T> visibleWidget(Class<T> tClass,
                                                                     TimeUnit timeUnit,
                                                                     long time, Predicate<T> predicate) {
        return widget(tClass,
                timeUnit,
                time,
                DefaultWidgetConditions.<T>widgetShouldBeVisible().and(predicate));
    }

    public static <T extends Widget> SequentialSearchSupplier<T> visibleWidget(Class<T> tClass,
                                                                               TimeUnit timeUnit, long time) {
        return visibleWidget(tClass, timeUnit, time, withNoCondition());
    }

    public static <T extends Widget> SequentialSearchSupplier<T> visibleWidget(Class<T> tClass,
                                                                               Predicate<T> predicate) {
        return visibleWidget(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialSearchSupplier<T> visibleWidget(Class<T> tClass) {
        return visibleWidget(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public SequentialSearchSupplier<R> from(SequentialSearchSupplier<? extends SearchContext> from) {
        return super.from(from);
    }

    @Override
    public <Q extends SearchContext> SequentialSearchSupplier<R> from(Q from) {
        return super.from(from);
    }

    public <Q extends SearchContext> SequentialSearchSupplier<Q> child(SequentialSearchSupplier<Q> child) {
        return child.from(this);
    }

    public <Q extends SearchContext> SequentialMultipleSearchSupplier<Q> child(SequentialMultipleSearchSupplier<Q> children) {
        return children.from(this);
    }

    @Override
    public Function<SeleniumSteps, R> get() {
        return ofNullable(super.get())
                .orElseGet(() -> {
            Function<SearchContext, R> endFunction = getEndFunction();
            return toGet(endFunction.toString(), seleniumSteps ->
                    endFunction.apply(seleniumSteps.getWrappedDriver()));
        });
    }

    @Override
    protected Function<SearchContext, R> getEndFunction() {
        Function<SearchContext, R> resultFunction = getFromIterable("A single item",
                searching,
                condition, true,
                true);
        return toGet(resultFunction.toString(), searchContext ->
                ofNullable(resultFunction.apply(searchContext)).orElseThrow(() ->
                        new NoSuchElementException(format("Nothing was found. Attempt to get: %s. Condition: %s",
                                searching,
                                condition))));
    }
}
