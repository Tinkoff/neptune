package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets;

import com.github.toy.constructor.selenium.api.widget.Widget;
import com.github.toy.constructor.selenium.functions.searching.SearchSupplier;
import com.github.toy.constructor.selenium.test.BaseWebDriverTest;
import com.github.toy.constructor.selenium.test.RetryAnalyzer;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons.CustomizedButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons.LabeledButton;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons.SimpleButton;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.List;
import java.util.function.Predicate;

import static com.github.toy.constructor.selenium.functions.searching.CommonConditions.shouldBeLabeledBy;
import static com.github.toy.constructor.selenium.functions.searching.SearchSupplier.button;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.CUSTOM_BUTTON;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.LABELED_BUTTON;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_BUTTON;
import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;

public class SearchForWidgetPositiveTest extends BaseWebDriverTest {

    private static String getWidgetDescription(String name, Predicate<?> condition) {
        return format("%s found on condition '%s'", name, condition);
    }

    @DataProvider(name = "search without criteria")
    public static Object[][] searchCriteria() {
        return new Object[][] {
                {button(), COMMON_BUTTON1, SimpleButton.class, SIMPLE_BUTTON},

                {button(FIVE_SECONDS), COMMON_BUTTON1, SimpleButton.class, SIMPLE_BUTTON},

                {button(List.of(BUTTON_LABEL_TEXT1)), COMMON_LABELED_BUTTON1, LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT1))},

                {button(List.of(BUTTON_LABEL_TEXT1), FIVE_SECONDS), COMMON_LABELED_BUTTON1, LabeledButton.class,
                        getWidgetDescription(LABELED_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT1))},

                {button(List.of(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6)), CUSTOM_LABELED_BUTTON2, CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6))},

                {button(List.of(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6), FIVE_SECONDS), CUSTOM_LABELED_BUTTON2, CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT10, BUTTON_LABEL_TEXT6))},

                {button(BUTTON_LABEL_TEXT10), CUSTOM_LABELED_BUTTON2, CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT10))},

                {button(BUTTON_LABEL_TEXT10, FIVE_SECONDS), CUSTOM_LABELED_BUTTON2, CustomizedButton.class,
                        getWidgetDescription(CUSTOM_BUTTON, shouldBeLabeledBy(BUTTON_LABEL_TEXT10))},
        };
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, dataProvider = "search without criteria")
    public <T extends Widget> void findWidgetWithoutConditionTest(SearchSupplier<T> search,
                                                                                WebElement element,
                                                                                Class<? extends Widget> widgetClass,
                                                                                String expectedDescription) {
        setStartBenchMark();
        T t = seleniumSteps.find(search);
        setEndBenchMark();
        assertThat(widgetClass.isAssignableFrom(t.getClass()), is(true));
        assertThat(t.getWrappedElement(), equalTo(element));
        assertThat(t.toString(), is(expectedDescription));
        assertThat(getTimeDifference(), lessThan(HALF_SECOND.toMillis()));
    }
}
