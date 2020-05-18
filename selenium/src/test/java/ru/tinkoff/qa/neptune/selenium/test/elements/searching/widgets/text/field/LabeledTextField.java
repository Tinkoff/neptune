package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.text.field;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;

import java.util.List;

import static java.util.stream.Collectors.toList;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_TAG;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.TEXT_FIELD_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.LABELED_TEXT_FIELD;

@Name(LABELED_TEXT_FIELD)
@FindBy(xpath = TEXT_FIELD_XPATH)
public class LabeledTextField extends SimpleTextField implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledTextField(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }
}
