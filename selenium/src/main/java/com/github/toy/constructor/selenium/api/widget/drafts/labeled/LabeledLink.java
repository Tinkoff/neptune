package com.github.toy.constructor.selenium.api.widget.drafts.labeled;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.drafts.Link;
import org.openqa.selenium.WebElement;

public abstract class LabeledLink extends Link implements Labeled {
    public LabeledLink(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
