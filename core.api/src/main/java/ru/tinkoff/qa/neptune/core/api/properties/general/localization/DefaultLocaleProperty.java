package ru.tinkoff.qa.neptune.core.api.properties.general.localization;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.util.IllformedLocaleException;
import java.util.Locale;

import static org.apache.commons.lang3.LocaleUtils.toLocale;

@PropertyDescription(description = "Defines default locale",
        section = "Report localization")
@PropertyName("DEFAULT_LOCALE")
public class DefaultLocaleProperty implements PropertySupplier<Locale, Locale> {
    public static final DefaultLocaleProperty DEFAULT_LOCALE_PROPERTY = new DefaultLocaleProperty();

    private DefaultLocaleProperty() {
        super();
    }

    @Override
    public Locale parse(String s) {
        try {
            return toLocale(s);
        } catch (IllformedLocaleException e) {
            throw new IllegalArgumentException(e.getMessage(), e);
        }
    }
}
