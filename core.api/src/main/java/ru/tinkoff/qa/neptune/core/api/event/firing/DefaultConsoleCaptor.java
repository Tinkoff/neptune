package ru.tinkoff.qa.neptune.core.api.event.firing;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import static java.lang.String.format;

/**
 * This is the simple captor that transforms objects to string for the further
 * printing to console.
 */
public class DefaultConsoleCaptor extends Captor<Object, String> {
    private static SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public DefaultConsoleCaptor() {
        super(List.of(new DefaultConsoleDataInjector()));
    }

    public void capture(Object caught, String message) {
        super.capture(caught, format("%s STEP: %s", DATE_FORMAT.format(new Date()), message));
    }

    @Override
    protected String getData(Object caught) {
        return String.valueOf(caught);
    }

    @Override
    public Class<Object> getTypeToBeCaptured() {
        return Object.class;
    }
}
