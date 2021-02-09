package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;

@SwitchToFrame(index = 2)
public class ClassWithSwitchingToFrameTest1 {

    public void test1() {
    }

    @SwitchToFrame(index = 1)
    public void test2() {
    }

    @SwitchToFrame(nameOrId = "name2")
    public void test3() {
    }

    @SwitchToFrame(tagName = "valid_frame1")
    public void test4() {
    }

    @SwitchToFrame(tagName = "invalid_frame", waitingTime = 5)
    public void test5() {
    }
}
