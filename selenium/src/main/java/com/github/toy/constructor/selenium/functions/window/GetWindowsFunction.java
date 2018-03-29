package com.github.toy.constructor.selenium.functions.window;

import com.github.toy.constructor.selenium.SeleniumSteps;
import org.openqa.selenium.WebDriver;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.github.toy.constructor.core.api.StoryWriter.toGet;

class GetWindowsFunction implements Function<SeleniumSteps, List<Window>> {

    static Function<SeleniumSteps, List<Window>> getWindows() {
        return toGet("Browser/web windows/tabs", new GetWindowsFunction());
    }

    @Override
    public List<Window> apply(SeleniumSteps seleniumSteps) {
        WebDriver driver = seleniumSteps.getWrappedDriver();
        return driver.getWindowHandles()
                .stream().map(s -> new DefaultWindow(s, driver)).collect(Collectors.toList());
    }
}
