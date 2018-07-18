package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.flags;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.RADIO_BUTTON_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_RADIOBUTTON;

@Name(SIMPLE_RADIOBUTTON)
@FindBy(xpath = RADIO_BUTTON_XPATH)
@Priority(2)
public class SimpleRadioButton extends Flag.RadioButton {

    public SimpleRadioButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void edit(Boolean valueToSet) {
        if (getValue() != valueToSet) {
            getWrappedElement().click();
        }
    }

    @Override
    public Boolean getValue() {
        return getWrappedElement().isSelected();
    }
}
