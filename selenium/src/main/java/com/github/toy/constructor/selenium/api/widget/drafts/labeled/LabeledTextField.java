package com.github.toy.constructor.selenium.api.widget.drafts.labeled;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.drafts.TextField;
import org.openqa.selenium.WebElement;

public abstract class LabeledTextField extends TextField implements Labeled {

    public LabeledTextField(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
