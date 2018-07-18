package ru.tinkoff.qa.neptune.selenium.test;

import ru.tinkoff.qa.neptune.selenium.test.enums.FrameIndexes;
import ru.tinkoff.qa.neptune.selenium.test.enums.FrameNames;
import ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles;
import org.apache.commons.lang3.ArrayUtils;
import org.openqa.selenium.*;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE1;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE2;
import static ru.tinkoff.qa.neptune.selenium.test.enums.WindowHandles.HANDLE3;
import static java.lang.String.format;
import static java.util.Arrays.stream;
import static java.util.Optional.ofNullable;
import static org.apache.commons.lang3.ArrayUtils.contains;

public class MockTargetLocator implements WebDriver.TargetLocator {

    private final MockWebDriver driver;
    private final MockAlert alert = new MockAlert();

    final List<String> handles = new LinkedList<>() {
        {
            add(0, HANDLE1.getHandle());
            add(1, HANDLE2.getHandle());
            add(2, HANDLE3.getHandle());
        }
    };
    String currentHandle;
    private WebElement activeElement;

    MockTargetLocator(MockWebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver frame(int index) {
        if (!ArrayUtils.contains(Arrays.stream(FrameIndexes.values()).map(FrameIndexes::getIndex).toArray(), index)) {
            throw new NoSuchFrameException(format("There is no frame found by index %s", index));
        }
        return driver.setFrame(index);
    }

    @Override
    public WebDriver frame(String nameOrId) {
        if (!ArrayUtils.contains(Arrays.stream(FrameNames.values()).map(FrameNames::getNameOrId).toArray(), nameOrId)) {
            throw new NoSuchFrameException(format("There is no frame found by name/id %s", nameOrId));
        }
        return driver.setFrame(nameOrId);
    }

    @Override
    public WebDriver frame(WebElement frameElement) {
        if (frameElement instanceof ValidFrameWebElement) {
            return driver.setFrame(frameElement);
        }
        throw new NoSuchFrameException(format("There is no frame found inside %s", frameElement.getClass().getSimpleName()));
    }

    @Override
    public WebDriver parentFrame() {
        return driver.setSwitchedToParentFrame(true);
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
        return driver.setSwitchedToDefaultContent(true);
    }

    @Override
    public WebElement activeElement() {
        activeElement = (WebElement) ofNullable(activeElement).map(webElement -> {
            throw new WebDriverException("Test exception");
        }).orElseGet(ActiveWebElement::new);
        return activeElement;
    }

    @Override
    public Alert alert() {
        return alert.setSwitchedTo(true);
    }

    public MockAlert getAlert() {
        return alert;
    }
}
