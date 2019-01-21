package ru.tinkoff.qa.neptune.selenium.test.steps.tests.scrolling;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import ru.tinkoff.qa.neptune.selenium.test.MockWebElement;

import java.time.Duration;

import static java.time.Duration.ofSeconds;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldContainElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldHaveText;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.OPTION;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.SELECT;

public class ScrollIntoViewTest extends BaseWebDriverTest {

    @Test
    public void scrollWebElementIntoView() {
        var select = seleniumSteps.find(webElement(tagName(SELECT)));
        select.getClass();
        assertThat(((MockWebElement) select).getScrollCount(), is(1)); //+1 for invocation of getScrollCount

        var option = seleniumSteps.find(webElement(tagName(OPTION)).foundFrom(webElement(tagName(SELECT))));
        assertThat(((MockWebElement) select).getScrollCount(), is(2)); //+1 for invocation of getScrollCount

        seleniumSteps.click(on(option));
        assertThat(((MockWebElement) option).getScrollCount(), is(2)); //+1 for invocation of getScrollCount
    }

    @Test
    public void scrollWidgetElementIntoView() {
        var table = seleniumSteps.find(table(shouldContainElements(buttons(ofSeconds(1)))));
        table.getClass();
        assertThat(((MockWebElement) table.getWrappedElement()).getScrollCount(), is(1)); //+1 for invocation of getScrollCount

        var button = seleniumSteps.find(button().foundFrom(table));
        assertThat(((MockWebElement) table.getWrappedElement()).getScrollCount(), is(2)); //+1 for invocation of getScrollCount

        seleniumSteps.click(on(button));
        assertThat(((MockWebElement) button.getWrappedElement()).getScrollCount(), is(2)); //+1 for invocation of getScrollCount
    }
}
