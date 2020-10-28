package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.hooks.DefaultBrowserPage;
import ru.tinkoff.qa.neptune.selenium.hooks.ForceNavigation;
import ru.tinkoff.qa.neptune.selenium.hooks.PreventNavigationToDefaultURL;

@DefaultBrowserPage(at = "https://www.google.com/")
public class ClassWithNavigationOnTest1 {

    public void test1() {

    }

    @PreventNavigationToDefaultURL
    public void test2() {

    }

    @ForceNavigation(to = "https://github.com/")
    public void test3() {

    }
}
