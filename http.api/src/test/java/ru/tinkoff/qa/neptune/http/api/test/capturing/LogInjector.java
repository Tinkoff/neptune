package ru.tinkoff.qa.neptune.http.api.test.capturing;

import ru.tinkoff.qa.neptune.core.api.event.firing.captors.CapturedStringInjector;

import java.util.ArrayList;
import java.util.List;

import static java.util.Optional.ofNullable;

public class LogInjector implements CapturedStringInjector {

    private final static ThreadLocal<List<String>> LOGS = new ThreadLocal<>();
    private final static List<List<String>> LOG_LIST = new ArrayList<>();

    static void clearLogs() {
        LOG_LIST.forEach(List::clear);
    }

    static List<String> getLog() {
        return LOGS.get();
    }

    @Override
    public void inject(StringBuilder toBeInjected, String message) {
        var logs = ofNullable(LOGS.get())
                .orElseGet(() -> {
                    var result = new ArrayList<String>();
                    LOGS.set(result);
                    LOG_LIST.add(result);
                    return result;
                });

        logs.add(message + "\n" + toBeInjected.toString());
    }
}
