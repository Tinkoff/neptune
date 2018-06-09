package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.flags;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_TAG;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.LABELED_CHECKBOX;

@Name(LABELED_CHECKBOX)
public class LabeledCheckBox extends SimpleCheckbox implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledCheckBox(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
