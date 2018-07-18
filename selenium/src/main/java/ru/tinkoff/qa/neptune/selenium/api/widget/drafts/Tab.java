package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

@Name("Tab")
public abstract class Tab extends Widget implements Clickable {
    public Tab(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * @return is the tab selected or not.
     */
    public abstract boolean isActive();
}
