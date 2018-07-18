package ru.tinkoff.qa.neptune.selenium.api.widget;

import org.openqa.selenium.SearchContext;

import java.util.List;

public interface Labeled extends SearchContext {

    /**
     * It is possible to find some elements by text inside the element or near.
     * Also some elements with not empty text are used as label/description. Also
     * some text attributes could be considered/used as text labels.
     *
     * @return list of text values which can be used by the searching.
     */
    List<String> labels();
}
