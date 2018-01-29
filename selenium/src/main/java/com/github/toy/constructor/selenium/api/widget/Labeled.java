package com.github.toy.constructor.selenium.api.widget;

import java.util.List;

public interface Labeled {

    /**
     * It is possible to find some elements by text inside the element or near.
     * Also some elements with not empty text are used as label/description. Also
     * some text attributes could be considered/used as text labels.
     *
     * @return list of text values which can be used by the searching.
     */
    List<String> labels();
}
