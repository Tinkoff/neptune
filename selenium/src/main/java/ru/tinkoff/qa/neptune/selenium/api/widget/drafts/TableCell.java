package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

@Name("Cell")
public abstract class TableCell extends Widget implements HasValue<String>, Clickable {
    public TableCell(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
