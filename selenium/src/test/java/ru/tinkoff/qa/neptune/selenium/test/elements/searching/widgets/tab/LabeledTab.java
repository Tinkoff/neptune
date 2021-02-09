package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.tab;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_TAB;

@Name(LABELED_TAB)
public class LabeledTab extends SimpleTab {

    @Label
    @FindBy(tagName = LABEL_TAG)
    private WebElement label;

    public LabeledTab(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
