package com.github.toy.constructor.selenium.api.widget.drafts.labeled;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.drafts.Select;
import org.openqa.selenium.WebElement;

public abstract class LabeledSelect extends Select implements Labeled {

    public LabeledSelect(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
