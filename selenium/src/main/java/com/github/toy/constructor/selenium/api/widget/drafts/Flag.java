package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

/**
 * Checkboxes, radio buttons etc.
 */
@Name("Flag")
public abstract class Flag extends Widget implements Editable<Boolean>,
        HasValue<Boolean> {

    public Flag(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Name("Check box")
    public static abstract class CheckBox extends Flag {
        public CheckBox(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }

    @Name("Radio button")
    public static abstract class RadioButton extends Flag {
        public RadioButton(WebElement wrappedElement) {
            super(wrappedElement);
        }
    }
}
