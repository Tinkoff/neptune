package com.github.toy.constructor.selenium.test.steps;

import org.openqa.selenium.*;

import java.util.List;
import java.util.stream.Collectors;

import static java.lang.String.format;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.ArrayUtils.contains;

public class MockTargetLocator implements WebDriver.TargetLocator {

    public static int[] FRAME_INDEXES = {1, 2, 3};
    public static String[] FRAME_NAMES_IDS = {"name1", "name2", "id1"};
    public static String[] WINDOW_HANDLES = {"handle1", "handle2", "handle3"};

    private final WebDriver driver;
    final List<String> handles = stream(WINDOW_HANDLES).collect(Collectors.toList());
    String currentHandle;

    public MockTargetLocator(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver frame(int index) {
        if (!contains(FRAME_INDEXES, index)) {
            throw new NoSuchFrameException(format("There is no frame found by index %s", index));
        }
        return driver;
    }

    @Override
    public WebDriver frame(String nameOrId) {
        if (!contains(FRAME_NAMES_IDS, nameOrId)) {
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
        if (!contains(WINDOW_HANDLES, nameOrHandle)) {
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
