package ru.tinkoff.qa.neptune.allure;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import static io.qameta.allure.Allure.addAttachment;

public class AllureStringInjector implements CapturedStringInjector {

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        addAttachment(message, toBeInjected.toString());
    }
}
