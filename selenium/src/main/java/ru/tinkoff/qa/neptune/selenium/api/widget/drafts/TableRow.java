package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.util.List;

@Name("Table Row")
@NameMultiple("Table Rows")
public abstract class TableRow extends Widget implements HasValue<List<String>> {
    public TableRow(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
