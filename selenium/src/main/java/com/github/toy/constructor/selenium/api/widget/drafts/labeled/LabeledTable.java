package com.github.toy.constructor.selenium.api.widget.drafts.labeled;

import com.github.toy.constructor.selenium.api.widget.Labeled;
import com.github.toy.constructor.selenium.api.widget.drafts.Table;
import org.openqa.selenium.WebElement;

public abstract class LabeledTable extends Table implements Labeled {
    public LabeledTable(WebElement wrappedElement) {
        super(wrappedElement);
    }
}
