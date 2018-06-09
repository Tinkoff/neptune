package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.drafts.Table;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.Map;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SPREADSHEET_TABLE;
import static java.util.stream.Collectors.toList;

@Name(SPREADSHEET_TABLE)
@FindBy(className = SPREAD_SHEET_CLASS)
public class SpreadsheetTable extends Table implements Labeled {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    public SpreadsheetTable(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public Map<String, List<String>> getValue() {
        return null;
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }
}
