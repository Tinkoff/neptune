package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.TurnsRetortingOff;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.*;
import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.regex.Pattern;

import static java.lang.String.join;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.utils.IsLoggableUtil.isLoggable;
import static ru.tinkoff.qa.neptune.selenium.api.widget.Widget.getWidgetName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindLabeledWidgets.labeledWidgets;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;
import static java.lang.String.format;
import static java.util.List.of;
import static org.apache.commons.lang3.StringUtils.EMPTY;

@SuppressWarnings({"unused"})
@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public final class MultipleSearchSupplier<R extends SearchContext> extends
        SequentialGetStepSupplier.GetIterableChainedStepSupplier<SearchContext, List<R>, SearchContext, R, MultipleSearchSupplier<R>> {

    private MultipleSearchSupplier(String description, Function<SearchContext, List<R>> originalFunction) {
        super(description, originalFunction);
        timeOut(ELEMENT_WAITING_DURATION.get());
        addIgnored(of(StaleElementReferenceException.class));
        if (FIND_ONLY_VISIBLE_ELEMENTS_WHEN_NO_CONDITION.get()) {
            criteria(shouldBeVisible());
        }
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param text that desired elements should have
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<WebElement> webElements(By by, String text) {
        Predicate<WebElement> shouldHaveText = shouldHaveText(text);
        ((TurnsRetortingOff<?>) shouldHaveText).turnReportingOff();

        var webElements = FindWebElements.webElements(by);
        var search = new MultipleSearchSupplier<>(format("Web element located %s with the text '%s'", by, text), webElements);
        webElements.setCriteriaDescription(() -> ofNullable(search.condition).map(predicate -> {
            if (isLoggable(predicate)) {
                return format("%s, %s", predicate.toString(), shouldHaveText.toString());
            }
            return shouldHaveText.toString();
        }).orElse(shouldHaveText.toString()));
        return search.criteria(shouldHaveText);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find elements
     * @param textPattern is a regExp to match text of desired elements
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<WebElement> webElements(By by, Pattern textPattern) {
        Predicate<WebElement> shouldHaveText = shouldHaveText(textPattern);
        ((TurnsRetortingOff<?>) shouldHaveText).turnReportingOff();

        var webElements = FindWebElements.webElements(by);
        var search = new MultipleSearchSupplier<>(format("Web element located %s with text that matches the pattern '%s'", by, textPattern), webElements);
        webElements.setCriteriaDescription(() -> ofNullable(search.condition).map(predicate -> {
            if (isLoggable(predicate)) {
                return format("%s, %s", predicate.toString(), shouldHaveText.toString());
            }
            return shouldHaveText.toString();
        }).orElse(shouldHaveText.toString()));
        return search.criteria(shouldHaveText);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<WebElement> webElements(By by) {
        var webElements = FindWebElements.webElements(by);
        var search = new MultipleSearchSupplier<>(format("List of web elements located %s", by), webElements);
        webElements.setCriteriaDescription(() -> ofNullable(search.condition).map(predicate -> {
            if (isLoggable(predicate)) {
                return predicate.toString();
            }
            return EMPTY;
        }).orElse(EMPTY));
        return search;
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of objects to be returned. tClass should have at least one
     *               not abstract subclass that implements {@link Labeled} or be that class.
     * @param labels (texts of some elements or attributes inside or beside the widget) are used to
     *               find widgets.
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static <T extends Widget> MultipleSearchSupplier<T> widgets(Class<T> tClass, List<String> labels) {
        var labeledBy = shouldBeLabeledBy(labels.toArray(new String[]{}));
        ((TurnsRetortingOff<?>) labeledBy).turnReportingOff();
        var labeledWidgets = labeledWidgets(tClass);
        var search =  new MultipleSearchSupplier<>(format("%s '%s'", getWidgetName(tClass), join(",", labels)), labeledWidgets);
        labeledWidgets.setCriteriaDescription(() -> ofNullable(search.condition).map(predicate -> {
            if (isLoggable(predicate)) {
                return format("%s, %s", predicate.toString(), labeledBy.toString());
            }
            return labeledBy.toString();
        }).orElse(labeledBy.toString()));
        return search.criteria(labeledBy);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of objects to be returned. tClass should have at least one
     *               not abstract subclass that implements {@link Labeled} or be that class.
     * @param label (text of some element or attribute inside or beside the widget) is used to
     *               find widgets.
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static <T extends Widget> MultipleSearchSupplier<T> widgets(Class<T> tClass, String label) {
        return widgets(tClass, List.of(label));
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of objects to be returned
     * @param <T> the type of widgets which should be found
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static <T extends Widget> MultipleSearchSupplier<T> widgets(Class<T> tClass) {
        var widgets = FindWidgets.widgets(tClass);
        var search = new MultipleSearchSupplier<>(getWidgetName(tClass), widgets);
        widgets.setCriteriaDescription(() -> ofNullable(search.condition).map(predicate -> {
            if (isLoggable(predicate)) {
                return predicate.toString();
            }
            return EMPTY;
        }).orElse(EMPTY));
        return search;
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Button> buttons() {
        return widgets(Button.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * @param labels (texts of some elements or attributes inside or beside the button) are used to find buttons.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Button> buttons(List<String> labels) {
        return widgets(Button.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found buttons.
     *
     * @param label (text of some element or attribute inside or beside the button) is used to find buttons.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Button> buttons(String label) {
        return widgets(Button.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag> flags() {
        return widgets(Flag.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * @param labels (texts of some elements or attributes inside or beside the flag) are used to
     *               find flags.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag> flags(List<String> labels) {
        return widgets(Flag.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found flags.
     *
     * @param label (text of some element or attribute inside or beside the flag) is used to
     *               find flags.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag> flags(String label) {
        return widgets(Flag.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found check boxes.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.CheckBox> checkBoxes() {
        return widgets(Flag.CheckBox.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found check boxes.
     *
     * @param labels (texts of some elements or attributes inside or beside the check box) are used to
     *               find check boxes.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.CheckBox> checkBoxes(List<String> labels) {
        return widgets(Flag.CheckBox.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found check boxes.
     *
     * @param label (text of some element or attribute inside or beside the check box) is used to
     *               find check boxes.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.CheckBox> checkBoxes(String label) {
        return widgets(Flag.CheckBox.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found radio buttons.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.RadioButton> radioButtons() {
        return widgets(Flag.RadioButton.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found radio buttons.
     *
     * @param labels (texts of some elements or attributes inside or beside the radio button) are used to
     *               find radio buttons.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.RadioButton> radioButtons(List<String> labels) {
        return widgets(Flag.RadioButton.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found radio buttons.
     *
     * @param label (text of some element or attribute inside or beside the radio button) is used to
     *               find radio buttons.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.RadioButton> radioButtons(String label) {
        return widgets(Flag.RadioButton.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Link> links() {
        return widgets(Link.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * @param labels (texts of some elements or attributes inside or beside the link) are used to
     *               find links.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Link> links(List<String> labels) {
        return widgets(Link.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found links.
     *
     * @param label (text of some element or attribute inside or beside the link) is used to
     *               find links.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Link> links(String label) {
        return widgets(Link.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Select> selects() {
        return widgets(Select.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * @param labels (texts of some elements or attributes inside or beside the select) are used to
     *               find selects.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Select> selects(List<String> labels) {
        return widgets(Select.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found selects.
     *
     * @param label (text of some element or attribute inside or beside the select) is used to
     *               find selects.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Select> selects(String label) {
        return widgets(Select.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found tabs.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Tab> tabs() {
        return widgets(Tab.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found tabs.
     *
     * @param labels (texts of some elements or attributes inside or beside the tab) are used to
     *               find tabs.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Tab> tabs(List<String> labels) {
        return widgets(Tab.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found tabs.
     *
     * @param label (text of some element or attribute inside or beside the tab) is used to
     *               find tabs.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Tab> tabs(String label) {
        return widgets(Tab.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found text fields.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TextField> textFields() {
        return widgets(TextField.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found text fields.
     *
     * @param labels (texts of some elements or attributes inside or beside the text field) are used to
     *               find text fields.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TextField> textFields(List<String> labels) {
        return widgets(TextField.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found text fields.
     *
     * @param label (text of some element or attribute inside or beside the text field) is used to
     *               find text fields.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TextField> textFields(String label) {
        return widgets(TextField.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found tables.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Table> tables() {
        return widgets(Table.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found tables.
     *
     * @param labels (texts of some elements or attributes inside or beside the text field) are used to
     *               find tables.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Table> tables(List<String> labels) {
        return widgets(Table.class, labels);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found tables.
     *
     * @param label (text of some element or attribute inside or beside the text field) is used to
     *               find tables.
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Table> tables(String label) {
        return widgets(Table.class, label);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found table rows.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TableRow> tableRows() {
        return widgets(TableRow.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found table headers.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TableHeader> tableHeaders() {
        return widgets(TableHeader.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found table footers.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TableFooter> tableFooters() {
        return widgets(TableFooter.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found table cells.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TableCell> tableCells() {
        return widgets(TableCell.class);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some elements from a parent element.
     * @param <Q> is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> MultipleSearchSupplier<R> foundFrom(SearchSupplier<Q> from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is a parent element.
     * @param <Q> is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> MultipleSearchSupplier<R> foundFrom(Q from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from result of some function applying. This function should take some
     * {@link SearchContext} as the input parameter and return some list of found instances of {@link SearchContext}.
     *
     * @param from is a function which takes some {@link SearchContext} as the input parameter and returns some
     *             list of found instances of {@link SearchContext}.
     * @param <Q> is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext>  MultipleSearchSupplier<R> foundFrom(Function<SearchContext, Q> from) {
        return super.from(from);
    }

    @Override
    public MultipleSearchSupplier<R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public MultipleSearchSupplier<R> criteria(Predicate<? super R> condition) {
        return super.criteria(condition);
    }

    @Override
    public MultipleSearchSupplier<R> criteria(String description, Predicate<? super R> condition) {
        return super.criteria(description, condition);
    }

    protected MultipleSearchSupplier<R> clone() {
        return super.clone();
    }
}
