package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableCell;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.CELL_CLASS;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.SPREADSHEET_CELL;

@Name(SPREADSHEET_CELL)
@FindBy(className = CELL_CLASS)
public class SpreadSheetCell extends TableCell {
    public SpreadSheetCell(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public String getValue() {
        return getWrappedElement().getText();
    }
}
