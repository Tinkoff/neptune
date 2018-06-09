package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.tab;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_TAG;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.LABELED_TAB;
import static java.util.stream.Collectors.toList;

@Name(LABELED_TAB)
public class LabeledTab extends SimpleTab implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledTab(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }
}
