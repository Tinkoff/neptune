package ru.tinkoff.qa.neptune.http.api.properties.date.format;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.text.SimpleDateFormat;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.http.api.properties.date.format.ApiDateFormatSymbolsProperty.API_DATE_FORMAT_SYMBOLS_PROPERTY;
import static ru.tinkoff.qa.neptune.http.api.properties.date.format.ApiTimeZoneProperty.API_TIME_ZONE_PROPERTY;

@PropertyDescription(description = {"Defines a string pattern of a date format to build SimpleDateFormat",
        "which is used to serialize and deserialize dates",
        "Resulted SimpleDateFormat may be completed by values provided by",
        "API_DATE_FORMAT_SYMBOLS and API_DATE_FORMAT_TIME_ZONE properties"},
        section = "Http client. Default date format")
@PropertyName("API_DATE_FORMAT")
public final class ApiDateFormatProperty implements PropertySupplier<SimpleDateFormat, String> {

    public static final ApiDateFormatProperty API_DATE_FORMAT_PROPERTY = new ApiDateFormatProperty();

    private ApiDateFormatProperty() {
        super();
    }

    @Override
    public SimpleDateFormat parse(String value) {
        var format = new SimpleDateFormat(value);
        ofNullable(API_DATE_FORMAT_SYMBOLS_PROPERTY.get()).ifPresent(format::setDateFormatSymbols);
        ofNullable(API_TIME_ZONE_PROPERTY.get()).ifPresent(format::setTimeZone);
        return format;
    }
}
