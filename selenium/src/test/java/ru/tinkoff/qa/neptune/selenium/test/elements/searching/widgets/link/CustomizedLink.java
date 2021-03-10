package ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.link;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import ru.tinkoff.qa.neptune.selenium.api.widget.Label;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Link;

import static org.openqa.selenium.By.xpath;
import static ru.tinkoff.qa.neptune.selenium.test.FakeDOMModel.*;
import static ru.tinkoff.qa.neptune.selenium.test.elements.searching.widgets.WidgetNames.CUSTOM_LINK;

@FindBy(css = CUSTOM_LINK_CSS)
@Name(CUSTOM_LINK)
public class CustomizedLink extends Link {

    public CustomizedLink(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Label
    public String label1() {
        return findElements(xpath(LABEL_XPATH)).get(0).getText();
    }

    @Label
    public String label12() {
        return findElements(xpath(LABEL_XPATH2)).get(1).getText();
    }

    @Override
    public String getReference() {
        return getWrappedElement().getAttribute(HREF);
    }

    @Override
    public void click() {
        getWrappedElement().click();
    }
}
