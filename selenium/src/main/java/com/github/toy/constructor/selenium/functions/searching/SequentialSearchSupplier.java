package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.api.widget.drafts.Button;
import com.github.toy.constructor.selenium.api.widget.drafts.Flag;
import com.github.toy.constructor.selenium.api.widget.drafts.Link;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;
import static com.github.toy.constructor.core.api.ToGetConditionalHelper.getFromIterable;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWebElementConditions.defaultPredicateForElements;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWebElementConditions.elementShouldHaveText;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWidgetConditions.defaultPredicateForWidgets;
import static com.github.toy.constructor.selenium.functions.searching.DefaultWidgetConditions.widgetShouldBeLabeledBy;
import static com.github.toy.constructor.selenium.functions.searching.FindLabeledWidgets.labeledWidgets;
import static com.github.toy.constructor.selenium.functions.searching.FindWebElements.webElements;
import static com.github.toy.constructor.selenium.functions.searching.FindWidgets.widgets;
import static com.github.toy.constructor.selenium.properties.TimeProperties.ELEMENT_WAITING_TIME_VALUE;
import static com.github.toy.constructor.selenium.properties.TimeUnitProperties.ELEMENT_WAITING_TIME_UNIT;
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

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link SearchContext} found from the input value.
     *
     * @param transformation is a function which performs the searching from some {@link SearchContext}
     *                       and transform a found item to another instance of {@link SearchContext}
     * @param condition to specify the searching criteria
     * @param <T> is a type of a value to be returned by resulted function
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends SearchContext> SequentialSearchSupplier<T> item(
            Function<SearchContext,List<T>> transformation,
            Predicate<T> condition) {
        return new SequentialSearchSupplier<>(transformation, condition);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                        TimeUnit timeUnit,
                                                        long time, Predicate<WebElement> predicate) {
        return item(webElements(by, timeUnit, time, predicate.toString()), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               String text,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return element(by, timeUnit, time,
                elementShouldHaveText(text).and(predicate));
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               Pattern textPattern,
                                                               TimeUnit timeUnit,
                                                               long time, Predicate<WebElement> predicate) {
        return element(by, timeUnit, time, elementShouldHaveText(textPattern)
                .and(predicate));
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               TimeUnit timeUnit, long time) {
        return element(by, timeUnit, time, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               String text,
                                                               TimeUnit timeUnit, long time) {
        return element(by, text,
                timeUnit, time, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               Pattern textPattern,
                                                               TimeUnit timeUnit, long time) {
        return element(by, textPattern, timeUnit, time, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by, Predicate<WebElement> predicate) {
        return element(by,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               String text,
                                                               Predicate<WebElement> predicate) {
        return element(by,
                text,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               Pattern textPattern,
                                                               Predicate<WebElement> predicate) {
        return element(by,
                textPattern,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by) {
        return element(by, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               String text) {
        return element(by, text, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param by by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<WebElement> element(By by,
                                                               Pattern textPattern) {
        return element(by, textPattern, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        TimeUnit timeUnit,
                                                                        long time,
                                                                        Predicate<T> predicate) {
        return item(widgets(tClass, timeUnit, time, predicate.toString()), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        List<String> labels,
                                                                        TimeUnit timeUnit,
                                                                        long time,
                                                                        Predicate<T> predicate) {
        Predicate<? extends T> labeledBy = widgetShouldBeLabeledBy(labels.toArray(new String[]{}));
        Predicate<T> resultPredicate = (Predicate<T>) labeledBy.and(predicate);
        return item(labeledWidgets(tClass, timeUnit, time, resultPredicate.toString()), resultPredicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find the widget.
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        String label,
                                                                        TimeUnit timeUnit,
                                                                        long time,
                                                                        Predicate<T> predicate) {
        return widget(tClass,
                List.of(label),
                timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        TimeUnit timeUnit,
                                                                        long time) {
        return widget(tClass, timeUnit, time, defaultPredicateForWidgets());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                         List<String> labels,
                                                                         TimeUnit timeUnit,
                                                                         long time) {
        return widget(tClass, labels, timeUnit, time, defaultPredicateForWidgets());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find the widget.
     * @param timeUnit is the parameter of a time to find the element
     * @param time is the parameter of a time to find the element
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        String label,
                                                                        TimeUnit timeUnit,
                                                                        long time) {
        return widget(tClass, label, timeUnit, time, defaultPredicateForWidgets());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        Predicate<T> predicate) {
        return widget(tClass,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        List<String> labels,
                                                                        Predicate<T> predicate) {
        return widget(tClass,
                labels,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find the widget.
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        String label,
                                                                        Predicate<T> predicate) {
        return widget(tClass,
                label,
                ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass) {
        return widget(tClass, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        List<String> labels) {
        return widget(tClass, labels, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The searching
     * will take 1 minute if system properties {@code waiting.for.elements.time.unit} and
     * {@code waiting.for.elements.time} are not defined. Otherwise it takes the specified time. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *              find the widget.
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static <T extends Widget> SequentialSearchSupplier<T> widget(Class<T> tClass,
                                                                        String label) {
        return widget(tClass, label, ELEMENT_WAITING_TIME_UNIT.get(),
                ELEMENT_WAITING_TIME_VALUE.get());
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param timeUnit is the parameter of a time to find a button
     * @param time is the parameter of a time to find a button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(TimeUnit timeUnit,
                                                          long time,
                                                          Predicate<Button> predicate) {
        return widget(Button.class, timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @param timeUnit is the parameter of a time to find a button
     * @param time is the parameter of a time to find a button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(List<String> labels,
                                                          TimeUnit timeUnit,
                                                          long time,
                                                          Predicate<Button> predicate) {
        return widget(Button.class, labels, timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find a button.
     * @param timeUnit is the parameter of a time to find a button
     * @param time is the parameter of a time to find a button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(String label,
                                                          TimeUnit timeUnit,
                                                          long time,
                                                          Predicate<Button> predicate) {
        return widget(Button.class,
                label,
                timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param timeUnit is the parameter of a time to find a button
     * @param time is the parameter of a time to find a button
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(TimeUnit timeUnit,
                                                          long time) {
        return widget(Button.class, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @param timeUnit is the parameter of a time to find a button
     * @param time is the parameter of a time to find a button
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(List<String> labels,
                                                          TimeUnit timeUnit,
                                                          long time) {
        return widget(Button.class, labels, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find a button.
     * @param timeUnit is the parameter of a time to find a button
     * @param time is the parameter of a time to find a button
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(String label,
                                                          TimeUnit timeUnit,
                                                          long time) {
        return widget(Button.class, label, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are
     * not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(Predicate<Button> predicate) {
        return widget(Button.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(List<String> labels,
                                                          Predicate<Button> predicate) {
        return widget(Button.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find a button.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(String label,
                                                          Predicate<Button> predicate) {
        return widget(Button.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button() {
        return widget(Button.class);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @return an instance of {@link SequentialSearchSupplier}
     */
        public static SequentialSearchSupplier<Button> button(List<String> labels) {
        return widget(Button.class, labels);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *              find a button.
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Button> button(String label) {
        return widget(Button.class, label);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param timeUnit is the parameter of a time to find a flag
     * @param time is the parameter of a time to find a flag
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(TimeUnit timeUnit,
                                                      long time,
                                                      Predicate<Flag> predicate) {
        return widget(Flag.class, timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @param timeUnit is the parameter of a time to find a flag
     * @param time is the parameter of a time to find a flag
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(List<String> labels,
                                                          TimeUnit timeUnit,
                                                          long time,
                                                          Predicate<Flag> predicate) {
        return widget(Flag.class, labels, timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find a flag.
     * @param timeUnit is the parameter of a time to find a flag
     * @param time is the parameter of a time to find a flag
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(String label,
                                                          TimeUnit timeUnit,
                                                          long time,
                                                          Predicate<Flag> predicate) {
        return widget(Flag.class,
                label,
                timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param timeUnit is the parameter of a time to find a flag
     * @param time is the parameter of a time to find a flag
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(TimeUnit timeUnit, long time) {
        return widget(Flag.class, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @param timeUnit is the parameter of a time to find a flag
     * @param time is the parameter of a time to find a flag
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(List<String> labels,
                                                        TimeUnit timeUnit,
                                                        long time) {
        return widget(Flag.class, labels, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find a flag.
     * @param timeUnit is the parameter of a time to find a flag
     * @param time is the parameter of a time to find a flag
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(String label,
                                                      TimeUnit timeUnit,
                                                      long time) {
        return widget(Flag.class, label, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are
     * not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(Predicate<Flag> predicate) {
        return widget(Flag.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(List<String> labels,
                                                      Predicate<Flag> predicate) {
        return widget(Flag.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find a flag.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(String label, Predicate<Flag> predicate) {
        return widget(Flag.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag() {
        return widget(Flag.class);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(List<String> labels) {
        return widget(Flag.class, labels);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *              find a flag.
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Flag> flag(String label) {
        return widget(Flag.class, label);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param timeUnit is the parameter of a time to find a link
     * @param time is the parameter of a time to find a link
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(TimeUnit timeUnit,
                                                      long time,
                                                      Predicate<Link> predicate) {
        return widget(Link.class, timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @param timeUnit is the parameter of a time to find a link
     * @param time is the parameter of a time to find a link
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(List<String> labels,
                                                      TimeUnit timeUnit,
                                                      long time,
                                                      Predicate<Link> predicate) {
        return widget(Link.class, labels, timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find a link.
     * @param timeUnit is the parameter of a time to find a link
     * @param time is the parameter of a time to find a link
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(String label,
                                                      TimeUnit timeUnit,
                                                      long time,
                                                      Predicate<Link> predicate) {
        return widget(Link.class,
                label,
                timeUnit, time, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param timeUnit is the parameter of a time to find a link
     * @param time is the parameter of a time to find a link
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(TimeUnit timeUnit, long time) {
        return widget(Link.class, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @param timeUnit is the parameter of a time to find a link
     * @param time is the parameter of a time to find a link
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(List<String> labels,
                                                      TimeUnit timeUnit,
                                                      long time) {
        return widget(Link.class, labels, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find a link.
     * @param timeUnit is the parameter of a time to find a link
     * @param time is the parameter of a time to find a link
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(String label,
                                                      TimeUnit timeUnit,
                                                      long time) {
        return widget(Link.class, label, timeUnit, time);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are
     * not defined. Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(Predicate<Link> predicate) {
        return widget(Link.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(List<String> labels,
                                                      Predicate<Link> predicate) {
        return widget(Link.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time.
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find a link.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(String label, Predicate<Link> predicate) {
        return widget(Link.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link() {
        return widget(Link.class);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(List<String> labels) {
        return widget(Link.class, labels);
    }

    /**
     * Returns an instance of {@link SequentialSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The searching will take 1 minute if system properties
     * {@code waiting.for.elements.time.unit} and {@code waiting.for.elements.time} are not defined.
     * Otherwise it takes the specified time. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     * @see com.github.toy.constructor.selenium.properties.TimeUnitProperties#ELEMENT_WAITING_TIME_UNIT
     * @see com.github.toy.constructor.selenium.properties.TimeProperties#ELEMENT_WAITING_TIME_VALUE
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *              find a link.
     * @return an instance of {@link SequentialSearchSupplier}
     */
    public static SequentialSearchSupplier<Link> link(String label) {
        return widget(Link.class, label);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some element from a parent element.
     * @return self-reference
     */
    public SequentialSearchSupplier<R> from(SequentialSearchSupplier<? extends SearchContext> from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is a parent element.
     * @param <Q> is a type of the parent element.
     * @return self-reference
     */
    @Override
    public <Q extends SearchContext> SequentialSearchSupplier<R> from(Q from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param child is how to find a child element.
     * @param <Q> is a type of the child element.
     * @return the instance of {@link SequentialSearchSupplier} which is given as the parameter.
     * But it wraps the new constructed chained searching.
     */
    public <Q extends SearchContext> SequentialSearchSupplier<Q> child(SequentialSearchSupplier<Q> child) {
        return child.from(this);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param children is how to find a list of child elements.
     * @param <Q> is a type of the child element.
     * @return the instance of {@link SequentialMultipleSearchSupplier} which is given as the parameter.
     * But it wraps the new constructed chained searching.
     */
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
