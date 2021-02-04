package ru.tinkoff.qa.neptune.selenium.test;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.selenium.auto.scrolling.AutoScroller;

public class MockScrollWebElementIntoView extends AutoScroller {

    public MockScrollWebElementIntoView(WebDriver driver) {
        super(driver);
    }

    @Override
    protected void scrollIntoView(WebElement e) {
        ((MockWebElement) e).scroll();
    }
}
