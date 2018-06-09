package com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.link;

import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.drafts.Link;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static com.github.toy.constructor.selenium.test.FakeDOMModel.HREF;
import static com.github.toy.constructor.selenium.test.FakeDOMModel.LINK_TAG;
import static com.github.toy.constructor.selenium.test.steps.tests.searching.widgets.WidgetNames.SIMPLE_LINK;

@Name(SIMPLE_LINK)
@FindBy(tagName = LINK_TAG)
public class SimpleLink extends Link {

    public SimpleLink(WebElement wrappedElement) {
        super(wrappedElement);
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
