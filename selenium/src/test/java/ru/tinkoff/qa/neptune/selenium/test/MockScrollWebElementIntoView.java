package ru.tinkoff.qa.neptune.selenium.test;

import ru.tinkoff.qa.neptune.selenium.functions.searching.ScrollsIntoView;

public class MockScrollWebElementIntoView extends ScrollsIntoView.ScrollWebElementIntoView {

    public MockScrollWebElementIntoView(MockWebElement elementToBeScrolledIntoView) {
        super(elementToBeScrolledIntoView);
    }

    @Override
    public void scrollsIntoView() {
        ((MockWebElement) getWrappedElement()).scroll();
    }
}
