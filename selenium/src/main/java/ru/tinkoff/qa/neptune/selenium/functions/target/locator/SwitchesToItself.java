package ru.tinkoff.qa.neptune.selenium.functions.target.locator;

import org.openqa.selenium.WrapsDriver;

public interface SwitchesToItself extends WrapsDriver {
    void switchToMe();
}
