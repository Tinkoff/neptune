package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToFrame;
import ru.tinkoff.qa.neptune.selenium.content.management.SwitchToWindow;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_METHOD;

@Navigate(to = "https://www.google.com")
@UseDefaultBrowserContent(howOften = FOR_EVERY_METHOD,
        addWindowParams = true,
        addNavigationParams = true,
        addFrameParams = true)
@SwitchToFrame(index = 1)
@SwitchToWindow(index = 1)
public class ClassWithNavigationOnTest2 {

    public void test1() {
    }

    @Navigate(to = "https://github.com")
    public void test3() {
    }
}
