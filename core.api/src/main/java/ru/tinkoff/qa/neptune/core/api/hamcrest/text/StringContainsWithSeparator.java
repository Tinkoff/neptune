package ru.tinkoff.qa.neptune.core.api.hamcrest.text;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeDiagnosingMatcher;

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
public final class StringContainsWithSeparator extends TypeSafeDiagnosingMatcher<String> {

    private final Matcher<? super Iterable<? super String>> toContain;
    private final String separator;

    private StringContainsWithSeparator(Matcher<? super Iterable<? super String>> toContain, String separator) {
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
    public StringContainsWithSeparator containsStringsSeparatedBy(Matcher<? super Iterable<? super String>> matcher, String separator) {
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
    public StringContainsWithSeparator containsStringSeparatedBy(String toContain, String separator) {
        return new StringContainsWithSeparator(hasItem(toContain), separator);
    }

    @Override
    protected boolean matchesSafely(String item, Description mismatchDescription) {
        if (item == null) {
            mismatchDescription.appendText(" was null");
            return false;
        }

        var value = stream(item.split(separator)).collect(toList());
        var result = toContain.matches(value);

        if (!result) {
            toContain.describeMismatch(value, mismatchDescription);
        }

        return result;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("string has substring(s) <")
                .appendDescriptionOf(toContain)
                .appendText("> separated by <")
                .appendText(separator)
                .appendText(">");
    }
}
