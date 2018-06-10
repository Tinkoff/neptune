package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.drafts.TableRow;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SPREADSHEET_ROW;
import static java.util.stream.Collectors.toList;

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
