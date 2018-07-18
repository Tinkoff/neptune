package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableFooter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.COMMON_FOOTER;
import static java.util.stream.Collectors.toList;

@Name(COMMON_FOOTER)
@Priority(HIGHEST)
@FindBys({@FindBy(tagName =T_FOOT), @FindBy(tagName = TR)})
public class SimpleFooter extends TableFooter {

    @FindBy(tagName = TH)
    private List<WebElement> cells;

    public SimpleFooter(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> getValue() {
        return cells.stream().map(WebElement::getText).collect(toList());
    }
}
