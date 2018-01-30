package com.github.toy.constructor.selenium.api.widget.drafts.labeled;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.drafts.Button;
import org.openqa.selenium.WebElement;

public abstract class LabeledButton extends Button implements Labeled {

    public LabeledButton(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
