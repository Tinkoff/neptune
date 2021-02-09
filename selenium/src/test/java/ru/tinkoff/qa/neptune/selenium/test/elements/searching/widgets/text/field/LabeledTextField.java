package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.text.field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

import static org.openqa.selenium.By.tagName;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TEXT_FIELD_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_TEXT_FIELD;

@Name(LABELED_TEXT_FIELD)
@FindBy(xpath = TEXT_FIELD_XPATH)
public class LabeledTextField extends SimpleTextField {

    public LabeledTextField(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Label
    public String label() {
        return findElement(tagName(LABEL_TAG)).getText();
    }
}
