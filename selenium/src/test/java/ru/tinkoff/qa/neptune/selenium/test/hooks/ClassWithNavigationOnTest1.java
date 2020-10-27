package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.hooks.DefaultBrowserPage;
import ru.tinkoff.qa.neptune.selenium.hooks.ForceNavigation;
import ru.tinkoff.qa.neptune.selenium.hooks.PreventGettingBackToDefaultPage;

@DefaultBrowserPage(at = "https://www.google.com/")
public class ClassWithNavigationOnTest1 {

    public void test1() {

    }

    @PreventGettingBackToDefaultPage
    public void test2() {

    }

    @ForceNavigation(to = "https://github.com/")
    public void test3() {

    }
}
