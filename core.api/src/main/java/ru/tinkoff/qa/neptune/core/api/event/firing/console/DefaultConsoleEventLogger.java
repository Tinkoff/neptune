package ru.tinkoff.qa.neptune.core.api.event.firing.console;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;

import static java.lang.String.format;

public class DefaultConsoleEventLogger implements EventLogger {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private LinkedList<String> steps = new LinkedList<>();
    private boolean successful = true;

    @Override
    public void fireTheEventStarting(String message) {
        successful = true;
        steps.addLast(message);
        System.out.println(format("%s STEP HAS BEEN STARTED: %s ", DATE_FORMAT.format(new Date()), message));
    }

    @Override
    public void fireThrownException(Throwable throwable) {
        successful = false;
        System.err.println("The exception has been thrown!!!!");
        throwable.printStackTrace(System.err);
    }

    @Override
    public void fireReturnedValue(Object returned) {
        System.out.println(format("Returned: %s", returned));
    }

    @Override
    public void fireEventFinishing() {
        if (steps.size() > 0) {
            var step = steps.getLast();
            if (successful) {
                System.out.println(format("%s STEP HAS FINISHED SUCCESSFULLY: %s", DATE_FORMAT.format(new Date()), step));
            } else {
                System.err.println(format("%s STEP HAS FAILED: %s", DATE_FORMAT.format(new Date()), step));
            }
        }
        successful = true;
        steps.removeLast();
    }
}
