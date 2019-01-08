package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumSteps;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

@MakeImageCapturesOnFinishing
public abstract class NavigationActionSupplier<T extends NavigationActionSupplier<T>>
        extends SequentialActionSupplier<SeleniumSteps, Window, T> {

    NavigationActionSupplier(String description) {
        super(description);
    }
}
