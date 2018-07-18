package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableCell;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TH;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.COMMON_BORDER_CELL;

@Name(COMMON_BORDER_CELL)
@Priority(2)
@FindBy(tagName = TH)
public class SimpleHeaderAndFooterCell extends TableCell {
    public SimpleHeaderAndFooterCell(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getValue() {
        return getWrappedElement().getText();
    }
}
