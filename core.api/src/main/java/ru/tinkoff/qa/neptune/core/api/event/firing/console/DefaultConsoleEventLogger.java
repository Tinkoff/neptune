package ru.tinkoff.qa.neptune.core.api.event.firing.console;

import ru.tinkoff.qa.neptune.core.api.event.firing.EventLogger;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;

public class DefaultConsoleEventLogger implements EventLogger {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");
    private String step;
    private boolean successful = true;

    @Override
    public void fireTheEventStarting(String message) {
        successful = true;
        step = message;
        System.out.println(format("%s. STEP '%s' has been started", DATE_FORMAT.format(new Date()), message));
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
        if (successful) {
            System.out.println(format("%s. STEP '%s' has finished successfully", DATE_FORMAT.format(new Date()), step));
        } else {
            System.err.println(format("%s. STEP '%s' has FAILED", DATE_FORMAT.format(new Date()), step));
        }
        successful = true;
        step = null;
    }
}
