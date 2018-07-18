package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

@Name("Button")
public abstract class Button extends Widget implements Clickable {

    public Button(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
