package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.BUTTON_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.SIMPLE_BUTTON;

@Priority(HIGHEST)
@Name(SIMPLE_BUTTON)
@FindBy(tagName = BUTTON_TAG)
public class SimpleButton extends AbstractButton {

    public SimpleButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
