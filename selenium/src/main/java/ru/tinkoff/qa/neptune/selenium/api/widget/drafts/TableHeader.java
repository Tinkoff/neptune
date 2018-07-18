package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

@Name("Header")
public abstract class TableHeader extends Widget implements HasValue<List<String>> {
    public TableHeader(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
