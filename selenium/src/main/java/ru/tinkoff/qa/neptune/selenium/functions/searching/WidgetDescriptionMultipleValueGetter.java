package ru.tinkoff.qa.neptune.selenium.functions.searching;

import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.NameMultiple;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;

import static java.util.Optional.ofNullable;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.isBundleLocalization;
import static ru.tinkoff.qa.neptune.core.api.steps.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.selenium.api.widget.WidgetDescriptionMetadataFactory.getMultipleNameMetadata;

public class WidgetDescriptionMultipleValueGetter implements ParameterValueGetter<Class<? extends Widget>> {

    @Override
    public String getParameterValue(Class<? extends Widget> o) {
        if (!isBundleLocalization()) {
            return ofNullable(getMultipleNameMetadata(o))
                    .map(metaData -> metaData.getAnnotation(NameMultiple.class).value())
                    .orElse(null);
        }

        return translate(getMultipleNameMetadata(o));
    }
}
