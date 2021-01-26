package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.tab;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Tab;

import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.CUSTOMIZED_TAB;

@Name(CUSTOMIZED_TAB)
@FindBy(className = TAB_CLASS)
public class CustomizedTab extends Tab {

    public CustomizedTab(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Label
    public String label1() {
        return findElement(xpath(LABEL_XPATH)).getText();
    }

    @Label
    public String label12() {
        return findElement(xpath(LABEL_XPATH2)).getText();
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
