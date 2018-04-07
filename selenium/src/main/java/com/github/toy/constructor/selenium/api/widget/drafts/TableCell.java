package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.HasValue;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

@Name("Cell")
public abstract class TableCell extends Widget implements HasValue<String> {
    public TableCell(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
