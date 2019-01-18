package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import org.openqa.selenium.WrapsDriver;
import ru.tinkoff.qa.neptune.core.api.steps.LoggableObject;

public interface SwitchesToItself extends WrapsDriver, LoggableObject {
    void switchToMe();
}
