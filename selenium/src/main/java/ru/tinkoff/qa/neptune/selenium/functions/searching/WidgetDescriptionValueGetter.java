package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.Name;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.isBundleLocalization;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionMetadataFactory.getNameMetadata;

public class WidgetDescriptionValueGetter implements ParameterValueGetter<Class<? extends Widget>> {

    @Override
    public String getParameterValue(Class<? extends Widget> o) {
        if (!isBundleLocalization()) {
            return ofNullable(getNameMetadata(o))
                    .map(metaData -> metaData.getAnnotation(Name.class).value())
                    .orElse(null);
        }

        return translate(getNameMetadata(o));
    }
}
