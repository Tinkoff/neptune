package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.flags;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;

import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.CHECK_BOX_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_CHECKBOX;

@Name(LABELED_CHECKBOX)
@FindBy(xpath = CHECK_BOX_XPATH)
@Priority(3)
public class LabeledCheckBox extends SimpleCheckbox {

    public LabeledCheckBox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Label
    public String label1() {
        return findElements(tagName(LABEL_TAG)).get(0).getText();
    }

    @Label
    public String label2() {
        return findElements(tagName(LABEL_TAG)).get(1).getText();
    }
}
