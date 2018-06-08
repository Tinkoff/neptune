package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.buttons;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.Name;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.CUSTOM_BUTTON_CLASS;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_XPATH;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.LABEL_XPATH2;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.CUSTOM_BUTTON;

@FindBy(className = CUSTOM_BUTTON_CLASS)
@Name(CUSTOM_BUTTON)
public class CustomizedButton extends AbstractButton implements Labeled {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    public CustomizedButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }

    @Override
    public List<String> labels() {
        return labels.stream().map(WebElement::getText).collect(Collectors.toList());
    }
}
