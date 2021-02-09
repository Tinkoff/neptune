package ru.tinkoff.qa.neptune.selenium.functions.target.locator.window;

import org.openqa.selenium.*;

import java.net.URL;
import java.util.function.Consumer;
import java.util.function.Function;

import static java.lang.String.format;

class DefaultWindow implements Window, TakesScreenshot {

    private final String handle;
    private final WebDriver driver;

    DefaultWindow(String handle, WebDriver driver) {
        this.handle = handle;
        this.driver = driver;
    }

    private static WebDriver.Window currentWindow(WebDriver driver) {
        return driver.manage().window();
    }

    @Override
    public Dimension getSize() {
        return switchToMeAndReturn(webDriver -> currentWindow(webDriver).getSize());
    }

    @Override
    public void setSize(Dimension targetSize) {
        switchToMeAndDo(webDriver -> currentWindow(webDriver).setSize(targetSize));
    }

    @Override
    public Point getPosition() {
        return switchToMeAndReturn(webDriver -> currentWindow(webDriver).getPosition());
    }

    @Override
    public void setPosition(Point targetPosition) {
        switchToMeAndDo(webDriver -> currentWindow(webDriver).setPosition(targetPosition));
    }

    @Override
    public void maximize() {
        switchToMeAndDo(webDriver -> currentWindow(webDriver).maximize());
    }

    @Override
    public void fullscreen() {
        switchToMeAndDo(webDriver -> currentWindow(webDriver).fullscreen());
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
        return switchToMeAndReturn(WebDriver::getTitle);
    }

    @Override
    public String getCurrentUrl() {
        return switchToMeAndReturn(WebDriver::getCurrentUrl);
    }

    public void switchToMe() {
        if (isPresent()) {
            driver.switchTo().window(handle);
            return;
        }
        throw new NoSuchWindowException(format("%s is not present", this.toString()));
    }

    private void switchToMeAndDo(Consumer<WebDriver> toDo) {
        var currentHandle = driver.getWindowHandle();
        try {
            switchToMe();
            toDo.accept(driver);
        } finally {
            driver.switchTo().window(currentHandle);
        }
    }

    private <T> T switchToMeAndReturn(Function<WebDriver, T> toGet) {
        var currentHandle = driver.getWindowHandle();
        try {
            switchToMe();
            return toGet.apply(driver);
        } finally {
            driver.switchTo().window(currentHandle);
        }
    }


    @Override
    public void back() {
        switchToMeAndDo(webDriver -> webDriver.navigate().back());
    }

    @Override
    public void forward() {
        switchToMeAndDo(webDriver -> webDriver.navigate().forward());
    }

    @Override
    public void to(String url) {
        switchToMeAndDo(webDriver -> webDriver.navigate().to(url));
    }

    @Override
    public void to(URL url) {
        switchToMeAndDo(webDriver -> webDriver.navigate().to(url));
    }

    @Override
    public void refresh() {
        switchToMeAndDo(webDriver -> webDriver.navigate().refresh());
    }

    @Override
    public String toString() {
        return format("Window[url %s title %s]", getCurrentUrl(), getTitle());
    }

    @Override
    public WebDriver getWrappedDriver() {
        return driver;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        return switchToMeAndReturn(webDriver -> ((TakesScreenshot) webDriver).getScreenshotAs(target));
    }
}
