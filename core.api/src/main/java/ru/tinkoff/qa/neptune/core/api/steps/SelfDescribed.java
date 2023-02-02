package ru.tinkoff.qa.neptune.core.api.steps;

import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import static org.apache.tika.utils.StringUtils.isBlank;
import static ru.tinkoff.qa.neptune.core.api.localization.StepLocalization.translate;

/**
 * This class is for custom auxiliary classes to make their instances
 * able to self-describe via {@link SelfDescribed#toString()} method.
 * <p></p>
 * It is also need presence of {@link Description} on some class or its superclasses
 * to make instances of the class be describable
 */
public abstract class SelfDescribed {

    @Override
    public String toString() {
        var translated = translate(this);
        if (isBlank(translated)) {
            return super.toString();
        }
        return translated;
    }
}
