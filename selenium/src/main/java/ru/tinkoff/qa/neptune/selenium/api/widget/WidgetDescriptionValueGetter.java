package ru.tinkoff.qa.neptune.selenium.api.widget;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.isBundleLocalization;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionFormer.getDescription;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionFormer.getNameMetadata;

public class WidgetDescriptionValueGetter implements ParameterValueGetter<Object> {

    @Override
    public String getParameterValue(Object o) {
        if (o instanceof Class) {
            if (!isBundleLocalization()) {
                return ofNullable(getNameMetadata((Class<?>) o))
                        .map(metaData -> metaData.getAnnotation(Name.class).value())
                        .orElse(null);
            }

            return translate(getNameMetadata((Class<?>) o));
        }

        return getDescription(o);
    }
}
