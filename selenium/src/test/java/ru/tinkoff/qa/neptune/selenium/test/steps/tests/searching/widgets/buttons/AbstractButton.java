package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Button;
import org.openqa.selenium.WebElement;

@Name("Abstract button")
public abstract class AbstractButton extends Button {
    public AbstractButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
