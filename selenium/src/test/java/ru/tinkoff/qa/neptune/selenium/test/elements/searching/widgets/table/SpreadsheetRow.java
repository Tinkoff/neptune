package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.table;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableRow;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.CELL_CLASS;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.STRING_CLASS;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.SPREADSHEET_ROW;

@Name(SPREADSHEET_ROW)
@FindBy(className = STRING_CLASS)
public class SpreadsheetRow extends TableRow {

    @FindBy(className = CELL_CLASS)
    private List<WebElement> cells;

    public SpreadsheetRow(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> getValue() {
        return cells.stream().map(WebElement::getText).collect(toList());
    }
}
