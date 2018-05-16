package com.github.toy.constructor.selenium.functions.navigation;

import com.github.toy.constructor.core.api.SequentialActionSupplier;
import com.github.toy.constructor.selenium.SeleniumSteps;
import com.github.toy.constructor.selenium.functions.target.locator.window.Window;
import org.openqa.selenium.WebDriver;

import java.util.function.Consumer;

public abstract class NavigationActionSupplier<T extends NavigationActionSupplier<T>> extends SequentialActionSupplier<SeleniumSteps, Window, T> {

    private static final Consumer<Window> FORWARD = WebDriver.Navigation::forward;
    private static final Consumer<Window> BACK = WebDriver.Navigation::back;
    private static final Consumer<Window> REFRESH = WebDriver.Navigation::refresh;

    NavigationActionSupplier() {
        super();
    }
}
