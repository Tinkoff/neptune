package ru.tinkoff.qa.neptune.core.api.hamcrest.text;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.arrayInOrder;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.arrayHasItem;

/**
 * Checks whenever string has substring(s) that meet defined criteria or not.
 * These substrings should be separated from each other by defined separator
 */
@Description("string has substring(s) separated by <'{separator}'>: {toContain}")
public final class StringContainsWithSeparator extends NeptuneFeatureMatcher<String> {

    @DescriptionFragment(value = "toContain")
    private final Matcher<String[]> toContain;
    @DescriptionFragment("separator")
    private final String separator;

    private StringContainsWithSeparator(Matcher<String[]> toContain, String separator) {
        super(true);
        checkArgument(nonNull(separator) && !EMPTY.equals(separator),
                "Separator should not be null or empty string");
        checkNotNull(toContain, "Matcher is not defined");
        this.toContain = toContain;
        this.separator = separator;
    }

    /**
     * Creates matcher that checks whenever string has substrings that meet defined criteria or not.
     * These substrings should be separated from each other by defined separator.
     *
     * @param separator is string separator
     * @param matcher   that verifies separated substrings
     * @return new {@link StringContainsWithSeparator}
     */
    public static Matcher<String> withSeparator(String separator, Matcher<String[]> matcher) {
        return new StringContainsWithSeparator(matcher, separator);
    }

    /**
     * Creates matcher that checks whenever string has only defined substrings in the listed order.
     *
     * @param subStrings expected substrings
     * @param separator  is string separator
     * @return new {@link StringContainsWithSeparator}
     */
    public static Matcher<String> withSeparator(String separator, String... subStrings) {
        return withSeparator(separator, arrayInOrder(subStrings));
    }

    /**
     * Creates matcher that checks whenever string has substring that meets defined criteria or not.
     * This substring should be separated from the whole string by defined separator.
     *
     * @param toContain that is supposed to be present
     * @param separator is string separator
     * @return new {@link StringContainsWithSeparator}
     */
    public static Matcher<String> withSeparator(String separator, String toContain) {
        return withSeparator(separator, arrayHasItem(toContain));
    }

    @Override
    protected boolean featureMatches(String toMatch) {
        var value = toMatch.split(separator);
        var result = toContain.matches(value);

        if (!result) {
            appendMismatchDescription(toContain, value);
        }

        return result;
    }
}
