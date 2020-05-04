package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.*;

import java.time.Duration;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

import static java.lang.String.format;
import static java.lang.String.join;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Widget.getWidgetName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindLabeledWidgets.labeledWidgets;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindWebElements.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindWidgets.widgets;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialGetStepSupplier.DefaultParameterNames(
        timeOut = "Time of the waiting for the element",
        from = "Parent element",
        criteria = "Element criteria"
)
public final class SearchSupplier<R extends SearchContext>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<SearchContext, R, SearchContext, SearchSupplier<R>> {

    private <S extends Iterable<R>> SearchSupplier(String description, Function<SearchContext, S> originalFunction) {
        super(description, originalFunction);
        from(searchContext -> searchContext);
        timeOut(ELEMENT_WAITING_DURATION.get());
        addIgnored(StaleElementReferenceException.class);
        if (FIND_ONLY_VISIBLE_ELEMENTS.get()) {
            criteria(visible());
        }
        throwOnEmptyResult(noSuchElementException(this));
    }

    private static Supplier<NoSuchElementException> noSuchElementException(SearchSupplier<?> supplier) {
        return () -> {
            var description = format("Nothing was found. Attempt to get %s", supplier.toString());
            var exceptionText = ofNullable(supplier.getCriteria())
                    .map(c -> format("%s. Criteria: %s", description, c.toString()))
                    .orElse(description);
            return new NoSuchElementException(exceptionText);
        };
    }


    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by) {
        var webElements = webElements(by);
        return new SearchSupplier<>(format("Web element located %s", by), webElements);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some {@link WebElement} found from the input value.
     *
     * @param by   locator strategy to find an element
     * @param text that the desired element should have
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<WebElement> webElement(By by, String text) {
        var shouldHaveText = text(text);
        var webElements = webElements(by);
        var search = new SearchSupplier<>(format("Web element located %s", by), webElements);
        return search.criteria(shouldHaveText);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some {@link Widget} found from the input value.
     *
     * @param tClass is a class of an object to be returned. tClass should have at
     *               least one not abstract subclass that implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) are used to
     *               find the widget.
     * @param <T>    the type of widget that should be found
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass, String... labels) {
        var labeledBy = labeled(labels);
        var labeledWidgets = labeledWidgets(tClass);
        var search = new SearchSupplier<>(format("%s '%s'", getWidgetName(tClass), join(", ", labels)), labeledWidgets);
        return search.criteria(labeledBy);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some {@link Widget} found from the input value.
     *
     * @param tClass is a class of an object to be returned
     * @param <T>    the type of widget to find
     * @return an instance of {@link SearchSupplier}
     */
    public static <T extends Widget> SearchSupplier<T> widget(Class<T> tClass) {
        var widgets = widgets(tClass);
        return new SearchSupplier<>(getWidgetName(tClass), widgets);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button() {
        return widget(Button.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some button.
     *
     * @param labels (texts of some elements or attributes inside or beside the button) are used to
     *               find a button.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Button> button(String... labels) {
        return widget(Button.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag() {
        return widget(Flag.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some flag.
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) are used to
     *               find a flag.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag> flag(String... labels) {
        return widget(Flag.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some check box.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox() {
        return widget(Flag.CheckBox.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some check box.
     *
     * @param labels (texts of some elements or attributes inside or beside the check box) are used to
     *               find a check box.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(String... labels) {
        return widget(Flag.CheckBox.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton() {
        return widget(Flag.RadioButton.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some radio button.
     *
     * @param labels (texts of some elements or attributes inside or beside the radio button) are used to
     *               find a radio button.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(String... labels) {
        return widget(Flag.RadioButton.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link() {
        return widget(Link.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some link.
     *
     * @param labels (texts of some elements or attributes inside or beside the link) are used to
     *               find a link.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Link> link(String... labels) {
        return widget(Link.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select() {
        return widget(Select.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some select.
     *
     * @param labels (texts of some elements or attributes inside or beside the select) are used to
     *               find a select.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Select> select(String... labels) {
        return widget(Select.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab() {
        return widget(Tab.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some tab.
     *
     * @param labels (texts of some elements or attributes inside or beside the tab) are used to
     *               find a tab.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Tab> tab(String... labels) {
        return widget(Tab.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some text field.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField() {
        return widget(TextField.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some text field.
     *
     * @param labels (texts of some elements or attributes inside or beside the text field) are used to
     *               find a text field.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextField> textField(String... labels) {
        return widget(TextField.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * @param labels (texts of some elements or attributes inside or beside the table) are used to
     *               find a table.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table(String... labels) {
        return widget(Table.class, labels);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Table> table() {
        return widget(Table.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table row.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableRow> tableRow() {
        return widget(TableRow.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table header.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableHeader> tableHeader() {
        return widget(TableHeader.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table footer.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableFooter> tableFooter() {
        return widget(TableFooter.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table cell.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TableCell> tableCell() {
        return widget(TableCell.class);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is a parent element.
     * @param <Q>  is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> SearchSupplier<R> foundFrom(SearchSupplier<Q> from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching for some element.
     *
     * @param from is a parent element.
     * @param <Q>  is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> SearchSupplier<R> foundFrom(Q from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from result of some function applying. This function should take some
     * {@link SearchContext} as the input parameter and return some found instance of {@link SearchContext}.
     *
     * @param from is a function that takes some {@link SearchContext} as the input parameter and returns some
     *             found instance of {@link SearchContext}.
     * @param <Q>  is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> SearchSupplier<R> foundFrom(Function<SearchContext, Q> from) {
        return super.from(from);
    }

    @Override
    public SearchSupplier<R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public SearchSupplier<R> criteria(Criteria<? super R> condition) {
        return super.criteria(condition);
    }

    @Override
    public SearchSupplier<R> criteria(String description, Predicate<? super R> condition) {
        return super.criteria(description, condition);
    }

    @Override
    public SearchSupplier<R> clone() {
        return super.clone();
    }
}
