package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

/**
 * For different kind of forms
 */
@Name("Form")
public abstract class Form extends Widget {

    public Form(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
