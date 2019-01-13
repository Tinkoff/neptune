package ru.tinkoff.qa.neptune.selenium.test;

import org.openqa.selenium.*;

import java.util.List;

public class ActiveWebElement implements WebElement {

    public static WebElement activeWebElement;

    ActiveWebElement() {
        activeWebElement = this;
    }

    @Override
    public void click() {

    }

    @Override
    public void submit() {

    }

    @Override
    public void sendKeys(CharSequence... keysToSend) {

    }

    @Override
    public void clear() {

    }

    @Override
    public String getTagName() {
        return null;
    }

    @Override
    public String getAttribute(String name) {
        return null;
    }

    @Override
    public boolean isSelected() {
        return false;
    }

    @Override
    public boolean isEnabled() {
        return false;
    }

    @Override
    public String getText() {
        return null;
    }

    @Override
    public List<WebElement> findElements(By by) {
        return null;
    }

    @Override
    public WebElement findElement(By by) {
        return null;
    }

    @Override
    public boolean isDisplayed() {
        return false;
    }

    @Override
    public Point getLocation() {
        return null;
    }

    @Override
    public Dimension getSize() {
        return null;
    }

    @Override
    public Rectangle getRect() {
        return null;
    }

    @Override
    public String getCssValue(String propertyName) {
        return null;
    }

    @Override
    public <X> X getScreenshotAs(OutputType<X> target) throws WebDriverException {
        String base64EncodedPng = "iVBORw0KGgoAAAANSUhEUgAAAHUAAABkCAIAAAA37OjVAAAAAXNSR0IArs4c6QAAAARnQU1BAACx\n" +
                "jwv8YQUAAAAJcEhZcwAADsMAAA7DAcdvqGQAAANCSURBVHhe7do/a1NRHMZxX4hO6qJTumXrJgiC\n" +
                "i5NOdmqn0kUcChVcHQQHwUFwcBDcxUW6iC9AfBEFE9K/scXGJ5zD8XDOrQn1Pr9zkvt8OYO5bSL5\n" +
                "9Jf7r70yUczky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7cuuh7+mV3/8HD\n" +
                "8YeP/jGzzvkCd3Br5efVG4Prt/0mZt3yxcw6XLf8VmYd8j159z7IYh093fZfYNYh32Gvb4yLjHyx\n" +
                "1xv2V7GOdp7/3tvzWw07fvEy4J6Px34rPyNfyIa3hwPL8avX/gsmxbiHG5t+q0lGvgAN7xDL5tiN\n" +
                "zkcjfGLC/7v/6LHl8CLT/e+vT58h694qZspvpYUDWny2YI+LrI9v8TTxiHEeFu+RsIrgImtfvEm8\n" +
                "1fC2Wyd2B9Lw+lh4aHOp1pi1LyIR5zOLhaNZkbENFfBFrRPjFcKruVVc1lXGF7VCnO8N3DK7fJhZ\n" +
                "MV+UEANlzkuPs+8/cJzMZfFqNcxsXElflBDPvPTAKVfjwGJVskNIKuyLgHKwth5L5YPceOwKq569\n" +
                "QV55XxcuPUZ37gWyMMiNssNeH6anX7+559ZcLb4oH+TR3fvxQ7fq3A9cVEW+rr+DfO3m4rKGqvOd\n" +
                "3pHZfhbLutX6lZ5Ndfkmd2SwBtG/F5G4Ft+L7shgxSdw+B78DPxzFqHyvsktWocY35FJiLEWaJBL\n" +
                "+s5/RwZbDreexN+W/AyqrZhvfkdm5tVtMsg4R67/LLiAbz628w9jPsjTp/f6J2/e+u+oLGvfZGxn\n" +
                "zmxjyW/zsDDLdR737Hzze4n/ecng7qKFX+hhVXjcs/ON/7zjcmPbWDLLtRHb+YZBa/1KF68WH/fw\n" +
                "KalnX2Hn6waNdC8xIcb6931ks6yPb7xAHJ9a4OPS7qfkci2Pr2uqvLEJX1j7TUVbNl9XkT8hbGw5\n" +
                "fetJvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTL\n" +
                "Tb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+3OTLTb7c5MtNvtzky02+\n" +
                "3OTLTb7MJpM/U7hf1q6DGbAAAAAASUVORK5CYII=";
        return target.convertFromBase64Png(base64EncodedPng);
    }

    @Override
    public String toString() {
        return "Active web element";
    }
}
