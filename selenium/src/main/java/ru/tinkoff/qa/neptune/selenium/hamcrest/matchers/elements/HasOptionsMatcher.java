package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.TypeSafeDiagnosingMatcher;

import java.util.Collection;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.*;

public final class HasOptionsMatcher<T extends Iterable<?>> extends TypeSafeDiagnosingMatcher<Select> {

    private final Matcher<T> matcher;

    private HasOptionsMatcher(Matcher<T> matcher) {
        checkArgument(nonNull(matcher), "Criteria to match options of the select should be defined");
        this.matcher = matcher;
    }

    /**
     * Creates an instance of {@link HasOptionsMatcher} that checks options of the {@link Select}. It is expected
     * that the select has same options and these options has same order as defined by {@code options}
     *
     * @param options are expected
     * @return created object of {@link HasOptionsMatcher}
     */
    public static HasOptionsMatcher<Iterable<? extends String>> hasOptions(String... options) {
        checkNotNull(options);
        checkArgument(options.length > 0, "There should be defined at least one expected option");
        return new HasOptionsMatcher<>(contains(options));
    }

    /**
     * Creates an instance of {@link HasOptionsMatcher} that checks options of the {@link Select}. It is expected
     * that the select has same options and these options has same order as defined by {@code options}
     *
     * @param options are expected
     * @return created object of {@link HasOptionsMatcher}
     */
    public static HasOptionsMatcher<Iterable<? extends String>> hasOptions(Collection<String> options) {
        checkNotNull(options);
        return hasOptions(options.toArray(new String[] {}));
    }

    /**
     * Creates an instance of {@link HasOptionsMatcher} that checks options of the {@link Select}.
     *
     * @param matcher to check options of the {@link Select}
     * @return created object of {@link HasOptionsMatcher}
     */
    public static HasOptionsMatcher<? extends Iterable<?>> hasOptions(Matcher<? extends Iterable<?>> matcher) {
        return new HasOptionsMatcher<>(matcher);
    }

    /**
     * Creates an instance of {@link HasOptionsMatcher} that checks options of the {@link Select}. It is expected
     * that the select has at least one option.
     *
     * @return created object of {@link HasOptionsMatcher}
     */
    public static HasOptionsMatcher<Iterable<?>> hasOptions() {
        return new HasOptionsMatcher<>(not(emptyIterable()));
    }

    @Override
    protected boolean matchesSafely(Select item, Description mismatchDescription) {
        var options = item.getOptions();
        boolean result = matcher.matches(options);

        if (!result) {
            mismatchDescription.appendText(format("Options of %s. Mismatch:", item));
            matcher.describeMismatch(options, mismatchDescription);
        }
        return result;
    }

    @Override
    public String toString() {
        return format("has options %s", matcher.toString());
    }
}
