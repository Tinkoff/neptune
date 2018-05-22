package com.github.toy.constructor.selenium.test.steps;

import com.github.toy.constructor.selenium.test.steps.enums.URLs;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.LinkedList;

public class MockNavigation implements WebDriver.Navigation {
    private final MockWebDriver driver;

    public MockNavigation(MockWebDriver driver) {
        this.driver = driver;
    }

    @Override
    public void back() {
        String handle = driver.getWindowHandle();
        URLs url = driver.currentUrls.get(handle);
        LinkedList<URLs> history = driver.handlesAndUrlHistory.get(handle);
        int index = history.indexOf(url);
        if (index > 0) {
            URLs previousUrl = history.listIterator(index).previous();
            driver.changeCurrentUrl(handle, previousUrl);
        }
    }

    @Override
    public void forward() {
        String handle = driver.getWindowHandle();
        URLs url = driver.currentUrls.get(handle);
        LinkedList<URLs> history = driver.handlesAndUrlHistory.get(handle);
        int index = history.indexOf(url);
        if (index < history.size() -1 ) {
            URLs nextUrl = history.listIterator(index + 1).next();
            driver.changeCurrentUrl(handle, nextUrl);
        }
    }

    public void to(String url) {
        driver.get(url);
    }

    public void to(URL url) {
        driver.get(String.valueOf(url));
    }

    @Override
    public void refresh() {
        ((MockWindow) driver.manage().window()).setRefreshed(true);
    }
}
