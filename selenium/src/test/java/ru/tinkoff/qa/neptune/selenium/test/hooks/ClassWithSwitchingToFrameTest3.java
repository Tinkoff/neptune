package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.HowToUseDefaultBrowserContent;
import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToWindow;

import static org.openqa.selenium.support.How.TAG_NAME;
import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_METHOD;

@SwitchToFrame(index = 1)
@HowToUseDefaultBrowserContent(howOften = FOR_EVERY_METHOD, addFrameParams = true)
public class ClassWithSwitchingToFrameTest3 {

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
