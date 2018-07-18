package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

/**
 * refs, anchors etc.
 */
@Name("Link")
public abstract class Link extends Widget implements Clickable {

    public Link(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * @return reference to remote content
     */
    public abstract String getReference();
}
