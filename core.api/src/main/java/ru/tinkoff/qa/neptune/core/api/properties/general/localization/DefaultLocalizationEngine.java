package ru.tinkoff.qa.neptune.core.api.properties.general.localization;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;
import ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization;

@PropertyDescription(description = "Define localisation mechanism",
        section = "Report localization")
@PropertyName("DEFAULT_LOCALIZATION_ENGINE")
public class DefaultLocalizationEngine implements ObjectByClassPropertySupplier<StepLocalization> {
    public static final DefaultLocalizationEngine DEFAULT_LOCALIZATION_ENGINE = new DefaultLocalizationEngine();

    private DefaultLocalizationEngine() {
        super();
    }

}
