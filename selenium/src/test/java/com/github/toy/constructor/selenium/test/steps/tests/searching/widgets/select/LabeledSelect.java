package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.select;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Priority;
import com.github.toy.constructor.selenium.api.widget.drafts.Select;
import com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.link.SimpleLink;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.toy.constructor.selenium.api.widget.Priority.HIGHEST;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_TAG;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.OPTION;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.SELECT;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.LABELED_SELECT;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_SELECT;
import static java.util.stream.Collectors.toList;

@Name(LABELED_SELECT)
public class LabeledSelect extends SimpleSelect implements Labeled {

    @FindBy(tagName = LABEL_TAG)
    private List<WebElement> labels;

    public LabeledSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }
}
