package ru.tinkoff.qa.neptune.core.api.properties.url;

import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * This interface is designed to read properties and return an URL.
 */
public interface URLValuePropertySupplier extends PropertySupplier<URL> {

    @Override
    default URL parse(String s) {
        try {
            return new URL(s);
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
