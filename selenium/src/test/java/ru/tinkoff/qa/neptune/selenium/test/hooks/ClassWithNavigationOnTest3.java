package ru.tinkoff.qa.neptune.selenium.test.hooks;

import ru.tinkoff.qa.neptune.selenium.content.management.Navigate;
import ru.tinkoff.qa.neptune.selenium.content.management.UseDefaultBrowserContent;

import static ru.tinkoff.qa.neptune.selenium.content.management.BrowserContentUsage.FOR_EVERY_TEST_METHOD;

@Navigate(to = "https://www.google.com")
@UseDefaultBrowserContent(howOften = FOR_EVERY_TEST_METHOD)
public class ClassWithNavigationOnTest3 {

    public void test1() {
    }

    @Navigate(to = "https://github.com")
    public void test2() {
    }
}
