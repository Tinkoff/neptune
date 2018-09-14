package ru.tinkoff.qa.neptune.core.api.event.firing;

import static java.lang.String.format;

class DefaultConsoleDataInjector implements CapturedDataInjector<String> {

    @Override
    public void inject(String toBeInjected, String message) {
        System.out.println(message);
        System.out.println(format("- RESULT: %s", toBeInjected));
    }
}
