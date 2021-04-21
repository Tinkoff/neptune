package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import java.util.List;

@Name("Table Footer")
@NameMultiple("Table Footers")
public abstract class TableFooter extends Widget implements HasValue<List<String>> {
    public TableFooter(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
