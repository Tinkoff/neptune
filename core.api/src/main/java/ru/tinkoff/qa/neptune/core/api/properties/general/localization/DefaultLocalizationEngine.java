package ru.tinkoff.qa.neptune.core.api.properties.general.localization;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.core.api.properties.object.ObjectByClassPropertySupplier;
import ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle;
import ru.tinkoff.qa.neptune.core.api.localization.StepLocalization;

import java.util.Objects;

@PropertyDescription(description = "Define localisation mechanism",
        section = "Report localization")
@PropertyName("DEFAULT_LOCALIZATION_ENGINE")
public final class DefaultLocalizationEngine implements ObjectByClassPropertySupplier<StepLocalization> {
    public static final DefaultLocalizationEngine DEFAULT_LOCALIZATION_ENGINE = new DefaultLocalizationEngine();

    private DefaultLocalizationEngine() {
        super();
    }

    /**
     * Is localization by resource bundle currently used or not.
     *
     * @return {@link 'true'} when property value is {@link LocalizationByResourceBundle}. {@link 'false'}
     * is returned otherwise.
     */
    public static boolean isBundleLocalization() {
        return Objects.equals(
                PropertySupplier.returnOptionalFromEnvironment(DEFAULT_LOCALIZATION_ENGINE).orElse(null),
                LocalizationByResourceBundle.class.getName());
    }
}
