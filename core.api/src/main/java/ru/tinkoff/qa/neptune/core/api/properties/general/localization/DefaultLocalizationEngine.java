package ru.tinkoff.qa.neptune.core.api.properties.general.localization;

import ru.tinkoff.qa.neptune.core.api.properties.PropertyDescription;
import ru.tinkoff.qa.neptune.core.api.properties.PropertyName;
import ru.tinkoff.qa.neptune.core.api.properties.PropertySupplier;
import ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization;

import static java.lang.Class.forName;

@PropertyDescription(description = "Define localisation mechanism",
        section = "Report localization")
@PropertyName("DEFAULT_LOCALIZATION_ENGINE")
public class DefaultLocalizationEngine implements PropertySupplier<StepLocalization> {
    public static final DefaultLocalizationEngine DEFAULT_LOCALIZATION_ENGINE = new DefaultLocalizationEngine();

    private DefaultLocalizationEngine() {
        super();
    }

    @Override
    public StepLocalization parse(String value) {
        try {
            var clazz = ((Class<?>) forName(value));

            if (!StepLocalization.class.isAssignableFrom(clazz)) {
                throw new IllegalArgumentException("Is not an inheritor or an implementation " +
                        StepLocalization.class.getName());
            }

            var c = clazz.getConstructor();
            c.setAccessible(true);
            return (StepLocalization) c.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
