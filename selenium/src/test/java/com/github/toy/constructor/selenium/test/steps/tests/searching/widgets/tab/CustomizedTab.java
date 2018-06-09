package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.tab;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.drafts.Tab;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.*;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.CUSTOMIZED_TAB;
import static java.util.stream.Collectors.toList;

@Name(CUSTOMIZED_TAB)
@FindBy(className = TAB_CLASS)
public class CustomizedTab extends Tab implements Labeled {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    public CustomizedTab(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(toList());
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
