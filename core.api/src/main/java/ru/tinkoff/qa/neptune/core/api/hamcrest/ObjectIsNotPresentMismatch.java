package ru.tinkoff.qa.neptune.core.api.hamcrest;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

@Description("Not present {objectName}: {characteristics}")
public final class ObjectIsNotPresentMismatch extends MismatchDescriber {

    @DescriptionFragment("objectName")
    final Object matchObjectName;

    @DescriptionFragment(value = "characteristics", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> matcher;

    private ObjectIsNotPresentMismatch(Object matchObjectName, Matcher<?> matcher) {
        this.matchObjectName = matchObjectName;
        this.matcher = matcher;
    }

    public ObjectIsNotPresentMismatch(String matchObjectName, Matcher<?> matcher) {
        this((Object) matchObjectName, matcher);
    }

    public ObjectIsNotPresentMismatch(MatchObjectName matchObjectName, Matcher<?> matcher) {
        this((Object) matchObjectName, matcher);
    }
}
