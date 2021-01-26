package ru.tinkoff.qa.neptune.selenium.functions.target.locator.frame;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.SwitchesToItself;

import static java.lang.String.format;

public class Frame implements SwitchesToItself {
    private final WebDriver webDriver;
    private final Object frame;

    Frame(WebDriver webDriver, Object frame) {
        this.webDriver = webDriver;
        this.frame = frame;
        try {
            switchToMe();
        }
        finally {
            webDriver.switchTo().parentFrame();
        }
    }

    @Override
    public void switchToMe() {
        Class<?> clazz = frame.getClass();
        if (Integer.class.isAssignableFrom(clazz)) {
            webDriver.switchTo().frame((Integer) frame);
            return;
        }

        if (String.class.isAssignableFrom(clazz)) {
            webDriver.switchTo().frame((String) frame);
            return;
        }

        if (By.class.isAssignableFrom(clazz)) {
            var e = webDriver.findElement((By) frame);
            webDriver.switchTo().frame(e);
        }

    }

    public String toString() {
        return format("frame %s", frame);
    }
}
