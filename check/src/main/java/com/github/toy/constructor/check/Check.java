package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.PerformStep;
import org.hamcrest.Matcher;

import java.util.*;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.hamcrest.MatcherAssert.assertThat;

public class Check implements PerformStep<Check> {

    private final Set<AssertionError> checkResult = new LinkedHashSet<>();

    /**
     * This method performs the checking of some value my matchers.
     * @param description of a value to check
     * @param toCheck a value to check
     * @param matchers to check the value by
     * @param <T> the type of the value to check
     * @return self-reference
     */
    public <T> Check check(String description, T toCheck, Matcher<T>... matchers) {
        checkNotNull(matchers);
        checkArgument(matchers.length > 0, "Should be defined at least one matcher");
        checkResult.clear();

        try {
            return perform(action(format("Check: %s", description), check -> {
                asList(matchers).forEach(matcher ->
                        perform(action(matcher.toString(), check1 -> {
                            try {
                                assertThat(toCheck, matcher);
                            } catch (AssertionError e) {
                                check1.checkResult.add(e);
                            }
                        })));
                if (check.checkResult.size() > 0) {
                    StringBuilder builder = new StringBuilder("Failed assertions");
                    check.checkResult.forEach(assertion -> builder.append(EMPTY)
                            .append(assertion.getMessage()));

                    throw new AssertionError(builder.toString());
                }
            }));
        }
        finally {
            checkResult.clear();
        }
    }
}
