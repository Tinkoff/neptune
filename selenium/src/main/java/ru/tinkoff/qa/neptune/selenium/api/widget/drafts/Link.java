package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Clickable;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

/**
 * refs, anchors etc.
 */
@Name("Link")
@NameMultiple("Links")
public abstract class Link extends Widget implements Clickable {

    public Link(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * @return reference to remote content
     */
    public abstract String getReference();
}
