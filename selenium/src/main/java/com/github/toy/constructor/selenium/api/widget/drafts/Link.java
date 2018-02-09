package com.github.toy.constructor.selenium.api.widget.drafts;

import com.github.toy.constructor.selenium.api.widget.Clickable;
import com.github.toy.constructor.selenium.api.widget.Name;
import com.github.toy.constructor.selenium.api.widget.Widget;
import org.openqa.selenium.WebElement;

/**
 * refs, anchors etc.
 */
@Name("Link")
public abstract class Link extends Widget implements Clickable {

    public Link(WebElement wrappedElement) {
        super(wrappedElement);
    }

    /**
     * @return reference to remote content
     */
    public abstract String getReference();
}
