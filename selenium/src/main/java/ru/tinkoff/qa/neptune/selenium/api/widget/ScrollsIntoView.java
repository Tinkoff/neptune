package ru.tinkoff.qa.neptune.selenium.api.widget;

import com.google.common.annotations.Beta;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;

@Beta
public interface ScrollsIntoView {

    /**
     * This method should scroll an element or widget into view.
     * <p>NOTE!</p>
     * When some subclass of the {@link Widget} implements {@link ScrollsIntoView} and an instance is received with
     * some {@link SearchSupplier} then this method is invoked implicitly every time before any method annotated by
     * {@link NeedToScrollIntoView}.
     */
    void scrollIntoView();
}
