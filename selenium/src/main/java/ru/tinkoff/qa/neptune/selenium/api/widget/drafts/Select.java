package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.*;

@Name("Selector")
public abstract class Select extends Widget implements Editable<String>, HasValue<String>, HasOptions {

    public Select(WebElement wrappedElement) {
        super(wrappedElement);
    }

}
