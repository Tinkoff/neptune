package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

/**
 * Checkboxes, radio buttons etc.
 */
public abstract class Flag extends Widget implements Editable<Boolean>,
        HasValue<Boolean> {

    public Flag(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
