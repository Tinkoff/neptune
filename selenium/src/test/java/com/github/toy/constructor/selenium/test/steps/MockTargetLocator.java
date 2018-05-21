package com.github.toy.constructor.selenium.test.steps;

import com.github.toy.constructor.selenium.test.steps.enums.FrameIndexes;
import com.github.toy.constructor.selenium.test.steps.enums.FrameNames;
import com.github.toy.constructor.selenium.test.steps.enums.WindowHandles;
import org.openqa.selenium.*;

import java.util.LinkedList;
import java.util.List;

import static com.github.toy.constructor.selenium.test.steps.enums.WindowHandles.HANDLE1;
import static com.github.toy.constructor.selenium.test.steps.enums.WindowHandles.HANDLE2;
import static com.github.toy.constructor.selenium.test.steps.enums.WindowHandles.HANDLE3;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.contains;

public class MockTargetLocator implements WebDriver.TargetLocator {

    private final MockWebDriver driver;
    final List<String> handles = new LinkedList<>() {
        {
            add(0, HANDLE1.getHandle());
            add(1, HANDLE2.getHandle());
            add(2, HANDLE3.getHandle());
        }
    };
    String currentHandle;

    public MockTargetLocator(MockWebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver frame(int index) {
        if (!contains(stream(FrameIndexes.values()).map(FrameIndexes::getIndex).toArray(), index)) {
            throw new NoSuchFrameException(format("There is no frame found by index %s", index));
        }
        return driver;
    }

    @Override
    public WebDriver frame(String nameOrId) {
        if (!contains(stream(FrameNames.values()).map(FrameNames::getNameOrId).toArray(), nameOrId)) {
            throw new NoSuchFrameException(format("There is no frame found by name/id %s", nameOrId));
        }
        return driver;
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        //TODO
        return null;
    }

    @Override
    public WebDriver parentFrame() {
        return driver;
    }

    @Override
    public WebDriver window(String nameOrHandle) {
        if (!contains(stream(WindowHandles.values()).map(WindowHandles::getHandle).toArray(), nameOrHandle)) {
            throw new NoSuchWindowException(format("There is no window handle %s", nameOrHandle));
        }
        currentHandle = nameOrHandle;
        return driver;
    }

    @Override
    public WebDriver defaultContent() {
        return driver;
    }

    @Override
    public WebElement activeElement() {
        //TODO
        return null;
    }

    @Override
    public Alert alert() {
        return new MockAlert();
    }
}
