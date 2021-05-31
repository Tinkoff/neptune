package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.ItemNotFoundMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.apache.commons.lang3.ArrayUtils.add;

abstract class AbstractSetOfObjectsMatcher<S, R> extends NeptuneFeatureMatcher<S> {

    static final String MATCHERS = "matchers";

    @DescriptionFragment(value = MATCHERS, makeReadableBy = AllParameterValueGetter.class)
    final Matcher<? super R>[] matchers;

    @SafeVarargs
    protected AbstractSetOfObjectsMatcher(boolean isNullSafe, Matcher<? super R>... matchers) {
        super(isNullSafe);
        checkNotNull(matchers);
        checkArgument(matchers.length > 0, "At least one matcher should be defined");
        this.matchers = matchers;
    }

    @SafeVarargs
    @SuppressWarnings("unchecked")
    static <T> Matcher<? super T>[] convertToMatcherArray(T... ts) {
        Matcher<T>[] matchers = new Matcher[]{};
        for (var t : ts) {
            matchers = add(matchers, Matchers.is(t));
        }
        return matchers;
    }

    int checkInAnyOrder(List<R> toCheck, boolean toLogMismatches, boolean breakOnFirst) {
        var matches = 0;

        for (var m : matchers) {
            var found = false;
            var foundElements = new ArrayList<R>();

            for (var r : toCheck) {
                if (m.matches(r)) {
                    found = true;
                    matches++;
                    foundElements.add(r);

                    if (breakOnFirst) {
                        break;
                    }
                }
            }

            if (found) {
                toCheck.removeAll(foundElements);
            } else if (toLogMismatches) {
                appendMismatchDescription(new ItemNotFoundMismatch(m));
            }
        }
        return matches;
    }
}
