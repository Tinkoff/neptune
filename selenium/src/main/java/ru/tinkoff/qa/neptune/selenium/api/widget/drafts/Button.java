package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

@Name("Button")
@NameMultiple("Buttons")
public abstract class Button extends Widget implements Clickable {

    public Button(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
