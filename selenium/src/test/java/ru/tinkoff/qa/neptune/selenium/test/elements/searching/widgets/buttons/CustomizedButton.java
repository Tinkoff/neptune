package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.buttons;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.ScrollsIntoView;

import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.CUSTOM_BUTTON;

@FindBy(className = CUSTOM_BUTTON_CLASS)
@Name(CUSTOM_BUTTON)
public class CustomizedButton extends AbstractButton implements ScrollsIntoView {

    private int scrollCount = 0;

    public CustomizedButton(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Label
    public String label() {
        return findElements(xpath(LABEL_XPATH)).get(0).getText();
    }

    @Label
    public String label2() {
        return findElements(xpath(LABEL_XPATH2)).get(1).getText();
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }

    @Override
    public void scrollIntoView() {
        scrollCount++;
    }

    public int getScrollCount() {
        return scrollCount;
    }
}
