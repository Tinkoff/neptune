package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.Editable;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasValue;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

/**
 * Test input, text area or something like that
 */
@Name("Text field")
public abstract class TextField extends Widget implements Editable<List<CharSequence>>,
        HasValue<String> {

    public TextField(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
