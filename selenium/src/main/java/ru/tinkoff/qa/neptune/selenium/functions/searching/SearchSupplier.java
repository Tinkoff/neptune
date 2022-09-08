package ru.tinkoff.qa.neptune.selenium.functions.searching;

import org.openqa.selenium.*;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnFailure;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotations.CaptureOnSuccess;
import ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.MaxDepthOfReporting;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.ThrowWhenNoData;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.*;
import ru.tinkoff.qa.neptune.selenium.captors.WebDriverImageCaptor;
import ru.tinkoff.qa.neptune.selenium.captors.WebElementImageCaptor;

import java.time.Duration;
import java.util.function.Function;

import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.OR;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindWebElements.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.FindWidgets.widgets;
import static ru.tinkoff.qa.neptune.selenium.properties.SessionFlagProperties.FIND_ONLY_VISIBLE_ELEMENTS;
import static ru.tinkoff.qa.neptune.selenium.properties.WaitingProperties.ELEMENT_WAITING_DURATION;

@SuppressWarnings({"unused"})
@SequentialGetStepSupplier.DefineTimeOutParameterName("Time of the waiting for the element")
@SequentialGetStepSupplier.DefineFromParameterName("Parent element")
@SequentialGetStepSupplier.DefineCriteriaParameterName("Element criteria")
@SequentialGetStepSupplier.DefineGetImperativeParameterName(value = "Find:")
@SequentialGetStepSupplier.DefineResultDescriptionParameterName("Found element")
@MaxDepthOfReporting(0)
@CaptureOnSuccess(by = WebElementImageCaptor.class)
@CaptureOnFailure(by = {WebDriverImageCaptor.class, WebElementImageCaptor.class})
@ThrowWhenNoData(toThrow = NoSuchElementException.class, startDescription = "Not found:")
public final class SearchSupplier<R extends SearchContext>
        extends SequentialGetStepSupplier.GetObjectFromIterableChainedStepSupplier<Object, R, SearchContext, SearchSupplier<R>> {

    private <S extends Iterable<R>> SearchSupplier(Function<SearchContext, S> originalFunction) {
        super(originalFunction);
        from(new SearchingInitialFunction());
        timeOut(ELEMENT_WAITING_DURATION.get());
        addIgnored(StaleElementReferenceException.class);
        if (FIND_ONLY_VISIBLE_ELEMENTS.get()) {
            criteria(visible());
        }
        throwOnNoResult();
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some {@link WebElement} found from the input value.
     *
     * @param by locator strategy to find an element
     * @return an instance of {@link SearchSupplier}
     */
    @Description("Web element located '{by}'")
    public static SearchSupplier<WebElement> webElement(@DescriptionFragment("by") By by) {
        var webElements = webElements(by);
        return new SearchSupplier<>(webElements);
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
    @Description("Web element located '{by}' with text '{text}'")
    public static SearchSupplier<WebElement> webElement(@DescriptionFragment("by") By by,
                                                        @DescriptionFragment("text") String text) {
        var shouldHaveText = text(text);
        var webElements = webElements(by);
        var search = new SearchSupplier<>(webElements);
        return search.criteria(shouldHaveText);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some {@link Widget} found from the input value.
     *
     * @param tClass      is a class of an object to be returned
     * @param textOrLabel text which is used to find a widget by full text matching. Also texts of labels
     *                    are used to find a widget.
     * @param <T>         the type of widget that should be found
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    @Description("{widgetClass} '{textOrLabel}'")
    public static <T extends Widget> SearchSupplier<T> widget(
            @DescriptionFragment(
                    value = "widgetClass",
                    makeReadableBy = WidgetDescriptionValueGetter.class)
                    Class<T> tClass,
            @DescriptionFragment("textOrLabel") String textOrLabel) {
        return new SearchSupplier<>(widgets(tClass))
                .criteria(OR(
                        text(textOrLabel),
                        labeled(textOrLabel)
                ));
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
    @Description("{tClass}")
    public static <T extends Widget> SearchSupplier<T> widget(
            @DescriptionFragment(
                    value = "tClass",
                    makeReadableBy = WidgetDescriptionValueGetter.class) Class<T> tClass) {
        var widgets = widgets(tClass);
        return new SearchSupplier<>(widgets);
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
     * @param textOrLabel text which is used to find a button by full text matching. Also texts of labels
     *                    are used to find a button.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Button> button(String textOrLabel) {
        return widget(Button.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a flag by full text matching. Also texts of labels
     *                    are used to find a flag.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Flag> flag(String textOrLabel) {
        return widget(Flag.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a checkbox by full text matching. Also texts of labels
     *                    are used to find a checkbox.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Flag.CheckBox> checkbox(String textOrLabel) {
        return widget(Flag.CheckBox.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a radio-button by full text matching. Also texts of labels
     *                    are used to find a radio-button.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Flag.RadioButton> radioButton(String textOrLabel) {
        return widget(Flag.RadioButton.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some toggle.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Flag.Toggle> toggle() {
        return widget(Flag.Toggle.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some toggle.
     *
     * @param textOrLabel text which is used to find a toggle by full text matching. Also texts of labels
     *                    are used to find a toggle.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Flag.Toggle> toggle(String textOrLabel) {
        return widget(Flag.Toggle.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a link by full text matching. Also texts of labels
     *                    are used to find a link.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Link> link(String textOrLabel) {
        return widget(Link.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a select by full text matching. Also texts of labels
     *                    are used to find a select.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Select> select(String textOrLabel) {
        return widget(Select.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some multi-select.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<MultiSelect> multiSelect() {
        return widget(MultiSelect.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some multi-select.
     *
     * @param textOrLabel text which is used to find a multi-select by full text matching. Also texts of labels
     *                    are used to find a multi-select.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<MultiSelect> multiSelect(String textOrLabel) {
        return widget(MultiSelect.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a tab by full text matching. Also texts of labels
     *                    are used to find a tab.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Tab> tab(String textOrLabel) {
        return widget(Tab.class, textOrLabel);
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
     * @param textOrLabel text which is used to find a text field by full text matching. Also texts of labels
     *                    are used to find a text field.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<TextField> textField(String textOrLabel) {
        return widget(TextField.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some calendar.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<CalendarWidget> calendar() {
        return widget(CalendarWidget.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some calendar.
     *
     * @param textOrLabel text which is used to find a calendar by full text matching. Also texts of labels
     *                    are used to find a calendar.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<CalendarWidget> calendar(String textOrLabel) {
        return widget(CalendarWidget.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some calendar range.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<CalendarRangeWidget> calendarRange() {
        return widget(CalendarRangeWidget.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some calendar range.
     *
     * @param textOrLabel text which is used to find a calendar range by full text matching. Also texts of labels
     *                    are used to find a calendar range.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<CalendarRangeWidget> calendarRange(String textOrLabel) {
        return widget(CalendarRangeWidget.class, textOrLabel);
    }


    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table.
     *
     * @param textOrLabel text which is used to find a table by full text matching. Also texts of labels
     *                    are used to find a table.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Table> table(String textOrLabel) {
        return widget(Table.class, textOrLabel);
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
     * and returns some table row.
     *
     * @param textOrLabel text which is used to find a table row by full text matching. Also texts of labels
     *                    are used to find a table row.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<TableRow> tableRow(String textOrLabel) {
        return widget(TableRow.class, textOrLabel);
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
     * and returns some table header.
     *
     * @param textOrLabel text which is used to find a table header by full text matching. Also texts of labels
     *                    are used to find a table header.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<TableHeader> tableHeader(String textOrLabel) {
        return widget(TableHeader.class, textOrLabel);
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
     * and returns some table footer.
     *
     * @param textOrLabel text which is used to find a table footer by full text matching. Also texts of labels
     *                    are used to find a table footer.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<TableFooter> tableFooter(String textOrLabel) {
        return widget(TableFooter.class, textOrLabel);
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
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some table cell.
     *
     * @param textOrLabel text which is used to find a table cell by full text matching. Also texts of labels
     *                    are used to find a table cell.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<TableCell> tableCell(String textOrLabel) {
        return widget(TableCell.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some text element.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextElement> textElement() {
        return widget(TextElement.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some text element.
     *
     * @param textOrLabel text which is used to find a text element by full text matching. Also texts of labels
     *                    are used to find a text element.
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<TextElement> textElement(String textOrLabel) {
        return widget(TextElement.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some form.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Form> form() {
        return widget(Form.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some form.
     *
     * @param textOrLabel text which is used to find a form by full text matching. Also texts of labels
     *                    are used to find a form.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Form> form(String textOrLabel) {
        return widget(Form.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some grouping element.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<ElementGroup> group() {
        return widget(ElementGroup.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some grouping element.
     *
     * @param textOrLabel text which is used to find a form by full text matching. Also texts of labels
     *                    are used to find a grouping element.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<ElementGroup> group(String textOrLabel) {
        return widget(ElementGroup.class, textOrLabel);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some image element.
     *
     * @return an instance of {@link SearchSupplier}
     */
    public static SearchSupplier<Image> image() {
        return widget(Image.class);
    }

    /**
     * Returns an instance of {@link SearchSupplier} that builds and supplies a function.
     * The built function takes an instance of {@link SearchContext} for the searching
     * and returns some image element.
     *
     * @param textOrLabel text which is used to find a form by full text matching. Also texts of labels
     *                    are used to find an image element.
     * @return an instance of {@link SearchSupplier}
     * @see ru.tinkoff.qa.neptune.selenium.api.widget.Label
     */
    public static SearchSupplier<Image> image(String textOrLabel) {
        return widget(Image.class, textOrLabel);
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

    @Override
    public SearchSupplier<R> timeOut(Duration timeOut) {
        return super.timeOut(timeOut);
    }
}
