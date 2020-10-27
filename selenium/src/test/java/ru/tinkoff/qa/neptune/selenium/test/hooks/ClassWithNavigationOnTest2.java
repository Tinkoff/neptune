package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.hooks.DefaultBrowserPage;
import ru.tinkoff.qa.neptune.selenium.hooks.ForceNavigation;
import ru.tinkoff.qa.neptune.selenium.hooks.PreventGettingBackToDefaultPage;

import static ru.tinkoff.qa.neptune.selenium.hooks.DefaultNavigationStrategies.ON_EVERY_METHOD;

@DefaultBrowserPage(at = "https://www.google.com/", when = ON_EVERY_METHOD)
public class ClassWithNavigationOnTest2 {

    public void test1() {

    }

    @PreventGettingBackToDefaultPage
    public void test2() {

    }

    @ForceNavigation(to = "https://github.com/")
    public void test3() {

    }
}
