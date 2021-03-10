package ru.tinkoff.qa.neptune.selenium.api.widget;

public interface Expandable {

    /**
     * Expanding a widget
     */
    void expand();

    /**
     * @return is widget expanded or not
     */
    boolean isExpanded();

    /**
     * Collapsing a widget
     */
    void collapse();
}
