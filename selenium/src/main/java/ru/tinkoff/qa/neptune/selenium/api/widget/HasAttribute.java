package ru.tinkoff.qa.neptune.selenium.api.widget;

public interface HasAttribute {

    /**
     * Taken from documentation of Selenium
     * <p>
     * Get the value of the given attribute of the element. Will return the current value, even if
     * this has been modified after the page has been loaded.
     *
     * <p>More exactly, this method will return the value of the property with the given name, if it
     * exists. If it does not, then the value of the attribute with the given name is returned. If
     * neither exists, null is returned.
     *
     * <p>The "style" attribute is converted as best can be to a text representation with a trailing
     * semi-colon.
     *
     * <p>The following are deemed to be "boolean" attributes, and will return either "true" or null:
     *
     * <p>async, autofocus, autoplay, checked, compact, complete, controls, declare, defaultchecked,
     * defaultselected, defer, disabled, draggable, ended, formnovalidate, hidden, indeterminate,
     * iscontenteditable, ismap, itemscope, loop, multiple, muted, nohref, noresize, noshade,
     * novalidate, nowrap, open, paused, pubdate, readonly, required, reversed, scoped, seamless,
     * seeking, selected, truespeed, willvalidate
     *
     * <p>Finally, the following commonly mis-capitalized attribute/property names are evaluated as
     * expected:
     *
     * <ul>
     * <li>If the given name is "class", the "className" property is returned.
     * <li>If the given name is "readonly", the "readOnly" property is returned.
     * </ul>
     *
     * <i>Note:</i> The reason for this behavior is that users frequently confuse attributes and
     * properties. If you need to do something more precise, e.g., refer to an attribute even when a
     * property of the same name exists, then you should evaluate Javascript to obtain the result
     * you desire.
     * </p>
     *
     * @param attribute The name of the attribute.
     * @return The attribute/property's current value or null if the value is not set.
     */
    String getAttribute(String attribute);
}
