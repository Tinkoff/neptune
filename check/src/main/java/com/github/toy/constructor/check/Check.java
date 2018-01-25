package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.PerformStep;
import org.hamcrest.Matcher;

import java.util.*;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.String.format;
import static java.util.Arrays.asList;
import static org.hamcrest.MatcherAssert.assertThat;

public class Check implements PerformStep<Check> {

    private final Set<AssertionError> checkResult = new LinkedHashSet<>();

    /**
     * This method performs the checking of some value by criteria.
     * @param description of a value to check
     * @param toCheck a value to check
     * @param criteria to check the value by
     * @param <T> the type of the value to check
     * @return self-reference
     */
    public <T> Check check(String description, T toCheck, Matcher... criteria) {
        checkNotNull(criteria);
        checkArgument(criteria.length > 0, "Should be defined at least one matcher");
        checkResult.clear();

        try {
            return perform(action(format("Check: %s", description), check -> {
                asList(criteria).forEach(matcher -> {
                    try {
                        perform(action(matcher.toString(), check1 -> assertThat(toCheck, matcher)));
                    } catch (AssertionError e) {
                        check.checkResult.add(e);
                    }
                });
                if (check.checkResult.size() > 0) {
                    StringBuilder builder = new StringBuilder("Failed assertions");
                    check.checkResult.forEach(assertion ->
                            builder.append("\n==================================\n")
                            .append(format("%s", assertion.getMessage())));

                    throw new AssertionError(builder.toString());
                }
            }));
        }
        finally {
            checkResult.clear();
        }
    }
}
