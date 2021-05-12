package ru.tinkoff.qa.neptune.http.api.properties.date.format;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;

import java.util.TimeZone;

import static java.util.TimeZone.getTimeZone;

@PropertyDescription(description = {"Defines default time zone",
        "to build a SimpleDateFormat provided by the property API_DATE_FORMAT"},
        section = "Http client. Default date format")
@PropertyName("API_DATE_FORMAT_TIME_ZONE")
public final class ApiTimeZoneProperty implements PropertySupplier<TimeZone, String> {

    public static final ApiTimeZoneProperty API_TIME_ZONE_PROPERTY = new ApiTimeZoneProperty();

    private ApiTimeZoneProperty() {
        super();
    }

    @Override
    public TimeZone parse(String value) {
        return getTimeZone(value);
    }
}
