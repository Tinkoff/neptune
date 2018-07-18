package ru.tinkoff.qa.neptune.selenium.api.widget;

public interface HasCssValue {
    /**
     * Taken from documentation of Selenium:
     * <p>
     * Get the value of a given CSS property.
     * Color values should be returned as rgba strings, so,
     * for example if the "background-color" property is set as "green" in the
     * HTML source, the returned value will be "rgba(0, 255, 0, 1)".
     *
     * Note that shorthand CSS properties (e.g. background, font, border, border-top, margin,
     * margin-top, padding, padding-top, list-style, outline, pause, cue) are not returned,
     * in accordance with the
     * <a href="http://www.w3.org/TR/DOM-Level-2-Style/css.html#CSS-CSSStyleDeclaration">DOM CSS2 specification</a>
     * - you should directly access the longhand properties (e.g. background-color) to access the
     * desired values.
     * </p>
     *
     * @param propertyName the css property name of the element
     * @return The current, computed value of the property.
     */
    String getCssValue(String propertyName);
}
