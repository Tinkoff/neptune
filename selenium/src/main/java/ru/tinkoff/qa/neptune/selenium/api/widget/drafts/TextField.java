package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

import java.util.List;

/**
 * Text input, text area or something like that
 */
@Name("Text field")
@NameMultiple("Text fields")
public abstract class TextField extends Widget implements Editable<List<CharSequence>>,
        HasValue<String> {

    public TextField(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
