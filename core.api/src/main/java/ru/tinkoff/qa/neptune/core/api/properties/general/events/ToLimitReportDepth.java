package ru.tinkoff.qa.neptune.core.api.properties.general.events;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDefaultValue;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.booleans.BooleanValuePropertySupplier;

@PropertyDescription(description = {
        "To limit report/log output or not by depth of step performing",
        "It is about steps which are built by `ru.tinkoff.qa.neptune.core.api.steps.SequentialActionSupplier`",
        "and `ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier`"},
        section = "General properties. Report")
@PropertyName("TO_LIMIT_REPORT_DEPTH")
@PropertyDefaultValue("true")
public final class ToLimitReportDepth implements BooleanValuePropertySupplier {

    public static final ToLimitReportDepth TO_LIMIT_REPORT_DEPTH_PROPERTY = new ToLimitReportDepth();

    private ToLimitReportDepth() {
        super();
    }
}
