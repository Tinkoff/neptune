package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

import java.util.List;

@Name("Footer")
public abstract class TableFooter extends Widget implements HasValue<List<String>> {
    public TableFooter(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
