package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.Map;

@Name("Table")
public abstract class Table extends Widget implements HasValue<Map<String, List<String>>> {

    public Table(WebElement wrappedElement) {
        super(wrappedElement);
    }

}
