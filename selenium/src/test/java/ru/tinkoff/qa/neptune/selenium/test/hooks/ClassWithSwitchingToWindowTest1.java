package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToWindow;

@SwitchToWindow(index = 1)
public class ClassWithSwitchingToWindowTest1 {

    public void test1() {
    }

    @SwitchToWindow(index = 2,
            title = "^.*\\b(Github)\\b.*$", url = "^.*\\b(github)\\b.*$")
    public void test2() {
    }

    @SwitchToWindow(index = 4, title = "Some tab", waitingTime = 5)
    public void test3() {
    }
}
