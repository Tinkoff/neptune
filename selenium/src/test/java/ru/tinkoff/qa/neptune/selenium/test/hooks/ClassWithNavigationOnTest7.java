package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;

@Navigate(to = "https://www.google.com")
public class ClassWithNavigationOnTest7 {

    @SwitchToFrame(index = 1)
    public void test1() {
    }

    public void test2() {
    }
}
