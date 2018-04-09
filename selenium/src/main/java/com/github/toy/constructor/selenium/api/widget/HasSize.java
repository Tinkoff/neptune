package com.github.toy.constructor.selenium.api.widget;

import org.openqa.selenium.Dimension;

public interface HasSize {
    /**
     * It is the width and height of the rendered element
     *
     * @return The size of the element on the page.
     */
    Dimension getSize();
}
