package com.github.toy.constructor.selenium.functions.searching;

import com.github.toy.constructor.core.api.GetSupplier;
import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.api.widget.drafts.*;
import com.github.toy.constructor.selenium.properties.WaitingProperties;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static com.github.toy.constructor.core.api.ToGetObjectFromIterable.getFromIterable;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.defaultPredicate;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeLabeledBy;
import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldHaveText;
import static com.github.toy.constructor.selenium.functions.searching.FindLabeledWidgets.labeledWidgets;
import static com.github.toy.constructor.selenium.functions.searching.FindWebElements.webElements;
import static com.github.toy.constructor.selenium.functions.searching.FindWidgets.widgets;
import static com.github.toy.constructor.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;
import static java.lang.String.format;

@SuppressWarnings({"unused", "unchecked"})
public final class SearchSupplier<R extends SearchContext> extends GetSupplier<SearchContext, R, SearchSupplier<R>> {

    private SearchSupplier() {
        super();
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link SearchContext} found from the input value.
     *
     * @param transformation is a function which performs the searching from some {@link SearchContext}
     *                       and transform a found item to another instance of {@link SearchContext}
     * @param duration       is the parameter of a time to find desired item
     * @param condition      to specify the searching criteria
     * @param <T>            is a type of a value to be returned by resulted function
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends SearchContext> SearchSupplier<T> item(Function<SearchContext, List<T>> transformation,
                                                                   Duration duration, Predicate<? super T> condition) {
        return new SearchSupplier<T>().set(getFromIterable("A single item", transformation,
                condition, duration, true, true,
                () -> new NoSuchElementException(format("Nothing was found. Attempt to get a single item of %s. Condition: %s",
                        transformation,
                        condition))));
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link SearchContext} found from the input value. About
     * time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param transformation is a function which performs the searching from some {@link SearchContext}
     *                       and transform a found item to another instance of {@link SearchContext}
     * @param condition      to specify the searching criteria
     * @param <T>            is a type of a value to be returned by resulted function
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends SearchContext> SearchSupplier<T> item(Function<SearchContext, List<T>> transformation,
                                                                   Predicate<? super T> condition) {
        return item(transformation, ELEMENT_WAITING_DURATION.get(), condition);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @param duration is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Duration duration, Predicate<? super WebElement> predicate) {
        return item(webElements(by, predicate.toString()), duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @param duration is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, String text, Duration duration,
                                                        Predicate<? super WebElement> predicate) {
        return webElement(by, duration, shouldHaveText(text).and(predicate));
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @param duration is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Pattern textPattern, Duration duration,
                                                        Predicate<? super WebElement> predicate) {
        return webElement(by, duration, shouldHaveText(textPattern).and(predicate));
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param duration is the parameter of a time to find the element
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Duration duration) {
        return webElement(by, duration, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @param duration is the parameter of a time to find the element
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, String text, Duration duration) {
        return webElement(by, text, duration, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. The
     * result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @param duration is the parameter of a time to find the element
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Pattern textPattern, Duration duration) {
        return webElement(by, textPattern, duration, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. About
     * time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param by locator strategy to find an element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Predicate<? super WebElement> predicate) {
        return item(webElements(by, predicate.toString()), predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. About
     * time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, String text, Predicate<? super WebElement> predicate) {
        return webElement(by, shouldHaveText(text).and(predicate));
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value. About
     * time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Pattern textPattern, Predicate<? super WebElement> predicate) {
        return webElement(by, shouldHaveText(textPattern).and(predicate));
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by) {
        return webElement(by, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by locator strategy to find an element
     * @param text which the desired element should have
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by,
                                                        String text) {
        return webElement(by, text, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link WebElement} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found element if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found element which is displayed on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param by by locator strategy to find an element
     * @param textPattern is a regExp to match text of the desired element
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, Pattern textPattern) {
        return webElement(by, textPattern, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param duration is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, Duration duration, Predicate<? super T> predicate) {
        return item(widgets(tClass, predicate.toString()), duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param duration is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, List<String> labels,
                                                              Duration duration, Predicate<? super T> predicate) {
        Predicate<? extends T> labeledBy = shouldBeLabeledBy(labels.toArray(new String[]{}));
        Predicate<? super T> resultPredicate = (Predicate<? super T>) labeledBy.and(predicate);
        return item(labeledWidgets(tClass, resultPredicate.toString()), duration, resultPredicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find the widget.
     * @param duration is the parameter of a time to find the element
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass,
                                                                        String label,
                                                                        Duration duration,
                                                                        Predicate<? super T> predicate) {
        return widget(tClass, List.of(label), duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value. The
     * result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param duration is the parameter of a time to find the element
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass,
                                                                        Duration duration) {
        return widget(tClass, duration, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
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
     * @param duration is the parameter of a time to find the element
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass,
                                                              List<String> labels, Duration duration) {
        return widget(tClass, labels, duration, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
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
     * @param duration is the parameter of a time to find the element
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, String label,
                                                              Duration duration) {
        return widget(tClass, label, duration, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, Predicate<? super T> predicate) {
        return item(widgets(tClass, predicate.toString()), predicate);
    }

    /**return item(widgets(tClass, predicate.toString()), duration, predicate);
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, List<String> labels, Predicate<? super T> predicate) {
        Predicate<? extends T> labeledBy = shouldBeLabeledBy(labels.toArray(new String[]{}));
        Predicate<? super T> resultPredicate = (Predicate<? super T>) labeledBy.and(predicate);
        return item(labeledWidgets(tClass, resultPredicate.toString()), resultPredicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *               find the widget.
     * @param predicate to specify the searching criteria
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, String label, Predicate<? super T> predicate) {
        return widget(tClass, List.of(label), predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param tClass is a class of {@link Widget} which instance should be returned
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass) {
        return widget(tClass, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) which are used to
     *               find the widget.
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, List<String> labels) {
        return widget(tClass, labels, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some instance of {@link Widget} found from the input value.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found widget if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found widget which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param tClass is a class of {@link Widget} which instance should be returned. tClass should have at
     *               least one not abstract subclass which also implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *              find the widget.
     * @param <T> the type of widget which should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, String label) {
        return widget(tClass, label, defaultPredicate());
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param duration is the parameter of a time to find a button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(Duration duration, Predicate<? super Button> predicate) {
        return widget(Button.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @param duration is the parameter of a time to find a button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(List<String> labels, Duration duration, Predicate<? super Button> predicate) {
        return widget(Button.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find a button.
     * @param duration is the parameter of a time to find a button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(String label, Duration duration, Predicate<? super Button> predicate) {
        return widget(Button.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a button
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(Duration duration) {
        return widget(Button.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @param duration is the parameter of a time to find a button
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(List<String> labels, Duration duration) {
        return widget(Button.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find a button.
     * @param duration is the parameter of a time to find a button
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(String label, Duration duration) {
        return widget(Button.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(Predicate<? super Button> predicate) {
        return widget(Button.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(List<String> labels, Predicate<? super Button> predicate) {
        return widget(Button.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the button) which is used to
     *               find a button.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(String label, Predicate<? super Button> predicate) {
        return widget(Button.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button() {
        return widget(Button.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param labels (texts of some elements or attributes inside or beside the button) which are used to
     *               find a button.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(List<String> labels) {
        return widget(Button.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the widget) which is used to
     *              find a button.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(String label) {
        return widget(Button.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param duration is the parameter of a time to find a flag
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(Duration duration, Predicate<? super Flag> predicate) {
        return widget(Flag.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @param duration is the parameter of a time to find a flag
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(List<String> labels, Duration duration, Predicate<? super Flag> predicate) {
        return widget(Flag.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find a flag.
     * @param duration is the parameter of a time to find a flag
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(String label, Duration duration, Predicate<? super Flag> predicate) {
        return widget(Flag.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a flag
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(Duration duration) {
        return widget(Flag.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @param duration is the parameter of a time to find a flag
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(List<String> labels, Duration duration) {
        return widget(Flag.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find a flag.
     * @param duration is the parameter of a time to find a flag
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(String label, Duration duration) {
        return widget(Flag.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(Predicate<? super Flag> predicate) {
        return widget(Flag.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(List<String> labels, Predicate<? super Flag> predicate) {
        return widget(Flag.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *               find a flag.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(String label, Predicate<? super Flag> predicate) {
        return widget(Flag.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag() {
        return widget(Flag.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *

     *
     * @param labels (texts of some elements or attributes inside or beside the flag) which are used to
     *               find a flag.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(List<String> labels) {
        return widget(Flag.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found flag if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found flag which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the flag) which is used to
     *              find a flag.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(String label) {
        return widget(Flag.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box.
     *
     * @param duration is the parameter of a time to find a check box
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(Duration duration, Predicate<Flag.CheckBox> predicate) {
        return widget(Flag.CheckBox.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box.
     *
     * @param labels (texts of some elements or attributes inside or beside the check box) which are used to
     *               find a check box.
     * @param duration is the parameter of a time to find a check box
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(List<String> labels, Duration duration,
                                                         Predicate<Flag.CheckBox> predicate) {
        return widget(Flag.CheckBox.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box.
     *
     * @param label (text of some element or attribute inside or beside the check box) which is used to
     *               find a check box.
     * @param duration is the parameter of a time to find a check box
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(String label, Duration duration,
                                                         Predicate<Flag.CheckBox> predicate) {
        return widget(Flag.CheckBox.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box. The result function will return the first found check box if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found check box which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a check box
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(Duration duration) {
        return widget(Flag.CheckBox.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box. The result function will return the first found check box if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found check box which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the check box) which are used to
     *               find a check box.
     * @param duration is the parameter of a time to find a check box
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(List<String> labels, Duration duration) {
        return widget(Flag.CheckBox.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box. The result function will return the first found check box if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found check box which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the check box) which is used to
     *               find a check box.
     * @param duration is the parameter of a time to find a check box
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(String label, Duration duration) {
        return widget(Flag.CheckBox.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(Predicate<Flag.CheckBox> predicate) {
        return widget(Flag.CheckBox.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the check box) which are used to
     *               find a check box.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(List<String> labels, Predicate<Flag.CheckBox> predicate) {
        return widget(Flag.CheckBox.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some check box. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the check box) which is used to
     *               find a check box.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(String label, Predicate<Flag.CheckBox> predicate) {
        return widget(Flag.CheckBox.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some checkbox.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found checkbox if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found checkbox which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox() {
        return widget(Flag.CheckBox.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some checkbox.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found checkbox if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found checkbox which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *

     *
     * @param labels (texts of some elements or attributes inside or beside the checkbox) which are used to
     *               find a checkbox.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(List<String> labels) {
        return widget(Flag.CheckBox.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some checkbox.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found checkbox if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found checkbox which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the checkbox) which is used to
     *              find a checkbox.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(String label) {
        return widget(Flag.CheckBox.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * @param duration is the parameter of a time to find a radio button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(Duration duration, Predicate<Flag.RadioButton> predicate) {
        return widget(Flag.RadioButton.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * @param labels (texts of some elements or attributes inside or beside the radio button) which are used to
     *               find a radio button.
     * @param duration is the parameter of a time to find a radio button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(List<String> labels, Duration duration,
                                                               Predicate<Flag.RadioButton> predicate) {
        return widget(Flag.RadioButton.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * @param label (text of some element or attribute inside or beside the radio button) which is used to
     *               find a radio button.
     * @param duration is the parameter of a time to find a radio button
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(String label, Duration duration,
                                                               Predicate<Flag.RadioButton> predicate) {
        return widget(Flag.RadioButton.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button. The result function will return the first found radio button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found radio button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a radio button
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(Duration duration) {
        return widget(Flag.RadioButton.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button. The result function will return the first found radio button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found radio button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the radio button) which are used to
     *               find a radio button.
     * @param duration is the parameter of a time to find a radio button
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(List<String> labels, Duration duration) {
        return widget(Flag.RadioButton.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button. The result function will return the first found radio button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found radio button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the radio button) which is used to
     *               find a radio button.
     * @param duration is the parameter of a time to find a radio button
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(String label, Duration duration) {
        return widget(Flag.RadioButton.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(Predicate<Flag.RadioButton> predicate) {
        return widget(Flag.RadioButton.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the radio button) which are used to
     *               find a radio button.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(List<String> labels, Predicate<Flag.RadioButton> predicate) {
        return widget(Flag.RadioButton.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the radio button) which is used to
     *               find a radio button.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(String label, Predicate<Flag.RadioButton> predicate) {
        return widget(Flag.RadioButton.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found radio button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found radio button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton() {
        return widget(Flag.RadioButton.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found radio button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found radio button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *

     *
     * @param labels (texts of some elements or attributes inside or beside the radio button) which are used to
     *               find a radio button.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(List<String> labels) {
        return widget(Flag.RadioButton.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found radio button if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found radio button which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the radio button) which is used to
     *              find a radio button.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(String label) {
        return widget(Flag.RadioButton.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param duration is the parameter of a time to find a link
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(Duration duration, Predicate<? super Link> predicate) {
        return widget(Link.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @param duration is the parameter of a time to find a link
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(List<String> labels, Duration duration, Predicate<? super Link> predicate) {
        return widget(Link.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find a link.
     * @param duration is the parameter of a time to find a link
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(String label, Duration duration, Predicate<? super Link> predicate) {
        return widget(Link.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a link
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(Duration duration) {
        return widget(Link.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @param duration is the parameter of a time to find a link
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(List<String> labels, Duration duration) {
        return widget(Link.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find a link.
     * @param duration is the parameter of a time to find a link
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(String label, Duration duration) {
        return widget(Link.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(Predicate<? super Link> predicate) {
        return widget(Link.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(List<String> labels, Predicate<? super Link> predicate) {
        return widget(Link.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *               find a link.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(String label, Predicate<? super Link> predicate) {
        return widget(Link.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link() {
        return widget(Link.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param labels (texts of some elements or attributes inside or beside the link) which are used to
     *               find a link.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(List<String> labels) {
        return widget(Link.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found link if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found link which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the link) which is used to
     *              find a link.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(String label) {
        return widget(Link.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * @param duration is the parameter of a time to find a select
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(Duration duration, Predicate<? super Select> predicate) {
        return widget(Select.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find a select.
     * @param duration is the parameter of a time to find a select
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(List<String> labels, Duration duration, Predicate<? super Select> predicate) {
        return widget(Select.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find a select.
     * @param duration is the parameter of a time to find a select
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(String label, Duration duration, Predicate<? super Select> predicate) {
        return widget(Select.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select. The result function will return the first found select if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found select which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a select
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(Duration duration) {
        return widget(Select.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select. The result function will return the first found select if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found select which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find a select.
     * @param duration is the parameter of a time to find a select
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(List<String> labels, Duration duration) {
        return widget(Select.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select. The result function will return the first found select if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found select which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find a select.
     * @param duration is the parameter of a time to find a select
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(String label, Duration duration) {
        return widget(Select.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(Predicate<? super Select> predicate) {
        return widget(Select.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find a select.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(List<String> labels, Predicate<? super Select> predicate) {
        return widget(Select.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *               find a select.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(String label, Predicate<? super Select> predicate) {
        return widget(Select.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found select if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found select which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select() {
        return widget(Select.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found select if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found select which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param labels (texts of some elements or attributes inside or beside the select) which are used to
     *               find a select.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(List<String> labels) {
        return widget(Select.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found select if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found select which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the select) which is used to
     *              find a select.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(String label) {
        return widget(Select.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * @param duration is the parameter of a time to find a tab
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(Duration duration, Predicate<? super Tab> predicate) {
        return widget(Tab.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * @param labels (texts of some elements or attributes inside or beside the tab) which are used to
     *               find a tab.
     * @param duration is the parameter of a time to find a tab
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(List<String> labels, Duration duration, Predicate<? super Tab> predicate) {
        return widget(Tab.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * @param label (text of some element or attribute inside or beside the tab) which is used to
     *               find a tab.
     * @param duration is the parameter of a time to find a tab
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(String label, Duration duration, Predicate<? super Tab> predicate) {
        return widget(Tab.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab. The result function will return the first found tab if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found tab which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a tab
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(Duration duration) {
        return widget(Tab.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab. The result function will return the first found tab if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found tab which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the tab) which are used to
     *               find a tab.
     * @param duration is the parameter of a time to find a tab
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(List<String> labels, Duration duration) {
        return widget(Tab.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab. The result function will return the first found tab if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found tab which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the tab) which is used to
     *               find a tab.
     * @param duration is the parameter of a time to find a tab
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(String label, Duration duration) {
        return widget(Tab.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(Predicate<? super Tab> predicate) {
        return widget(Tab.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the tab) which are used to
     *               find a tab.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(List<String> labels, Predicate<? super Tab> predicate) {
        return widget(Tab.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the tab) which is used to
     *               find a tab.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(String label, Predicate<? super Tab> predicate) {
        return widget(Tab.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found tab if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found tab which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab() {
        return widget(Tab.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found tab if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found tab which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param labels (texts of some elements or attributes inside or beside the tab) which are used to
     *               find a tab.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(List<String> labels) {
        return widget(Tab.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found tab if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found tab which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the tab) which is used to
     *              find a tab.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(String label) {
        return widget(Tab.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field.
     *
     * @param duration is the parameter of a time to find a text field
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(Duration duration, Predicate<? super TextField> predicate) {
        return widget(TextField.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field.
     *
     * @param labels (texts of some elements or attributes inside or beside the text field) which are used to
     *               find a text field.
     * @param duration is the parameter of a time to find a text field
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(List<String> labels, Duration duration, Predicate<? super TextField> predicate) {
        return widget(TextField.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field.
     *
     * @param label (text of some element or attribute inside or beside the text field) which is used to
     *               find a text field.
     * @param duration is the parameter of a time to find a text field
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(String label, Duration duration, Predicate<? super TextField> predicate) {
        return widget(TextField.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field. The result function will return the first found text field if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found text field which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a text field
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(Duration duration) {
        return widget(TextField.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field. The result function will return the first found text field if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found text field which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the text field) which are used to
     *               find a text field.
     * @param duration is the parameter of a time to find a text field
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(List<String> labels, Duration duration) {
        return widget(TextField.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field. The result function will return the first found text field if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found text field which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the text field) which is used to
     *               find a text field.
     * @param duration is the parameter of a time to find a text field
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(String label, Duration duration) {
        return widget(TextField.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(Predicate<? super TextField> predicate) {
        return widget(TextField.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the text field) which are used to
     *               find a text field.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(List<String> labels, Predicate<? super TextField> predicate) {
        return widget(TextField.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some text field. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the text field) which is used to
     *               find a table.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(String label, Predicate<? super TextField> predicate) {
        return widget(TextField.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField() {
        return widget(TextField.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param labels (texts of some elements or attributes inside or beside the table) which are used to
     *               find a table.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(List<String> labels) {
        return widget(TextField.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the table) which is used to
     *              find a table.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(String label) {
        return widget(TextField.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * @param duration is the parameter of a time to find a table
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(Duration duration, Predicate<? super Table> predicate) {
        return widget(Table.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * @param labels (texts of some elements or attributes inside or beside the table) which are used to
     *               find a table.
     * @param duration is the parameter of a time to find a table
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(List<String> labels, Duration duration, Predicate<? super Table> predicate) {
        return widget(Table.class, labels, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * @param label (text of some element or attribute inside or beside the table) which is used to
     *               find a table.
     * @param duration is the parameter of a time to find a table
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(String label, Duration duration, Predicate<? super Table> predicate) {
        return widget(Table.class, label, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table. The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a table
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(Duration duration) {
        return widget(Table.class, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table. The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param labels (texts of some elements or attributes inside or beside the table) which are used to
     *               find a table.
     * @param duration is the parameter of a time to find a table
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(List<String> labels, Duration duration) {
        return widget(Table.class, labels, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table. The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param label (text of some element or attribute inside or beside the table) which is used to
     *               find a table.
     * @param duration is the parameter of a time to find a table
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(String label, Duration duration) {
        return widget(Table.class, label, duration);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(Predicate<? super Table> predicate) {
        return widget(Table.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param labels (texts of some elements or attributes inside or beside the table) which are used to
     *               find a table.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(List<String> labels, Predicate<? super Table> predicate) {
        return widget(Table.class, labels, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param label (text of some element or attribute inside or beside the table) which is used to
     *               find a table.
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(String label, Predicate<? super Table> predicate) {
        return widget(Table.class, label, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table() {
        return widget(Table.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param labels (texts of some elements or attributes inside or beside the table) which are used to
     *               find a table.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(List<String> labels) {
        return widget(Table.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @param label (text of some element or attribute inside or beside the table) which is used to
     *              find a table.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(String label) {
        return widget(Table.class, label);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table row.
     *
     * @param duration is the parameter of a time to find a table row
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableRow> tableRow(Duration duration, Predicate<? super TableRow> predicate) {
        return widget(TableRow.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found table row. The result function will return the first found table row if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table row which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a table row
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableRow> tableRow(Duration duration) {
        return widget(TableRow.class, duration);
    }


    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table row. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableRow> tableRow(Predicate<? super TableRow> predicate) {
        return widget(TableRow.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found table row.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table row if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table row which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableRow> tableRow() {
        return widget(TableRow.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table header.
     *
     * @param duration is the parameter of a time to find a table header
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableHeader> tableHeader(Duration duration, Predicate<? super TableHeader> predicate) {
        return widget(TableHeader.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found  table header. The result function will return the first found table header if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table header which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a table header
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableHeader> tableHeader(Duration duration) {
        return widget(TableHeader.class, duration);
    }


    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table header. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableHeader> tableHeader(Predicate<? super TableHeader> predicate) {
        return widget(TableHeader.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found  table header.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table header if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table header which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableHeader> tableHeader() {
        return widget(TableHeader.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table footer.
     *
     * @param duration is the parameter of a time to find a table footer
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableFooter> tableFooter(Duration duration, Predicate<? super TableFooter> predicate) {
        return widget(TableFooter.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found table footer. The result function will return the first found table footer if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table footer which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a table footer
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableFooter> tableFooter(Duration duration) {
        return widget(TableFooter.class, duration);
    }


    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table footer. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableFooter> tableFooter(Predicate<? super TableFooter> predicate) {
        return widget(TableFooter.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found table footer.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table footer if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table footer which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableFooter> tableFooter() {
        return widget(TableFooter.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table cell.
     *
     * @param duration is the parameter of a time to find a table cell
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableCell> tableCell(Duration duration, Predicate<? super TableCell> predicate) {
        return widget(TableCell.class, duration, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found table cell. The result function will return the first found table cell if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table cell which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION
     *
     * @param duration is the parameter of a time to find a table cell
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableCell> tableCell(Duration duration) {
        return widget(TableCell.class, duration);
    }


    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns some table cell. About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * @param predicate to specify the searching criteria
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableCell> tableCell(Predicate<? super TableCell> predicate) {
        return widget(TableCell.class, predicate);
    }

    /**
     * Returns an instance of {@link SearchSupplier} which wraps a function.
     * The wrapped function takes an instance of {@link SearchContext} for the searching
     * and returns the first found table cell.
     *
     * About time which the searching takes
     * @see WaitingProperties#ELEMENT_WAITING_DURATION
     *
     * The result function will return the first found table cell if the property
     * {@code find.only.visible.elements.when.no.condition} is not defined or has value {@code "false"}.
     * Otherwise it will return the first found table cell which is visible on a page.
     * @see com.github.toy.constructor.selenium.properties.FlagProperties#FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION     *
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableCell> tableCell() {
        return widget(TableCell.class);
    }
}
