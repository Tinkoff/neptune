package ru.tinkoff.qa.neptune.allure;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import static io.qameta.allure.Allure.addAttachment;
import static ru.tinkoff.qa.neptune.allure.lifecycle.ItemsToNotBeReported.toReport;

public class AllureStringInjector implements CapturedStringInjector {

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        if (!toReport()) {
            return;
        }

        addAttachment(message, toBeInjected.toString());
    }
}
