package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.PerformStep;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

public class Check implements PerformStep<Check> {

    /**
     * This method performs the checking of some value by criteria.
     * @param description of a value to check
     * @param toCheck a value to check
     * @param criteria to check the value by
     * @param <T> the type of the value to check
     * @return self-reference
     */
    public <T> Check assertThat(String description, T toCheck, Matcher<? super T> criteria) {
        checkArgument(criteria != null, "Criteria matcher should be defined");

        return perform(action(format("%s by criteria %s", description, criteria),
                check -> MatcherAssert.assertThat(toCheck, criteria)));
    }
}
