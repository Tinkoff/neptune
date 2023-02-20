package ru.tinkoff.qa.neptune.check.agent;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle;

import java.lang.reflect.Method;

import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.localization.LocalizationByResourceBundle.getFromResourceBundles;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocaleProperty.DEFAULT_LOCALE_PROPERTY;
import static ru.tinkoff.qa.neptune.core.api.properties.general.localization.DefaultLocalizationEngine.DEFAULT_LOCALIZATION_ENGINE;

public final class HamcrestLocalizationHelper {

    private HamcrestLocalizationHelper() {
        super();
    }

    public static boolean isLocalizationUsed() {
        return nonNull(DEFAULT_LOCALIZATION_ENGINE.get()) && nonNull(DEFAULT_LOCALE_PROPERTY.get());
    }


    public static String calculateDescription(Matcher<?> matcher, Method method, Object[] args) {
        if (!LocalizationByResourceBundle.class.equals(DEFAULT_LOCALIZATION_ENGINE.get().getClass())) {
            return translate(matcher.toString());
        } else {
            return translate(getFromResourceBundles(DEFAULT_LOCALE_PROPERTY.get(), method),
                method,
                args);
        }
    }
}
