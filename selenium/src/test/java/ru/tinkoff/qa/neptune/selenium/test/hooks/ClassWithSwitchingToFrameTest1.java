package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;

import static org.openqa.selenium.support.How.TAG_NAME;

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

    @SwitchToFrame(howToFindFrameElement = TAG_NAME, locatorValue = "valid_frame1")
    public void test4() {
    }

    @SwitchToFrame(howToFindFrameElement = TAG_NAME, locatorValue = "invalid_frame", waitingTime = 5)
    public void test5() {
    }
}
