package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.link;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Priority;

import static ru.tinkoff.qa.neptune.selenium.api.widget.Priority.HIGHEST;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_LINK;

@Priority(HIGHEST)
@Name(LABELED_LINK)
public class LabeledLink extends SimpleLink {

    @Label
    @FindBy(tagName = LABEL_TAG)
    private WebElement labels;

    public LabeledLink(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
