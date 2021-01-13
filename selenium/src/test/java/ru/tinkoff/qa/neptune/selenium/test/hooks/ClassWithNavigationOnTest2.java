package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_METHOD;

@Navigate(to = "https://www.google.com")
@UseDefaultBrowserContent(FOR_EVERY_METHOD)
public class ClassWithNavigationOnTest2 {

    public void test1() {
    }

    @Navigate(to = "https://github.com")
    public void test3() {
    }
}
