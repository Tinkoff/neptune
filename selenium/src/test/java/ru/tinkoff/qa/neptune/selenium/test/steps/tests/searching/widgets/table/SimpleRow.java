package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableRow;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.FindBys;

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TD;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TR;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.T_BODY;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.COMMON_ROW;
import static java.util.stream.Collectors.toList;

@Name(COMMON_ROW)
@Priority(HIGHEST)
@FindBys({@FindBy(tagName = T_BODY), @FindBy(tagName = TR)})
public class SimpleRow extends TableRow {

    @FindBy(tagName = TD)
    private List<WebElement> cells;

    public SimpleRow(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> getValue() {
        return cells.stream().map(WebElement::getText).collect(toList());
    }
}
