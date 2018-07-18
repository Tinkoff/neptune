package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

@Name("Footer")
public abstract class TableFooter extends Widget implements HasValue<List<String>> {
    public TableFooter(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
