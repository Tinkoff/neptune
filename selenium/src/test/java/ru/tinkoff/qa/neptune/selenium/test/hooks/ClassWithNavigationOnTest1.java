package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;

@Navigate(to = "https://www.google.com")
public class ClassWithNavigationOnTest1 {

    public void test1() {
    }

    @Navigate(to = "https://github.com")
    public void test2() {
    }
}
