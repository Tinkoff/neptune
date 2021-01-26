package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.table;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TABLE;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_TABLE;

@Name(LABELED_TABLE)
@FindBy(tagName = TABLE)
public class LabeledTable extends SimpleTable {

    public LabeledTable(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Label
    public String label1() {
        return findElements(tagName(LABEL_TAG)).get(0).getText();
    }

    @Label
    public String label2() {
        return findElements(tagName(LABEL_TAG)).get(1).getText();
    }
}
