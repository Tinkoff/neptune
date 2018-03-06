package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.SequentialGetSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.api.widget.drafts.Button;
import com.github.toy.constructor.selenium.api.widget.drafts.Flag;
import com.github.toy.constructor.selenium.api.widget.drafts.Link;
import com.github.toy.constructor.selenium.api.widget.drafts.Select;
import com.github.toy.constructor.selenium.properties.WaitingProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
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
import static com.github.toy.constructor.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;
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
     * @param duration is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                        Duration duration,
                                                                        Predicate<WebElement> predicate) {
        return items(webElements(by, duration, predicate.toString()),
                predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param text which the desired elements should have
     * @param duration is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                        String text,
                                                                        Duration duration,
                                                                        Predicate<WebElement> predicate) {
        return elements(by, duration,
                elementShouldHaveText(text).and(predicate));
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param textPattern is a regExp to match text of desired elements
     * @param duration is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                        Pattern textPattern,
                                                                        Duration duration,
                                                                        Predicate<WebElement> predicate) {
        return elements(by, duration, elementShouldHaveText(textPattern)
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
     * @param duration is the parameter of a time to find elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Duration duration) {
        return elements(by, duration, defaultPredicateForElements());
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
     * @param duration is the parameter of a time to find elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               String text,
                                                               Duration duration) {
        return elements(by, text, duration, defaultPredicateForElements());
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
     * @param duration is the parameter of a time to find elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern,
                                                               Duration duration) {
        return elements(by, textPattern, duration, defaultPredicateForElements());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param by locator strategy to find elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by, Predicate<WebElement> predicate) {
        return elements(by, ELEMENT_WAITING_DURATION.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param by locator strategy to find elements
     * @param text which the desired elements should have
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                       String text,
                                                                       Predicate<WebElement> predicate) {
        return elements(by, text, ELEMENT_WAITING_DURATION.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param by locator strategy to find elements
     * @param textPattern is a regExp to match text of desired elements
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                                        Pattern textPattern,
                                                                        Predicate<WebElement> predicate) {
        return elements(by, textPattern, ELEMENT_WAITING_DURATION.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by) {
        return elements(by, ELEMENT_WAITING_DURATION.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param text which the desired elements should have
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by, String text) {
        return elements(by, text, ELEMENT_WAITING_DURATION.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found elements if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found elements which are displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of desired elements
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<WebElement> elements(By by,
                                                               Pattern textPattern) {
        return elements(by, textPattern, ELEMENT_WAITING_DURATION.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param duration is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 Duration duration,
                                                                                 Predicate<T> predicate) {
        return items(FindWidgets.widgets(tClass, duration, predicate.toString()), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find widgets.
     * @param duration is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 List<String> labels,
                                                                                 Duration duration,
                                                                                 Predicate<T> predicate) {
        Predicate<? extends T> labeledBy = widgetShouldBeLabeledBy(labels.toArray(new String[]{}));
        Predicate<T> resultPredicate = (Predicate<T>) labeledBy.and(predicate);
        return items(labeledWidgets(tClass, duration, resultPredicate.toString()), resultPredicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find widgets.
     * @param duration is the parameter of a time to find elements
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 String label,
                                                                                 Duration duration,
                                                                                 Predicate<T> predicate) {
        return widgets(tClass,
                List.of(label),
                duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value. The
     * result function will return a list of any found widgets if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found widgets which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param duration is the parameter of a time to find elements
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 Duration duration) {
        return widgets(tClass, duration, defaultPredicateForWidgets());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value. The
     * result function will return a list of any found widgets if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found widgets which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find widgets.
     * @param duration is the parameter of a time to find elements
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        List<String> labels,
                                                                        Duration duration) {
        return widgets(tClass, labels, duration, defaultPredicateForWidgets());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value. The
     * result function will return a list of any found widgets if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found widgets which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find widgets.
     * @param duration is the parameter of a time to find elements
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 String label,
                                                                                 Duration duration) {
        return widgets(tClass, label, duration, defaultPredicateForWidgets());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        Predicate<T> predicate) {
        return widgets(tClass, ELEMENT_WAITING_DURATION.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find widgets.
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        List<String> labels,
                                                                        Predicate<T> predicate) {
        return widgets(tClass, labels, ELEMENT_WAITING_DURATION.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find widgets.
     * @param predicate to specify the searching criteria
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                                 String label,
                                                                                 Predicate<T> predicate) {
        return widgets(tClass, label, ELEMENT_WAITING_DURATION.get(), predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found widgets if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found widgets which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass) {
        return widgets(tClass, ELEMENT_WAITING_DURATION.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found widgets if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found widgets which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find widgets.
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        List<String> labels) {
        return widgets(tClass, labels, ELEMENT_WAITING_DURATION.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found widgets if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found widgets which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instances should be returned. tClass should have at least one
     *               not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find widgets.
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static <T extends Widget> SequentialMultipleSearchSupplier<T> widgets(Class<T> tClass,
                                                                        String label) {
        return widgets(tClass, label, ELEMENT_WAITING_DURATION.get());
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * @param duration is the parameter of a time to find buttons
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(Duration duration,
                                                                   Predicate<Button> predicate) {
        return widgets(Button.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find buttons.
     * @param duration is the parameter of a time to find buttons
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(List<String> labels,
                                                                   Duration duration,
                                                                   Predicate<Button> predicate) {
        return widgets(Button.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find buttons.
     * @param duration is the parameter of a time to find buttons
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(String label,
                                                                   Duration duration,
                                                                   Predicate<Button> predicate) {
        return widgets(Button.class, label,
                duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons. The result function will return a list of any found buttons
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found buttons which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find buttons
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(Duration duration) {
        return widgets(Button.class, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons. The result function will return a list of any found buttons
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found buttons which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find buttons.
     * @param duration is the parameter of a time to find buttons
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(List<String> labels,
                                                                   Duration duration) {
        return widgets(Button.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons. The result function will return a list of any found buttons
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found buttons which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find buttons.
     * @param duration is the parameter of a time to find buttons
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(String label,
                                                                   Duration duration) {
        return widgets(Button.class, label, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(Predicate<Button> predicate) {
        return widgets(Button.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find buttons.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(List<String> labels,
                                                                   Predicate<Button> predicate) {
        return widgets(Button.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find buttons.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(String label,
                                                                   Predicate<Button> predicate) {
        return widgets(Button.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found buttons
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found buttons which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons() {
        return widgets(Button.class);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found buttons
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found buttons which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find buttons.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(List<String> labels) {
        return widgets(Button.class, labels);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found buttons
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found buttons which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find buttons.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Button> buttons(String label) {
        return widgets(Button.class, label);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * @param duration is the parameter of a time to find flags
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(Duration duration,
                                                               Predicate<Flag> predicate) {
        return widgets(Flag.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find flags.
     * @param duration is the parameter of a time to find flags
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(List<String> labels,
                                                                 Duration duration,
                                                                 Predicate<Flag> predicate) {
        return widgets(Flag.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find flags.
     * @param duration is the parameter of a time to find flags
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(String label,
                                                                 Duration duration,
                                                                 Predicate<Flag> predicate) {
        return widgets(Flag.class, label,
                duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags. The result function will return a list of any found flags
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found flags which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find flags
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(Duration duration) {
        return widgets(Flag.class, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags. The result function will return a list of any found flags
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found flags which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find flags.
     * @param duration is the parameter of a time to find flags
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(List<String> labels,
                                                               Duration duration) {
        return widgets(Flag.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags. The result function will return a list of any found flags
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found flags which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find flags.
     * @param duration is the parameter of a time to find flags
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(String label,
                                                               Duration duration) {
        return widgets(Flag.class, label, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(Predicate<Flag> predicate) {
        return widgets(Flag.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the flags) which are used to
     *               find flags.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(List<String> labels,
                                                               Predicate<Flag> predicate) {
        return widgets(Flag.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find flags.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(String label,
                                                               Predicate<Flag> predicate) {
        return widgets(Flag.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found flags
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found flags which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags() {
        return widgets(Flag.class);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found flags
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found flags which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find flags.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(List<String> labels) {
        return widgets(Flag.class, labels);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found flags
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found flags which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find flags.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Flag> flags(String label) {
        return widgets(Flag.class, label);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * @param duration is the parameter of a time to find links
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(Duration duration,
                                                               Predicate<Link> predicate) {
        return widgets(Link.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find links.
     * @param duration is the parameter of a time to find links
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(List<String> labels,
                                                               Duration duration,
                                                               Predicate<Link> predicate) {
        return widgets(Link.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find links.
     * @param duration is the parameter of a time to find links
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(String label,
                                                               Duration duration,
                                                               Predicate<Link> predicate) {
        return widgets(Link.class, label,
                duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links. The result function will return a list of any found links
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found links which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find links
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(Duration duration) {
        return widgets(Link.class, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links. The result function will return a list of any found links
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found links which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the links) which are used to
     *               find links.
     * @param duration is the parameter of a time to find links
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(List<String> labels,
                                                               Duration duration) {
        return widgets(Link.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links. The result function will return a list of any found links
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found links which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find links.
     * @param duration is the parameter of a time to find links
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(String label,
                                                               Duration duration) {
        return widgets(Link.class, label, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(Predicate<Link> predicate) {
        return widgets(Link.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find links.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(List<String> labels,
                                                               Predicate<Link> predicate) {
        return widgets(Link.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find links.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(String label,
                                                               Predicate<Link> predicate) {
        return widgets(Link.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found links
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found links which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links() {
        return widgets(Link.class);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found links
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found links which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find links.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(List<String> labels) {
        return widgets(Link.class, labels);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found links
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found links which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find links.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Link> links(String label) {
        return widgets(Link.class, label);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * @param duration is the parameter of a time to find selects
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(Duration duration,
                                                               Predicate<Select> predicate) {
        return widgets(Select.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find selects.
     * @param duration is the parameter of a time to find selects
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(List<String> labels,
                                                                   Duration duration,
                                                                   Predicate<Select> predicate) {
        return widgets(Select.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find selects.
     * @param duration is the parameter of a time to find selects
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(String label,
                                                                   Duration duration,
                                                                   Predicate<Select> predicate) {
        return widgets(Select.class, label,
                duration, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects. The result function will return a list of any found selects
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found selects which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find selects
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(Duration duration) {
        return widgets(Select.class, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects. The result function will return a list of any found selects
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found selects which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find selects.
     * @param duration is the parameter of a time to find selects
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(List<String> labels,
                                                                   Duration duration) {
        return widgets(Select.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects. The result function will return a list of any found selects
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found selects which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find selects.
     * @param duration is the parameter of a time to find selects
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(String label,
                                                                   Duration duration) {
        return widgets(Select.class, label, duration);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(Predicate<Select> predicate) {
        return widgets(Select.class, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find selects.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(List<String> labels,
                                                                   Predicate<Select> predicate) {
        return widgets(Select.class,
                labels, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find selects.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(String label,
                                                                   Predicate<Select> predicate) {
        return widgets(Select.class,
                label, predicate);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found selects
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found selects which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects() {
        return widgets(Select.class);
    }

    /**
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found selects
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found selects which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find selects.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(List<String> labels) {
        return widgets(Select.class, labels);
    }

    /**selects
     * Returns an instance of {@link SequentialMultipleSearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return a list of any found selects
     * if the property {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return a list of found selects which are visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find selects.
     * @return an instance of {@link SequentialMultipleSearchSupplier}
     */
    public static SequentialMultipleSearchSupplier<Select> selects(String label) {
        return widgets(Select.class, label);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some elements from a parent element.
     * @return self-reference
     */
    public SequentialMultipleSearchSupplier<R> from(SequentialSearchSupplier<? extends SearchContext> from) {
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
