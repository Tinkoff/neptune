package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;

@Name("Abstract button")
public abstract class AbstractButton extends Button {
    public AbstractButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
