package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.table;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.TableFooter;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.CELL_CLASS;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.FOOTER_CLASS;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.SPREADSHEET_FOOTER;
import static java.util.stream.Collectors.toList;

@Name(SPREADSHEET_FOOTER)
@FindBy(className = FOOTER_CLASS)
public class SpreadSheetFooter extends TableFooter {

    @FindBy(className = CELL_CLASS)
    private List<WebElement> cells;

    public SpreadSheetFooter(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> getValue() {
        return cells.stream().map(WebElement::getText).collect(toList());
    }
}
