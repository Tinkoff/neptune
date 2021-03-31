package ru.tinkoff.qa.neptune.http.api.properties.date.format;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectPropertySupplier;

import java.text.DateFormatSymbols;
import java.util.function.Supplier;

@PropertyDescription(description = {"Defines full name of a class which implements Supplier<DateFormatSymbols> and whose objects",
        "supply instances of java.text.DateFormatSymbols",
        "It is used to build a SimpleDateFormat provided by the property API_DATE_FORMAT"},
        section = "Http client. Default date format")
@PropertyName("API_DATE_FORMAT_SYMBOLS")
public final class ApiDateFormatSymbolsProperty implements ObjectPropertySupplier<DateFormatSymbols, Supplier<DateFormatSymbols>> {

    public static final ApiDateFormatSymbolsProperty API_DATE_FORMAT_SYMBOLS_PROPERTY = new ApiDateFormatSymbolsProperty();

    private ApiDateFormatSymbolsProperty() {
        super();
    }
}
