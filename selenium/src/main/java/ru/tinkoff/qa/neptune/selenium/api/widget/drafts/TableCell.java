package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

@Name("Table Cell")
@NameMultiple("Table Cells")
public abstract class TableCell extends Widget implements HasValue<String>, Clickable {
    public TableCell(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
