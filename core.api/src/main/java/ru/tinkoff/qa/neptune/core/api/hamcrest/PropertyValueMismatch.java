package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

/**
 * Common mismatch description.
 */
@Description("{property}: {value}. {mismatch}")
public final class PropertyValueMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "property")
    final Object propertyName;

    @DescriptionFragment("value")
    final Object checked;

    @DescriptionFragment(value = "mismatch", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final org.hamcrest.Description description;

    private PropertyValueMismatch(Object propertyName, Object checked, Matcher<?> matcher) {
        this.propertyName = propertyName;
        var d = new StringDescription();
        matcher.describeMismatch(checked, d);
        description = d;
        this.checked = checked;
    }

    public PropertyValueMismatch(String propertyName, Object checked, Matcher<?> matcher) {
        this((Object) propertyName, checked, matcher);
    }

    public PropertyValueMismatch(MatchObjectName matchPropertyName, Object checked, Matcher<?> matcher) {
        this((Object) matchPropertyName, checked, matcher);
    }
}
