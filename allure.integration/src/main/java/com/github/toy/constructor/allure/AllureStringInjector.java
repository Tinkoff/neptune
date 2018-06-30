package com.github.toy.constructor.allure;

import com.github.toy.constructor.core.api.event.firing.captors.CapturedStringInjector;

import static io.qameta.allure.Allure.addAttachment;

public class AllureStringInjector implements CapturedStringInjector {

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        addAttachment(message, toBeInjected.toString());
    }
}
