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
import java.util.regex.Pattern;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getSubIterable;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWebElementConditions.defaultPredicateForElements;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWebElementConditions.elementShouldHaveText;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWidgetConditions.defaultPredicateForWidgets;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWidgetConditions.widgetShouldBeLabeledBy;
import static com.github.toy.constructor.selenium.functions.searching.FindLabeledWidgets.labeledWidgets;
import static com.github.toy.constructor.selenium.functions.searching.FindWebElements.webElements;
import static com.github.toy.constructor.selenium.properties.TimeProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.properties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
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

    public static <T extends SearchContext> SequentialMultipleSearchSupplier<T> items(
            Function<SearchContext,List<T>> transformation,
            Predicate<T> condition) {
        return new SequentialMultipleSearchSupplier<>(transformation, condition);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return items(webElements(by, timeUnit, time, predicate.toString()),
                predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return elements(by, timeUnit, time,
                elementShouldHaveText(text).and(predicate));
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return elements(by, timeUnit, time, elementShouldHaveText(textPattern)
                .and(predicate));
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, timeUnit, time, defaultPredicateForElements());
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, text,
                timeUnit, time, defaultPredicateForElements());
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, textPattern, timeUnit, time, defaultPredicateForElements());
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by, Predicate<WebElement> predicate) {
        return elements(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                       String text,
                                                                       Predicate<WebElement> predicate) {
        return elements(by,
                text,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                        Pattern textPattern,
                                                                        Predicate<WebElement> predicate) {
        return elements(by,
                textPattern,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by) {
        return elements(by, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text) {
        return elements(by, text, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern) {
        return elements(by, textPattern, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 TimeUnit timeUnit,
                                                                                 long time,
                                                                                 Predicate<T> predicate) {
        return items(FindWidgets.widgets(tClass, timeUnit, time, predicate.toString()), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 List<String> labels,
                                                                                 TimeUnit timeUnit,
                                                                                 long time,
                                                                                 Predicate<T> predicate) {
        Predicate<? extends T> labeledBy = widgetShouldBeLabeledBy(labels.toArray(new String[]{}));
        Predicate<T> resultPredicate = (Predicate<T>) labeledBy.and(predicate);
        return items(labeledWidgets(tClass, timeUnit, time, resultPredicate.toString()), resultPredicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 String label,
                                                                                 TimeUnit timeUnit,
                                                                                 long time,
                                                                                 Predicate<T> predicate) {
        return widgets(tClass,
                List.of(label),
                timeUnit, time, predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 TimeUnit timeUnit, long time) {
        return widgets(tClass, timeUnit, time, defaultPredicateForWidgets());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        List<String> labels,
                                                                        TimeUnit timeUnit,
                                                                        long time) {
        return widgets(tClass, labels, timeUnit, time, defaultPredicateForWidgets());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 String label,
                                                                                 TimeUnit timeUnit,
                                                                                 long time) {
        return widgets(tClass, label, timeUnit, time, defaultPredicateForWidgets());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        Predicate<T> predicate) {
        return widgets(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        List<String> labels,
                                                                        Predicate<T> predicate) {
        return widgets(tClass,
                labels,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 String label,
                                                                                 Predicate<T> predicate) {
        return widgets(tClass,
                label,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass) {
        return widgets(tClass, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        List<String> labels) {
        return widgets(tClass, labels, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        String label) {
        return widgets(tClass, label, ELEMENT_WAITING_TIME_UNIT.get(),
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
