package ru.tinkoff.qa.neptune.selenium.test.webdriver.starting;

import org.openqa.selenium.WebDriver;
import ru.tinkoff.qa.neptune.selenium.properties.WebDriverTuner;

import java.util.ArrayList;
import java.util.List;

public class TestWebDriverTuner implements WebDriverTuner {

    public final List<String> actions = new ArrayList<>();

    @Override
    public void tuneDriver(WebDriver webDriver, boolean isNewSession) {
        if (isNewSession) {
            actions.add("created");
            return;
        }
        actions.add("refreshed");
    }
}
