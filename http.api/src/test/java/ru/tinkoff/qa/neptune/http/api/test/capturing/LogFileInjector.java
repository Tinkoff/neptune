package ru.tinkoff.qa.neptune.http.api.test.capturing;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedFileInjector;

import java.io.File;
import java.util.ArrayList;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.LOGS;
import static ru.tinkoff.qa.neptune.http.api.test.capturing.LogInjector.LOG_LIST;

public class LogFileInjector implements CapturedFileInjector {

    @Override
    public void inject(File toBeInjected, String message) {
        var logs = ofNullable(LOGS.get())
                .orElseGet(() -> {
                    var result = new ArrayList<String>();
                    LOGS.set(result);
                    LOG_LIST.add(result);
                    return result;
                });

        logs.add(message);
    }
}
