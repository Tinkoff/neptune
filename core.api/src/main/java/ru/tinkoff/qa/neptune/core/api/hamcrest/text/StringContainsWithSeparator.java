package ru.tinkoff.qa.neptune.core.api.hamcrest.text;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.hasItem;

/**
 * Checks whenever string has substring(s) that meet defined criteria or not.
 * These substrings should be separated from each other by defined separator
 */
@Description("string has substring(s) <{toContain}> separated by <{separator}>")
public final class StringContainsWithSeparator extends NeptuneFeatureMatcher<String> {

    @DescriptionFragment(value = "toContain", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super Iterable<String>> toContain;
    @DescriptionFragment("separator")
    private final String separator;

    private StringContainsWithSeparator(Matcher<? super Iterable<String>> toContain, String separator) {
        super(true);
        checkArgument(isNotBlank(separator), "Separator is blank");
        checkNotNull(toContain, "Matcher is not defined");
        this.toContain = toContain;
        this.separator = separator;
    }

    /**
     * Creates matcher that checks whenever string has substrings that meet defined criteria or not.
     * These substrings should be separated from each other by defined separator.
     *
     * @param matcher   that verifies separated substrings
     * @param separator is string separator
     * @return new {@link StringContainsWithSeparator}
     */
    public static StringContainsWithSeparator containsStringsSeparatedBy(Matcher<? super Iterable<String>> matcher, String separator) {
        return new StringContainsWithSeparator(matcher, separator);
    }

    /**
     * Creates matcher that checks whenever string has substring that meets defined criteria or not.
     * This substring should be separated from the whole string by defined separator.
     *
     * @param toContain that is supposed to be present
     * @param separator is string separator
     * @return new {@link StringContainsWithSeparator}
     */
    public static StringContainsWithSeparator containsStringSeparatedBy(String toContain, String separator) {
        return new StringContainsWithSeparator(hasItem(toContain), separator);
    }

    @Override
    protected boolean featureMatches(String toMatch) {
        var value = stream(toMatch.split(separator)).collect(toList());
        var result = toContain.matches(value);

        if (!result) {
            appendMismatchDescription(toContain, toMatch);
        }

        return result;
    }
}
