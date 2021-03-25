package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.steps.Criteria;
import ru.tinkoff.qa.neptune.core.api.steps.Description;
import ru.tinkoff.qa.neptune.core.api.steps.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.*;

import java.time.Duration;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.OR;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;

@SuppressWarnings({"unused"})
@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
@SequentialGetStepSupplier.DefaultParameterNames(
        timeOut = "Time of the waiting for elements",
        from = "Parent element",
        criteria = "Element criteria",
        imperative = "Find"
)
public final class MultipleSearchSupplier<R extends SearchContext> extends
        SequentialGetStepSupplier.GetIterableChainedStepSupplier<SearchContext, List<R>, SearchContext, R, MultipleSearchSupplier<R>> {

    private MultipleSearchSupplier(Function<SearchContext, List<R>> originalFunction) {
        super(originalFunction);
        from(searchContext -> searchContext);
        timeOut(ELEMENT_WAITING_DURATION.get());
        addIgnored(StaleElementReferenceException.class);
        if (FIND_ONLY_VISIBLE_ELEMENTS.get()) {
            criteria(visible());
        }
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link MultipleSearchSupplier}
     */
    @Description("List of web elements located {by}")
    public static MultipleSearchSupplier<WebElement> webElements(@DescriptionFragment("by") By by) {
        var webElements = FindWebElements.webElements(by);
        return new MultipleSearchSupplier<>(webElements);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link WebElement} found from the input value.
     *
     * @param by   locator strategy to find elements
     * @param text that desired elements should have
     * @return an instance of {@link MultipleSearchSupplier}
     */
    @Description("Web element located {by} with the text '{text}'")
    public static MultipleSearchSupplier<WebElement> webElements(@DescriptionFragment("by") By by, @DescriptionFragment("text") String text) {
        var shouldHaveText = text(text);
        var webElements = FindWebElements.webElements(by);
        var search = new MultipleSearchSupplier<>(webElements);
        return search.criteria(shouldHaveText);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass      is a class of objects to be returned.
     * @param textOrLabel text which is used to find widgets by full text matching. Also texts of labels
     *                    are used to find widgets.
     * @param <T>         the type of widgets which should be found
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    @Description("List of {tClass} '{textOrLabel}'")
    public static <T extends Widget> MultipleSearchSupplier<T> widgets(@DescriptionFragment(value = "tClass", makeReadableBy = WidgetNameGetter.class) Class<T> tClass,
                                                                       @DescriptionFragment("textOrLabel") String textOrLabel) {
        return new MultipleSearchSupplier<T>(
                FindWidgets.widgets(tClass))
                .criteria(OR(
                        text(textOrLabel),
                        labeled(textOrLabel)
                ));
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of {@link Widget} found from the input value.
     *
     * @param tClass is a class of objects to be returned
     * @param <T>    the type of widgets which should be found
     * @return an instance of {@link MultipleSearchSupplier}
     */
    @Description("List of {tClass}")
    public static <T extends Widget> MultipleSearchSupplier<T> widgets(@DescriptionFragment(value = "tClass",
            makeReadableBy = WidgetNameGetter.class) Class<T> tClass) {
        var widgets = FindWidgets.widgets(tClass);
        return new MultipleSearchSupplier<>(widgets);
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
     * @param textOrLabel text which is used to find buttons by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Button> buttons(String textOrLabel) {
        return widgets(Button.class, textOrLabel);
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
     * @param textOrLabel text which is used to find flags by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Flag> flags(String textOrLabel) {
        return widgets(Flag.class, textOrLabel);
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
     * @param textOrLabel text which is used to find checkboxes by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Flag.CheckBox> checkBoxes(String textOrLabel) {
        return widgets(Flag.CheckBox.class, textOrLabel);
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
     * @param textOrLabel text which is used to find radio-buttons by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Flag.RadioButton> radioButtons(String textOrLabel) {
        return widgets(Flag.RadioButton.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found toggles.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Flag.Toggle> toggles() {
        return widgets(Flag.Toggle.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found toggles.
     *
     * @param textOrLabel text which is used to find toggles by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Flag.Toggle> toggles(String textOrLabel) {
        return widgets(Flag.Toggle.class, textOrLabel);
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
     * @param textOrLabel text which is used to find links by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Link> links(String textOrLabel) {
        return widgets(Link.class, textOrLabel);
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
     * @param textOrLabel text which is used to find selects by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Select> selects(String textOrLabel) {
        return widgets(Select.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found multi-selects.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<MultiSelect> multiSelects() {
        return widgets(MultiSelect.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found multi-selects.
     *
     * @param textOrLabel text which is used to find multi-selects by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<MultiSelect> multiSelects(String textOrLabel) {
        return widgets(MultiSelect.class, textOrLabel);
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
     * @param textOrLabel text which is used to find tabs by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Tab> tabs(String textOrLabel) {
        return widgets(Tab.class, textOrLabel);
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
     * @param textOrLabel text which is used to find text fields by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<TextField> textFields(String textOrLabel) {
        return widgets(TextField.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found calendars.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<CalendarWidget> calendars() {
        return widgets(CalendarWidget.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found calendars.
     *
     * @param textOrLabel text which is used to find calendars by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<CalendarWidget> calendars(String textOrLabel) {
        return widgets(CalendarWidget.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found calendar ranges.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<CalendarRangeWidget> calendarRanges() {
        return widgets(CalendarRangeWidget.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found calendar ranges.
     *
     * @param textOrLabel text which is used to find calendar ranges by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<CalendarRangeWidget> calendarRanges(String textOrLabel) {
        return widgets(CalendarRangeWidget.class, textOrLabel);
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
     * @param textOrLabel text which is used to find tables by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Table> tables(String textOrLabel) {
        return widgets(Table.class, textOrLabel);
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
     * and returns some list of found table rows.
     *
     * @param textOrLabel text which is used to find table rows by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<TableRow> tableRows(String textOrLabel) {
        return widgets(TableRow.class, textOrLabel);
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
     * and returns some list of found table headers.
     *
     * @param textOrLabel text which is used to find table headers by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<TableHeader> tableHeaders(String textOrLabel) {
        return widgets(TableHeader.class, textOrLabel);
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
     * and returns some list of found table footers.
     *
     * @param textOrLabel text which is used to find table footers by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<TableFooter> tableFooters(String textOrLabel) {
        return widgets(TableFooter.class, textOrLabel);
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
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found table cells.
     *
     * @param textOrLabel text which is used to find table cells by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<TableCell> tableCells(String textOrLabel) {
        return widgets(TableCell.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found text elements.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<TextElement> textElements() {
        return widgets(TextElement.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found text elements.
     *
     * @param textOrLabel text which is used to find text elements by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<TextElement> textElements(String textOrLabel) {
        return widgets(TextElement.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found forms.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Form> forms() {
        return widgets(Form.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found forms.
     *
     * @param textOrLabel text which is used to find forms by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Form> forms(String textOrLabel) {
        return widgets(Form.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found grouping elements.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<ElementGroup> groups() {
        return widgets(ElementGroup.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found grouping elements.
     *
     * @param textOrLabel text which is used to find grouping elements by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<ElementGroup> groups(String textOrLabel) {
        return widgets(ElementGroup.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found image elements.
     *
     * @return an instance of {@link MultipleSearchSupplier}
     */
    public static MultipleSearchSupplier<Image> images() {
        return widgets(Image.class);
    }

    /**
     * Returns an instance of {@link MultipleSearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some list of found image elements.
     *
     * @param textOrLabel text which is used to find image elements by full text matching. Also texts of labels
     *                    are used to find elements.
     * @return an instance of {@link MultipleSearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static MultipleSearchSupplier<Image> images(String textOrLabel) {
        return widgets(Image.class, textOrLabel);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is how to find some elements from a parent element.
     * @param <Q>  is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> MultipleSearchSupplier<R> foundFrom(SearchSupplier<Q> from) {
        return super.from(from);
    }

    /**
     * Constructs the chained searching from some instance of {@link SearchContext}.
     *
     * @param from is a parent element.
     * @param <Q>  is a type of the parent element.
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
     * @param <Q>  is a type of the parent element.
     * @return self-reference
     */
    public <Q extends SearchContext> MultipleSearchSupplier<R> foundFrom(Function<SearchContext, Q> from) {
        return super.from(from);
    }

    @Override
    public MultipleSearchSupplier<R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }

    @Override
    public MultipleSearchSupplier<R> criteria(Criteria<? super R> condition) {
        return super.criteria(condition);
    }

    @Override
    public MultipleSearchSupplier<R> criteria(String description, Predicate<? super R> condition) {
        return super.criteria(description, condition);
    }

    @Override
    public MultipleSearchSupplier<R> clone() {
        return super.clone();
    }
}
