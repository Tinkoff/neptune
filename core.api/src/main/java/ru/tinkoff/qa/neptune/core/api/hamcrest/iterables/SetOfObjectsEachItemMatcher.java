package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.lang.String.valueOf;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.AbstractSetOfObjectsMatcher.MATCHERS;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.*;

/**
 * This matcher checks each item of any set of objects (iterable, collection, array, map entries).
 *
 * @param <S> is a type of a checked value
 * @param <R> is a type of an item of iterable object
 * @param <T> is a type of iterable
 */
@Description("each item: {" + MATCHERS + "}")
public abstract class SetOfObjectsEachItemMatcher<S, R, T extends Iterable<R>> extends AbstractSetOfObjectsMatcher<S, R> {

    @SafeVarargs
    private SetOfObjectsEachItemMatcher(Matcher<? super R>... matchers) {
        super(true, matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that each item of checked iterable
     * meet every defined criteria.
     *
     * @param matchers criteria for each item
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a mather
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> eachOfIterable(Matcher<? super T>... matchers) {
        return (Matcher<R>) new IterableEachItemMatcher<>(matchers);
    }

    /**
     * Creates a matcher that checks an array. It is expected that each item of checked array meet
     * every defined criteria.
     *
     * @param matchers criteria for each item
     * @param <T>      is a type of an item of array
     * @return a mather
     */
    @SafeVarargs
    public static <T> Matcher<T[]> eachOfArray(Matcher<? super T>... matchers) {
        return new ArrayEachItemMatcher<>(matchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that each entry of checked map meet
     * every defined criteria.
     *
     * @param keyMatcher   criteria for each key
     * @param valueMatcher criteria for each value
     * @param <K>          is a type of map keys
     * @param <V>          is a type of map values
     * @param <T>          is a type of map
     * @return return a mather
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> eachEntry(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        return (Matcher<T>) new MapEachItemMatcher<>(mapEntry(keyMatcher, valueMatcher));
    }

    /**
     * Creates a matcher that checks a map. It is expected that each entry of checked map has keys that meet
     * every defined criteria.
     *
     * @param keyMatchers criteria for each key
     * @param <K>         is a type of map keys
     * @param <V>         is a type of map values
     * @param <T>         is a type of map
     * @return return a mather
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> eachEntryKey(Matcher<? super K>... keyMatchers) {
        return (Matcher<T>) new MapEachItemMatcher<>(entryKey(keyMatchers));
    }

    /**
     * Creates a matcher that checks a map. It is expected that each entry of checked map has values that meet
     * every defined criteria.
     *
     * @param valueMatchers criteria for each value
     * @param <K>           is a type of map keys
     * @param <V>           is a type of map values
     * @param <T>           is a type of map
     * @return return a mather
     */
    @SuppressWarnings("unchecked")
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> eachEntryValue(Matcher<? super V>... valueMatchers) {
        return (Matcher<T>) new MapEachItemMatcher<>(entryValue(valueMatchers));
    }

    boolean eachMatches(T toCheck) {
        int index = 0;
        boolean matches = true;
        for (var r : toCheck) {
            var m = all(matchers);
            if (!m.matches(r)) {
                var d = new StringDescription();
                d.appendText(valueOf(index)).appendText(": ").appendText(valueOf(r)).appendText(". ");
                m.describeMismatch(r, d);
                appendMismatchDescription(d);
                matches = false;
            }
            index++;
        }

        return matches;
    }

    private static class IterableEachItemMatcher<R> extends SetOfObjectsEachItemMatcher<Iterable<R>, R, Iterable<R>> {

        @SafeVarargs
        private IterableEachItemMatcher(Matcher<? super R>... matchers) {
            super(matchers);
        }

        @Override
        protected boolean featureMatches(Iterable<R> toMatch) {
            return eachMatches(toMatch);
        }
    }

    private static class ArrayEachItemMatcher<R> extends SetOfObjectsEachItemMatcher<R[], R, List<R>> {

        @SafeVarargs
        private ArrayEachItemMatcher(Matcher<? super R>... matchers) {
            super(matchers);
        }

        @Override
        protected boolean featureMatches(R[] toMatch) {
            return eachMatches(asList(toMatch));
        }
    }

    @Description("each entry: {" + MATCHERS + "}")
    private static class MapEachItemMatcher<K, V> extends SetOfObjectsEachItemMatcher<Map<K, V>, Map.Entry<K, V>, Set<Map.Entry<K, V>>> {

        private MapEachItemMatcher(Matcher<Map.Entry<K, V>> entryMatcher) {
            super(entryMatcher);
        }

        @Override
        protected boolean featureMatches(Map<K, V> toMatch) {
            return eachMatches(toMatch.entrySet());
        }
    }
}
