package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.flags;

import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Flag;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.CHECK_BOX_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_CHECKBOX;

@Name(SIMPLE_CHECKBOX)
@FindBy(xpath = CHECK_BOX_XPATH)
@Priority(HIGHEST)
public class SimpleCheckbox extends Flag.CheckBox {

    public SimpleCheckbox(WebElement wrappedElement) {
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
