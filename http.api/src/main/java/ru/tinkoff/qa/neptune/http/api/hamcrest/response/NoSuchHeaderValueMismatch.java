package ru.tinkoff.qa.neptune.http.api.hamcrest.response;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.MismatchDescriber;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.Set;

@Description("Http headers {headerNames} have no value {valueMatcher}")
final class NoSuchHeaderValueMismatch extends MismatchDescriber {

    @DescriptionFragment(value = "headerNames")
    final Set<String> headerNames;

    @DescriptionFragment(value = "valueMatcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    final Matcher<?> headerValueMatcher;


    public NoSuchHeaderValueMismatch(Set<String> headerNames, Matcher<?> headerValueMatcher) {
        this.headerNames = headerNames;
        this.headerValueMatcher = headerValueMatcher;
    }
}
