package ru.tinkoff.qa.neptune.selenium.test;

import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.logging.Logs;

import java.util.Set;

import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.POSITION_1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.POSITION_2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialPositions.POSITION_3;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.SIZE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.SIZE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.InitialSizes.SIZE3;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;

public class MockOptions implements WebDriver.Options {

    private final MockWindow window1;
    private final MockWindow window2;
    private final MockWindow window3;

    private final MockWebDriver driver;

    MockOptions(MockWebDriver driver) {
        this.driver = driver;
        window1 = new MockWindow(SIZE1.getSize(), POSITION_1.getPosition());
        window2 = new MockWindow(SIZE2.getSize(), POSITION_2.getPosition());
        window3 = new MockWindow(SIZE3.getSize(), POSITION_3.getPosition());
    }

    @Override
    public void addCookie(Cookie cookie) {
        //Todo Does nothing for now
    }

    @Override
    public void deleteCookieNamed(String name) {
        //Todo Does nothing for now
    }

    @Override
    public void deleteCookie(Cookie cookie) {
        //Todo Does nothing for now
    }

    @Override
    public void deleteAllCookies() {
        //Todo Does nothing for now
    }

    @Override
    public Set<Cookie> getCookies() {
        return null;
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
    public WebDriver.ImeHandler ime() {
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
