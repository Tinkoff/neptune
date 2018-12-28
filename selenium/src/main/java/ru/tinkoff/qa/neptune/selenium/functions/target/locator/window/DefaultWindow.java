package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.*;

import java.net.URL;

import static java.lang.String.format;

class DefaultWindow implements Window, TakesScreenshot {

    private final String handle;
    private final WebDriver driver;

    DefaultWindow(String handle, WebDriver driver) {
        this.handle = handle;
        this.driver = driver;
    }

    @Override
    public void setSize(Dimension targetSize) {
        switchToMe();
        currentWindow().setSize(targetSize);
    }

    @Override
    public void setPosition(Point targetPosition) {
        switchToMe();
        currentWindow().setPosition(targetPosition);
    }

    @Override
    public Dimension getSize() {
        switchToMe();
        return currentWindow().getSize();
    }

    @Override
    public Point getPosition() {
        switchToMe();
        return currentWindow().getPosition();
    }

    @Override
    public void maximize() {
        switchToMe();
        currentWindow().maximize();
    }

    @Override
    public void fullscreen() {
        switchToMe();
        currentWindow().fullscreen();
    }

    @Override
    public void close() {
        switchToMe();
        driver.close();
    }

    @Override
    public boolean isPresent() {
        return driver.getWindowHandles().contains(handle);
    }

    @Override
    public String getTitle() {
        switchToMe();
        return driver.getTitle();
    }

    @Override
    public String getCurrentUrl() {
        switchToMe();
        return driver.getCurrentUrl();
    }

    public void switchToMe() {
        if (isPresent()) {
            driver.switchTo().window(handle);
            return;
        }
        throw new NoSuchWindowException(format("%s is not present", this.toString()));
    }

    private WebDriver.Window currentWindow() {
        return driver.manage().window();
    }

    @Override
    public void back() {
        switchToMe();
        driver.navigate().back();
    }

    @Override
    public void forward() {
        switchToMe();
        driver.navigate().forward();
    }

    @Override
    public void to(String url) {
        switchToMe();
        driver.navigate().to(url);
    }

    @Override
    public void to(URL url) {
        switchToMe();
        driver.navigate().to(url);
    }

    @Override
    public void refresh() {
        switchToMe();
        driver.navigate().refresh();
    }

    @Override
    public String toString() {
        return format("Window: url %s, title %s", getCurrentUrl(), getTitle());
    }

    @Override
    public WebDriver getWrappedDriver() {
        switchToMe();
        return driver;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return ((TakesScreenshot) driver).getScreenshotAs(target);
    }
}
