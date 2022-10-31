package ru.tinkoff.qa.neptune.selenium.test;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.*;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;

public class MockOptions implements WebDriver.Options {

    private final MockWindow window1;
    private final MockWindow window2;
    private final MockWindow window3;

    private final MockWebDriver driver;

    private final Set<Cookie> cookieJar = new HashSet<>(){
        {
            add(new Cookie("key1", "value1", "youtube.com", "some/path", new Date(), true));
            add(new Cookie("key2", "value2", "www.google.com", "some/path2", new Date(), false));
            add(new Cookie("key3", "value3", "github.com", "some/path3", new Date(), false));
            add(new Cookie("key4", "value4", "paypal.com", "some/path4", new Date(), true));
            add(new Cookie("key5", "value5", "deezer.com", "some/path5", new Date(), false));
        }
    };

    MockOptions(MockWebDriver driver) {
        this.driver = driver;
        window1 = new MockWindow(SIZE1.getSize(), POSITION_1.getPosition());
        window2 = new MockWindow(SIZE2.getSize(), POSITION_2.getPosition());
        window3 = new MockWindow(SIZE3.getSize(), POSITION_3.getPosition());
    }

    @Override
    public void addCookie(Cookie cookie) {
        cookieJar.add(cookie);
    }

    @Override
    public void deleteCookieNamed(String name) {
        //Todo Does nothing for now
    }

    @Override
    public void deleteCookie(Cookie cookie) {
        cookieJar.remove(cookie);
    }

    @Override
    public void deleteAllCookies() {
        cookieJar.clear();
    }

    @Override
    public Set<Cookie> getCookies() {
        return new HashSet<>(cookieJar);
    }

    @Override
    public Cookie getCookieNamed(String name) {
        return null;
    }

    @Override
    public WebDriver.Timeouts timeouts() {
        return null;
    }

    @Override
    public WebDriver.Window window() {
        String handle = driver.getWindowHandle();
        if (handle.equals(HANDLE1.getHandle())) {
            return window1;
        }

        if (handle.equals(HANDLE2.getHandle())) {
            return window2;
        }

        return window3;
    }

    @Override
    public Logs logs() {
        return null;
    }
}
