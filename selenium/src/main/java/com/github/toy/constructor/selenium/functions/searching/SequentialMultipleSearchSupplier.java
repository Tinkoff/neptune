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

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of instances of {@link SearchContext} found from the input value.
     *
     * @param transformation is a function which performs the searching from some {@link SearchContext}
     *                       and transform the list of found items to another list of instances
     *                       of {@link SearchContext}
     * @param condition to specify the searching criteria
     * @param <T> is a type of a value to be returned by resulted function
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends SearchContext> SequentialMultipleSearchSupplier<T> items(
            Function<SearchContext,List<T>> transformation,
            Predicate<T> condition) {
        return new SequentialMultipleSearchSupplier<>(transformation, condition);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return items(webElements(by, timeUnit, time, predicate.toString()),
                predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param text which the desired elements should have
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return elements(by, timeUnit, time,
                elementShouldHaveText(text).and(predicate));
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param textPattern is a regExp to match text of desired elements
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return elements(by, timeUnit, time, elementShouldHaveText(textPattern)
                .and(predicate));
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The
     * result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find elements
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, timeUnit, time, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The
     * result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find elements
     * @param text which the desired elements should have
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, text,
                timeUnit, time, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The
     * result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find elements
     * @param textPattern is a regExp to match text of desired elements
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern,
                                                               TimeUnit timeUnit, long time) {
        return elements(by, textPattern, timeUnit, time, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by, Predicate<WebElement> predicate) {
        return elements(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find elements
     * @param text which the desired elements should have
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                       String text,
                                                                       Predicate<WebElement> predicate) {
        return elements(by,
                text,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find elements
     * @param textPattern is a regExp to match text of desired elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                        Pattern textPattern,
                                                                        Predicate<WebElement> predicate) {
        return elements(by,
                textPattern,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by) {
        return elements(by, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @param text which the desired elements should have
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text) {
        return elements(by, text, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of desired elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern) {
        return elements(by, textPattern, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 TimeUnit timeUnit,
                                                                                 long time,
                                                                                 Predicate<T> predicate) {
        return items(FindWidgets.widgets(tClass, timeUnit, time, predicate.toString()), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find widgets. @param tClass should have at least one not abstract subclass which
     *               also implements {@link com.github.toy.constructor.selenium.api.widget.Labeled} or be
     *               that class
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find telements
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 List<String> labels,
                                                                                 TimeUnit timeUnit,
                                                                                 long time,
                                                                                 Predicate<T> predicate) {
        Predicate<? extends T> labeledBy = widgetShouldBeLabeledBy(labels.toArray(new String[]{}));
        Predicate<T> resultPredicate = (Predicate<T>) labeledBy.and(predicate);
        return items(labeledWidgets(tClass, timeUnit, time, resultPredicate.toString()), resultPredicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find widgets. @param tClass should have at least one not abstract subclass which
     *               also implements {@link com.github.toy.constructor.selenium.api.widget.Labeled} or be
     *               that class
     * @param timeUnit is the parameter of a time to find elements
     * @param time is the parameter of a time to find telements
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
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
