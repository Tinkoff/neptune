package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import ru.tinkoff.qa.neptune.selenium.BaseWebDriverPreparations;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
import ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier;
import ru.tinkoff.qa.neptune.selenium.test.RetryAnalyzer;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.CustomizedButton;
import ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons.LabeledButton;

import static java.time.Duration.ofMillis;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.core.api.steps.Criteria.NOT;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.CommonElementCriteria.*;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.buttons;
import static ru.tinkoff.qa.neptune.selenium.functions.searching.MultipleSearchSupplier.webElements;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;

public class SearchForWidgetsPositiveTest extends BaseWebDriverPreparations {

    @DataProvider(name = "search without criteria")
    public static Object[][] searchCriteria() {
        return new Object[][]{
            {buttons(), 22, Button.class},

            {buttons().timeOut(FIVE_SECONDS),
                22,
                Button.class},

            {buttons(BUTTON_LABEL_TEXT1),
                1,
                LabeledButton.class},

            {buttons(BUTTON_LABEL_TEXT1)
                .timeOut(FIVE_SECONDS),
                1,
                LabeledButton.class},

                {buttons(BUTTON_LABEL_TEXT6), 2,
                        CustomizedButton.class},


                {buttons().criteria(enabled()),
                        12,
                        Button.class},

                {buttons().criteria(NOT(enabled())),
                        10,
                        Button.class},

                {buttons().criteria(visible()),
                        11,
                        Button.class},

                {buttons().criteria(NOT(visible())),
                        11,
                        Button.class},

                {buttons()
                        .criteria(nested(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1)),
                        8,
                        Button.class},

                {buttons().criteria(NOT(nested(webElements(tagName(LABEL_TAG))
                        .timeOut(ofMillis(5)), 1))),
                        14,
                        Button.class},

                {buttons()
                        .criteria(attr(ATTR5, VALUE11))
                        .criteria(attr(ATTR6, VALUE12)),
                        2,
                        CustomizedButton.class},

                {buttons().criteria(attr(ATTR5, VALUE11))
                        .criteria(NOT(attr(ATTR6, VALUE13))),
                        2,
                        CustomizedButton.class},
        };
    }

    @Test(retryAnalyzer = RetryAnalyzer.class, dataProvider = "search without criteria")
    public <T extends Widget> void findWidgetTest(MultipleSearchSupplier<T> search,
                                                  int count,
                                                  Class<?> widgetClass) {
        setStartBenchMark();
        var list = seleniumSteps.find(search);
        setEndBenchMark();
        assertThat(list, hasSize(count));
        assertThat(list, everyItem(instanceOf(widgetClass)));
        assertThat(getTimeDifference() - ONE_SECOND.toMillis(), lessThan(250L));
    }
}
