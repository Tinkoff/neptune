package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToWindow;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static org.openqa.selenium.support.How.TAG_NAME;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_METHOD;

@SwitchToFrame(index = 1)
@UseDefaultBrowserContent(FOR_EVERY_METHOD)
public class ClassWithSwitchingToFrameTest2 {

    @SwitchToWindow(index = 1)
    public void test1() {
    }

    @Navigate(to = "https://github.com")
    public void test2() {
    }

    @SwitchToWindow(index = 2)
    @SwitchToFrame(howToFindFrameElement = TAG_NAME, locatorValue = "valid_frame1")
    public void test3() {

    }
}
