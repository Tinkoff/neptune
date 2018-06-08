package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.BUTTON_TAG;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_BUTTON;

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
