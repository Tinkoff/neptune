package com.github.toy.constructor.check;

import com.github.toy.constructor.core.api.PerformStep;
import com.github.toy.constructor.core.api.CreateWith;
import com.github.toy.constructor.core.api.ProviderOfEmptyParameters;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.MatcherAssert;

import static com.github.toy.constructor.core.api.StaticLogger.log;
import static com.github.toy.constructor.core.api.StoryWriter.action;
import static com.google.common.base.Preconditions.checkArgument;
import static java.lang.String.format;

@CreateWith(provider = ProviderOfEmptyParameters.class)
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
                check -> MatcherAssert.assertThat(toCheck, new Matcher<>() {
                    @Override
                    public boolean matches(Object item) {
                        boolean result = criteria.matches(item);
                        if (!result) {
                            log(item, "Mismatched object");
                        }
                        return result;
                    }

                    @Override
                    public void describeMismatch(Object item, Description mismatchDescription) {
                        criteria.describeMismatch(item, mismatchDescription);
                    }

                    @Override
                    public void _dont_implement_Matcher___instead_extend_BaseMatcher_() {
                        criteria._dont_implement_Matcher___instead_extend_BaseMatcher_();
                    }

                    @Override
                    public void describeTo(Description description) {
                        criteria.describeTo(description);
                    }
                })));
    }
}
