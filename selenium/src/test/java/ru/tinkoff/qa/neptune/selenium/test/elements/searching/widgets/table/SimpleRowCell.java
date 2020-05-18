package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.table;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableCell;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TD;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.COMMON_ROW_CELL;

@Name(COMMON_ROW_CELL)
@Priority(HIGHEST)
@FindBy(tagName = TD)
public class SimpleRowCell extends TableCell {
    public SimpleRowCell(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getValue() {
        return getWrappedElement().getText();
    }

    @Override
    public void click() {

    }
}
