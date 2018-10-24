package ru.tinkoff.qa.neptune.core.api.event.firing;

import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.String.format;
import static java.lang.String.valueOf;
import static java.util.List.of;

/**
 * This is the simple captor that transforms objects to string for the further
 * printing into console.
 */
public class DefaultConsoleCaptor extends Captor<Object, String> {
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss");

    public DefaultConsoleCaptor() {
        super(of(new DefaultConsoleDataInjector()));
    }

    public void capture(Object caught, String message) {
        super.capture(caught, format("%s STEP: %s", DATE_FORMAT.format(new Date()), message));
    }

    @Override
    protected String getData(Object caught) {
        return valueOf(caught);
    }

    @Override
    public Class<Object> getTypeToBeCaptured() {
        return Object.class;
    }
}
