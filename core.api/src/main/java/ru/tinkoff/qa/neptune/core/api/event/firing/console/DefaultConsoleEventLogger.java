package ru.tinkoff.qa.neptune.core.api.event.firing.console;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.Map;

import static ru.tinkoff.qa.neptune.core.api.utils.ToArrayUtil.stringValueOfObjectOrArray;

public class DefaultConsoleEventLogger implements EventLogger {
    private final SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private final LinkedList<String> steps = new LinkedList<>();
    private boolean successful = true;

    @Override
    public void fireTheEventStarting(String message, Map<String, String> parameters) {
        successful = true;
        steps.addLast(message);
        System.out.println(dateFormat.format(new Date()) + " STEP HAS BEEN STARTED: " + message);
        if (parameters.isEmpty()) {
            return;
        }
        System.out.println("PARAMETERS:");
        parameters.forEach((s, s2) -> System.out.println("  - " + s + ": " + s2));
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        successful = false;
        System.err.println("The exception has been thrown!!!!");
        throwable.printStackTrace(System.err);
    }

    @Override
    public void fireReturnedValue(String resultDescription, Object returned) {
        System.out.printf("%s: %s%n", resultDescription, stringValueOfObjectOrArray(returned));
    }

    @Override
    public void fireEventFinishing() {
        if (!steps.isEmpty()) {
            var step = steps.getLast();
            if (successful) {
                System.out.println(dateFormat.format(new Date()) + " STEP HAS FINISHED SUCCESSFULLY: " + step);
            } else {
                System.err.println(dateFormat.format(new Date()) + " STEP HAS FAILED: " + step);
            }
            steps.removeLast();
        }
        successful = true;
    }

    @Override
    public void addParameters(Map<String, String> parameters) {
        parameters.forEach((s, s2) -> System.out.println("  - " + s + ": " + s2));
    }
}
