package ru.tinkoff.qa.neptune.jupiter.integration.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.MultipleEnumPropertySuppler;

import static ru.tinkoff.qa.neptune.jupiter.integration.properties.RefreshEachTimeBefore.*;

@PropertyDescription(description = {"Defines when (which methods) it is necessary to refresh contexts/resources,",
        "e.g. back to the base browser page, renew db/http connections etc.",
        "Available values: ALL_STARTING, EACH_STARTING, TEST_STARTING" +
                "It is possible to define multiple comma-separated value"},
        section = "JUnit properties")
@PropertyName("JUNIT5_REFRESH_BEFORE")
@PropertyDefaultValue("TEST_STARTING")
public final class Junit5RefreshStrategyProperty implements MultipleEnumPropertySuppler<RefreshEachTimeBefore> {

    public static final Junit5RefreshStrategyProperty REFRESH_STRATEGY_PROPERTY = new Junit5RefreshStrategyProperty();

    private Junit5RefreshStrategyProperty() {
        super();
    }

    private static boolean is(RefreshEachTimeBefore strategy) {
        var value = REFRESH_STRATEGY_PROPERTY.get();
        if (value != null) {
            return value.contains(strategy);
        }

        return false;
    }

    public static boolean isBeforeAll() {
        return is(ALL_STARTING);
    }

    public static boolean isBeforeEach() {
        return is(EACH_STARTING);
    }

    public static boolean isBeforeTest() {
        return is(TEST_STARTING);
    }
}
