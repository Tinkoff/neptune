package ru.tinkoff.qa.neptune.selenium.api.widget;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.List;

import static java.util.List.of;
import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.isBundleLocalization;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionFormer.getMultipleDescription;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionFormer.getMultipleNameMetadata;

public class WidgetDescriptionMultipleValueGetter implements ParameterValueGetter<Object> {

    @Override
    public String getParameterValue(Object o) {
        if (o instanceof Class) {
            if (!isBundleLocalization()) {
                return ofNullable(getMultipleNameMetadata((Class<?>) o))
                        .map(metaData -> metaData.getAnnotation(NameMultiple.class).value())
                        .orElse(null);
            }

            return translate(getMultipleNameMetadata((Class<?>) o));
        }

        if (o instanceof List) {
            return getMultipleDescription((List<?>) o);
        }

        return getMultipleDescription(of(o));
    }
}
