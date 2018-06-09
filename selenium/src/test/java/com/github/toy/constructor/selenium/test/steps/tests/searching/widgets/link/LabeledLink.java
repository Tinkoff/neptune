package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.link;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_TAG;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.LABELED_LINK;

@Priority(HIGHEST)
@Name(LABELED_LINK)
public class LabeledLink extends SimpleLink implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledLink(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
