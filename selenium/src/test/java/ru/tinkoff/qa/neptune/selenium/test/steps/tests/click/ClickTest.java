package ru.tinkoff.qa.neptune.selenium.test.steps.tests.click;

import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.attr;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.nested;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.*;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class ClickTest extends BaseWebDriverTest {

    @Test
    public void test1() {
        seleniumSteps.click(button());
        assertThat(COMMON_BUTTON1.getClickCount(), is(1));

        seleniumSteps.click(button(BUTTON_LABEL_TEXT12).criteria(attr(ATTR6, VALUE14)));
        assertThat(CUSTOM_LABELED_BUTTON4.getClickCount(), is(1));

        seleniumSteps.click(button(BUTTON_LABEL_TEXT6)
                .foundFrom(webElement(className(CELL_CLASS), CELL_TEXT81))
                .criteria(nested(buttons()))
                .foundFrom(table().criteria(attr(ATTR1, VALUE12))));
        assertThat(CUSTOM_LABELED_BUTTON2.getClickCount(), is(1));
    }

    @Test
    public void test2() {
        int clickCount = CUSTOM_LABELED_BUTTON1.getClickCount();
        Button button = seleniumSteps.find(button(BUTTON_LABEL_TEXT5));
        seleniumSteps.click(button);
        assertThat(CUSTOM_LABELED_BUTTON1.getClickCount(), is(clickCount + 1));

        button = seleniumSteps.find(button(BUTTON_LABEL_TEXT5)
                .foundFrom(webElement(className(CELL_CLASS), CELL_TEXT73))
                .criteria(nested(buttons())));
        seleniumSteps.click(button);
        assertThat(CUSTOM_LABELED_BUTTON1.getClickCount(), is(clickCount + 2));
    }

    @Test
    public void test3() {
        seleniumSteps.click(webElement(CHAINED_FIND_TAB));
        assertThat(COMMON_LABELED_TAB1.getClickCount(), is(1));

        seleniumSteps.click(webElement(className(TAB_CLASS))
                .criteria(attr(ATTR20, VALUE20))
                .criteria(nested(webElements(CUSTOM_LABEL_BY, TAB_TEXT9))));
        assertThat(CUSTOM_LABELED_TAB1.getClickCount(), is(1));

        seleniumSteps.click(webElement(tagName(TEXT_AREA_TAG)).foundFrom(table(TABLE_LABEL_TEXT10)));
        assertThat(TEXT_AREA2.getClickCount(), is(1));
    }

    @Test
    public void test4() {
        WebElement webElement = seleniumSteps.find(webElement(className(TAB_CLASS))
                .criteria(attr(ATTR20, VALUE1))
                .criteria(nested(webElements(CUSTOM_LABEL_BY, TAB_TEXT10))));
        seleniumSteps.click(webElement);
        assertThat(CUSTOM_LABELED_TAB2.getClickCount(), is(1));
    }
}
