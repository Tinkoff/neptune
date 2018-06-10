package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.table;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.drafts.TableHeader;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SPREADSHEET_HEADER;
import static java.util.stream.Collectors.toList;

@Name(SPREADSHEET_HEADER)
@FindBy(className = HEADLINE_CLASS)
public class SpreadSheetHeader extends TableHeader {

    @FindBy(className = CELL_CLASS)
    private List<WebElement> cells;

    public SpreadSheetHeader(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> getValue() {
        return cells.stream().map(WebElement::getText).collect(toList());
    }
}
