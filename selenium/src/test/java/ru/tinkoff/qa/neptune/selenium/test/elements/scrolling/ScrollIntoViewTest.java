package ru.tinkoff.qa.neptune.selenium.test.elements.scrolling;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockScrollWebElementIntoView;
import ru.tinkoff.qa.neptune.selenium.test.MockWebElement;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.text.field.TextArea;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.properties.DefaultScrollerProperty.DEFAULT_SCROLLER_PROPERTY;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TABLE;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TH;

public class ScrollIntoViewTest extends BaseWebDriverTest {

    @BeforeClass
    public static void prepare() {
        DEFAULT_SCROLLER_PROPERTY.accept(MockScrollWebElementIntoView.class.getName());
    }

    @Test
    public void scrollWebElementIntoView() {
        var table = seleniumSteps.find(webElement(tagName(TABLE)));

        var tableScrollCount = ((MockWebElement) table).getScrollCount();
        assertThat(((MockWebElement) table).getScrollCount(), is(tableScrollCount));

        var th = seleniumSteps.find(webElement(tagName(TH)).foundFrom(table));
        assertThat(((MockWebElement) table).getScrollCount(), is(tableScrollCount));

        var thScrollCount = ((MockWebElement) th).getScrollCount();
        seleniumSteps.click(th);
        seleniumSteps.click(table);
        assertThat(((MockWebElement) th).getScrollCount(), is(thScrollCount + 1));
        assertThat(((MockWebElement) table).getScrollCount(), is(tableScrollCount + 1));
    }

    @Test
    public void scrollElementInsideWidgetIntoView() {
        var table = seleniumSteps.find(table()
                .criteria(CommonElementCriteria.nested(buttons()
                        .timeOut(ofSeconds(1)))));
        var tableScrollCount = ((MockWebElement) table.getWrappedElement()).getScrollCount();
        assertThat(((MockWebElement) table.getWrappedElement()).getScrollCount(), is(tableScrollCount));

        var button = seleniumSteps.find(button().foundFrom(table));
        assertThat(((MockWebElement) table.getWrappedElement()).getScrollCount(), is(tableScrollCount));

        var buttonScrollCount = ((MockWebElement) button.getWrappedElement()).getScrollCount();
        seleniumSteps.click(button);
        assertThat(((MockWebElement) button.getWrappedElement()).getScrollCount(), is(buttonScrollCount));
    }

    @Test
    public void scrollScrollableWidgetIntoView() {
        var button = seleniumSteps.find(widget(CustomizedButton.class));
        var textArea = seleniumSteps.find(widget(TextArea.class));

        int buttonScrolls = button.getScrollCount();
        int textAreaScrolls = textArea.getScrollCount();

        seleniumSteps.click(button);
        seleniumSteps.edit(textArea, "1", "2");

        assertThat(button.getScrollCount(), is(buttonScrolls + 1));
        assertThat(textArea.getScrollCount(), is(textAreaScrolls + 1));
    }

    @AfterClass
    public static void dropProperty() {
        System.getProperties().remove(DEFAULT_SCROLLER_PROPERTY.getName());
    }
}
