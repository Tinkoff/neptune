package ru.tinkoff.qa.neptune.testng.integration.properties;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.enums.MultipleEnumPropertySuppler;

@PropertyDescription(description = {"Defines when (which methods) it is necessary to refresh contexts/resources,",
        "e.g. back to the base browser page, renew db/http connections etc.",
        "Available values: SUITE_STARTING, ALL_TEST_STARTING, CLASS_STARTING, GROUP_STARTING, BEFORE_METHOD_STARTING, METHOD_STARTING" +
                "It is possible to define multiple comma-separated value"},
        section = "TestNG properties")
@PropertyName("TESTNG_REFRESH_BEFORE")
@PropertyDefaultValue("METHOD_STARTING")
public final class TestNGRefreshStrategyProperty implements MultipleEnumPropertySuppler<RefreshEachTimeBefore> {

    public static final TestNGRefreshStrategyProperty REFRESH_STRATEGY_PROPERTY = new TestNGRefreshStrategyProperty();

    private TestNGRefreshStrategyProperty() {
        super();
    }
}
