package ru.tinkoff.qa.neptune.selenium.test.steps.tests.click;

import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
import ru.tinkoff.qa.neptune.selenium.test.BaseWebDriverTest;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier;

import static ru.tinkoff.qa.neptune.selenium.functions.click.ClickActionSupplier.on;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldContainElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonConditions.shouldHaveAttribute;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.button;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.table;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier.webElement;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static java.time.Duration.ofMillis;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import static java.util.List.of;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;

public class ClickTest extends BaseWebDriverTest {

    @Test
    public void clickOnWidgetBySearchCriteriaTest() {
        seleniumSteps.click(on(button()));
        assertThat(COMMON_BUTTON1.getClickCount(), is(1));

        seleniumSteps.click(on(button(of(BUTTON_LABEL_TEXT8, BUTTON_LABEL_TEXT12),
                shouldHaveAttribute(ATTR6, VALUE14))));
        assertThat(CUSTOM_LABELED_BUTTON4.getClickCount(), is(1));

        seleniumSteps.click(on(button(BUTTON_LABEL_TEXT6)
                .foundFrom(webElement(className(CELL_CLASS), CELL_TEXT81, shouldContainElements(buttons(ofMillis(5)))))));
        assertThat(CUSTOM_LABELED_BUTTON2.getClickCount(), is(1));
    }

    @Test
    public void clickOnWidgetDirectlyTest() {
        Button button = seleniumSteps.find(button(BUTTON_LABEL_TEXT5));
        seleniumSteps.click(ClickActionSupplier.on(button));
        assertThat(CUSTOM_LABELED_BUTTON1.getClickCount(), is(1));

        button = seleniumSteps.find(button(BUTTON_LABEL_TEXT5)
                .foundFrom(webElement(className(CELL_CLASS), CELL_TEXT73, shouldContainElements(buttons(ofMillis(5))))));
        seleniumSteps.click(ClickActionSupplier.on(button));
        assertThat(CUSTOM_LABELED_BUTTON1.getClickCount(), is(2));
    }

    @Test
    public void clickOnWebElementBySearchCriteriaTest() {
        seleniumSteps.click(on(webElement(CHAINED_FIND_TAB)));
        assertThat(COMMON_LABELED_TAB1.getClickCount(), is(1));

        seleniumSteps.click(on(webElement(className(TAB_CLASS), shouldHaveAttribute(ATTR20, VALUE20)
                .and(shouldContainElements(webElements(CUSTOM_LABEL_BY, TAB_TEXT9, ofMillis(5)))))));
        assertThat(CUSTOM_LABELED_TAB1.getClickCount(), is(1));

        seleniumSteps.click(on(webElement(tagName(TEXT_AREA_TAG)).foundFrom(table(TABLE_LABEL_TEXT10))));
        assertThat(TEXT_AREA2.getClickCount(), is(1));
    }

    @Test
    public void clickOnWebElementDirectlyTest() {
        WebElement webElement = seleniumSteps.find(webElement(className(TAB_CLASS),
                shouldHaveAttribute(ATTR20, VALUE1)
                        .and(shouldContainElements(webElements(CUSTOM_LABEL_BY, TAB_TEXT10, ofMillis(5))))));
        seleniumSteps.click(ClickActionSupplier.on(webElement));
        assertThat(CUSTOM_LABELED_TAB2.getClickCount(), is(1));
    }
}
