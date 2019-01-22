package ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.buttons;

import ru.tinkoff.qa.neptune.selenium.api.widget.Labeled;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindAll;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;

import java.util.List;
import java.util.stream.Collectors;

import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.CUSTOM_BUTTON_CLASS;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_XPATH;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.LABEL_XPATH2;
import static ru.tinkoff.qa.neptune.selenium.test.steps.tests.searching.widgets.WidgetNames.CUSTOM_BUTTON;

@FindBy(className = CUSTOM_BUTTON_CLASS)
@Name(CUSTOM_BUTTON)
public class CustomizedButton extends AbstractButton implements Labeled, ScrollsIntoView {

    @FindAll({@FindBy(xpath = LABEL_XPATH),
            @FindBy(xpath = LABEL_XPATH2)})
    private List<WebElement> labels;

    private int scrollCount = 0;

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

    @Override
    public void scrollIntoView() {
        scrollCount++;
    }

    public int getScrollCount() {
        return scrollCount;
    }
}
