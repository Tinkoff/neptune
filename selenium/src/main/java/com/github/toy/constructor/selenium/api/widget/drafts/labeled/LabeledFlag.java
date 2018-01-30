package com.github.toy.constructor.selenium.api.widget.drafts.labeled;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.drafts.Flag;
import org.openqa.selenium.WebElement;

public abstract class LabeledFlag extends Flag implements Labeled {
    public LabeledFlag(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
