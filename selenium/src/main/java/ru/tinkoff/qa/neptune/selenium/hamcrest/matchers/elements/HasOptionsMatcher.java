package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;
import ru.tinkoff.qa.neptune.selenium.api.widget.HasOptions;
import ru.tinkoff.qa.neptune.selenium.api.widget.drafts.Select;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.Objects.nonNull;
import static org.hamcrest.Matchers.emptyIterable;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.not.NotMatcher.notOf;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsConsistsOfMatcher.iterableInOrder;

@Description("options: {matcher}")
public final class HasOptionsMatcher extends NeptuneFeatureMatcher<HasOptions> {

    @DescriptionFragment(value = "matcher", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Iterable<String>> matcher;

    private HasOptionsMatcher(Matcher<Iterable<String>> matcher) {
        super(true);
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
    public static Matcher<HasOptions> hasOptions(String... options) {
        checkNotNull(options);
        checkArgument(options.length > 0, "There should be defined at least one expected option");
        return new HasOptionsMatcher(iterableInOrder(options));
    }

    /**
     * Creates an instance of {@link HasOptionsMatcher} that checks options of the {@link Select}.
     *
     * @param matcher to check options of the {@link Select}
     * @return created object of {@link HasOptionsMatcher}
     */
    public static Matcher<HasOptions> hasOptions(Matcher<Iterable<String>> matcher) {
        return new HasOptionsMatcher(matcher);
    }

    /**
     * Creates an instance of {@link HasOptionsMatcher} that checks options of the {@link Select}. It is expected
     * that the select has at least one option.
     *
     * @return created object of {@link HasOptionsMatcher}
     */
    public static Matcher<HasOptions> hasOptions() {
        return new HasOptionsMatcher(notOf(emptyIterable()));
    }

    @Override
    protected boolean featureMatches(HasOptions toMatch) {
        var options = toMatch.getOptions();
        boolean result = matcher.matches(options);

        if (!result) {
            appendMismatchDescription(matcher, options);
        }

        return result;
    }
}
