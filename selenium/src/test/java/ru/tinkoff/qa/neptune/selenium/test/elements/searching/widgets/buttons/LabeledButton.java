package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.BUTTON_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_BUTTON;

@Name(LABELED_BUTTON)
@FindBy(tagName = BUTTON_TAG)
public class LabeledButton extends SimpleButton {

    @Label
    @FindBy(tagName = LABEL_TAG)
    private WebElement label;

    public LabeledButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
