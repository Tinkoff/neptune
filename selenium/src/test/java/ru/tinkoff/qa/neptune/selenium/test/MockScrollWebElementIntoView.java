package ru.tinkoff.qa.neptune.selenium.test;

import ru.tinkoff.qa.neptune.selenium.functions.searching.ScrollWebElementIntoView;

import java.lang.reflect.Method;

public class MockScrollWebElementIntoView extends ScrollWebElementIntoView {

    public MockScrollWebElementIntoView(MockWebElement elementToBeScrolledIntoView) {
        super(elementToBeScrolledIntoView);
    }

    @Override
    protected boolean needsForTheScrolling(Method method) {
        return method
                .getName()
                .equals("click");
    }

    public void scrollIntoView() {
        ((MockWebElement) getWrappedElement()).scroll();
    }
}
