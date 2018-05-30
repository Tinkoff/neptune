package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.Editable;
import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

@Name("Selector")
public abstract class Select extends Widget implements Editable<String>, HasValue<List<String>> {

    public Select(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
