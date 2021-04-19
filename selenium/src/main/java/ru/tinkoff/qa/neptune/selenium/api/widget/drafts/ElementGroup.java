package ru.tinkoff.qa.neptune.selenium.api.widget.drafts;

import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

/**
 * Elements which are used to group other elements
 */
@Name("Group of elements")
@NameMultiple("Groups of elements")
public abstract class ElementGroup extends Widget {

    public ElementGroup(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
