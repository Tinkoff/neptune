package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllMatchersParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.ObjectIsNotPresentMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.EmptyMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.Item;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static com.google.common.collect.Iterables.size;
import static java.lang.reflect.Array.getLength;
import static org.apache.commons.lang3.ArrayUtils.add;
import static org.hamcrest.Matchers.equalTo;

public abstract class AbstractSetOfObjectsMatcher<S, R> extends NeptuneFeatureMatcher<S> {

    static final String MATCHERS = "matchers";

    @DescriptionFragment(value = MATCHERS, makeReadableBy = AllMatchersParameterValueGetter.class)
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
            matchers = add(matchers, equalTo(t));
        }
        return matchers;
    }

    @Override
    protected final boolean prerequisiteChecking(Object actual) {
        var isValid = super.prerequisiteChecking(actual);

        if (!isValid) {
            return false;
        }

        var size = 0;
        if (actual instanceof Iterable) {
            size = size((Iterable<?>) actual);
        }

        if (actual.getClass().isArray()) {
            size = getLength(actual);
        }

        if (actual instanceof Map) {
            size = ((Map<?, ?>) actual).size();
        }

        if (size == 0) {
            appendMismatchDescription(new EmptyMismatch());
            return false;
        }

        return true;
    }

    int checkInAnyOrder(List<R> toCheck, boolean toLogMismatches, boolean breakOnFirst, Matcher<?>... matchers) {
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
                appendMismatchDescription(new ObjectIsNotPresentMismatch(new Item(), m));
            }
        }
        return matches;
    }
}
