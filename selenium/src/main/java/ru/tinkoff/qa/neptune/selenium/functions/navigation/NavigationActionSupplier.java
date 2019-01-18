package ru.tinkoff.qa.neptune.selenium.functions.navigation;

import ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeFileCapturesOnFinishing;
import ru.tinkoff.qa.neptune.core.api.event.firing.annotation.MakeImageCapturesOnFinishing;
import ru.tinkoff.qa.neptune.selenium.SeleniumStepContext;
import ru.tinkoff.qa.neptune.selenium.functions.target.locator.window.Window;

@MakeImageCapturesOnFinishing
@MakeFileCapturesOnFinishing
public abstract class NavigationActionSupplier<T extends NavigationActionSupplier<T>>
        extends SequentialActionSupplier<SeleniumStepContext, Window, T> {

    NavigationActionSupplier(String description) {
        super(description);
    }
}
