package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_TAG;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.LABELED_BUTTON;

@Name(LABELED_BUTTON)
public class LabeledButton extends SimpleButton implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
