package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

/**
 * Test input, text area or something like that
 */
@Name("Text field")
public abstract class TextField extends Widget implements Editable<CharSequence[]>,
        HasValue<String> {

    public TextField(WebElement wrappedElement) {
        super(wrappedElement);
    }

    @Override
    public abstract void edit(CharSequence... valueToSet);
}
