package ru.tinkoff.qa.neptune.selenium.test.steps.tests.click;

import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.BaseWebDriverPreparations;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.attr;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class ClickTest extends BaseWebDriverPreparations {

    @Test
    public void test1() {
        seleniumSteps.click(button());
        assertThat(COMMON_BUTTON1.getClickCount(), is(1));

        seleniumSteps.click(button(BUTTON_LABEL_TEXT12).criteria(attr(ATTR6, VALUE14)));
        assertThat(CUSTOM_LABELED_BUTTON4.getClickCount(), is(1));
    }

    @Test
    public void test2() {
        int clickCount = CUSTOM_LABELED_BUTTON1.getClickCount();
        Button button = seleniumSteps.find(button(BUTTON_LABEL_TEXT5));
        seleniumSteps.click(button);
        assertThat(CUSTOM_LABELED_BUTTON1.getClickCount(), is(clickCount + 1));
    }

    @Test
    public void test3() {
        seleniumSteps.click(webElement(CHAINED_FIND_TAB));
        assertThat(COMMON_LABELED_TAB1.getClickCount(), is(1));
    }
}
