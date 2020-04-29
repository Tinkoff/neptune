package ru.tinkoff.qa.neptune.selenium.test.elements.scrolling;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebElement;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.text.field.TextArea;

import java.util.List;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.edit.EditActionSupplier.valueOfThe;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TABLE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TH;

public class ScrollIntoViewTest extends BaseWebDriverTest {

    @Test
    public void scrollWebElementIntoView() {
        var table = seleniumSteps.find(webElement(tagName(TABLE)));

        var tableScrollCount = ((MockWebElement) table).getScrollCount();
        table.getClass();
        assertThat(((MockWebElement) table).getScrollCount(), is(tableScrollCount));

        var th = seleniumSteps.find(webElement(tagName(TH)).foundFrom(table));
        assertThat(((MockWebElement) table).getScrollCount(), is(tableScrollCount));

        var thScrollCount = ((MockWebElement) th).getScrollCount();
        seleniumSteps.click(on(th));
        seleniumSteps.click(on(table));
        assertThat(((MockWebElement) th).getScrollCount(), is(thScrollCount + 1));
        assertThat(((MockWebElement) table).getScrollCount(), is(tableScrollCount + 1));
    }

    @Test
    public void scrollElementInsideWidgetIntoView() {
        var table = seleniumSteps.find(table()
                .criteria(CommonElementCriteria.nested(buttons()
                        .timeOut(ofSeconds(1)))));
        var tableScrollCount = ((MockWebElement) table.getWrappedElement()).getScrollCount();

        table.getClass();
        assertThat(((MockWebElement) table.getWrappedElement()).getScrollCount(), is(tableScrollCount));

        var button = seleniumSteps.find(button().foundFrom(table));
        assertThat(((MockWebElement) table.getWrappedElement()).getScrollCount(), is(tableScrollCount));

        var buttonScrollCount = ((MockWebElement) button.getWrappedElement()).getScrollCount();
        seleniumSteps.click(on(button));
        assertThat(((MockWebElement) button.getWrappedElement()).getScrollCount(), is(buttonScrollCount));
    }

    @Test
    public void scrollScrollableWidgetIntoView() {
        var button = seleniumSteps.find(widget(CustomizedButton.class));
        var textArea = seleniumSteps.find(widget(TextArea.class));

        int buttonScrolls = button.getScrollCount();
        int textAreaScrolls = textArea.getScrollCount();

        seleniumSteps.click(on(button));
        seleniumSteps.edit(valueOfThe(textArea, List.of("1", "2")));

        assertThat(button.getScrollCount(), is(buttonScrolls + 1));
        assertThat(textArea.getScrollCount(), is(textAreaScrolls + 1));
    }
}
