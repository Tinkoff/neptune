package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.DifferentSizeMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.ItemMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Iterables.size;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.AbstractSetOfObjectsMatcher.MATCHERS;

/**
 * This matcher checks that any set of objects (iterable, collection, array, map entries) consists only of objects
 * that meet defined criteria.
 *
 * @param <S> is a type of a checked value
 * @param <R> is a type of an item of iterable object
 * @param <T> is a type of iterable
 */
@Description("in any order: {" + MATCHERS + "}")
public abstract class SetOfObjectsConsistsOfMatcher<S, R, T extends Iterable<R>> extends AbstractSetOfObjectsMatcher<S, R> {

    private final boolean strictlyInOrder;

    @SafeVarargs
    private SetOfObjectsConsistsOfMatcher(boolean strictlyInOrder, Matcher<? super R>... matchers) {
        super(true, matchers);
        this.strictlyInOrder = strictlyInOrder;
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to have
     * only defined elements in the listed order. Each one matcher of {@code matchers} describes a single distinct
     * item of the iterable.
     *
     * @param matchers describe every one distinct item of the iterable.
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableInOrder(Matcher<? super T>... matchers) {
        return (Matcher<R>) new IterableConsistsOfMatcherInOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to have
     * only defined elements in the listed order. Each one item of {@code ts} is a single distinct
     * item of the iterable.
     *
     * @param ts  describe every one distinct item of the iterable.
     * @param <T> is a type of an item of iterable
     * @param <R> is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R extends Iterable<T>> Matcher<R> iterableInOrder(T... ts) {
        return iterableInOrder(convertToMatcherArray(ts));
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to have
     * only defined elements. An order of items has no matter. Each one matcher of {@code matchers} describes
     * a single distinct item of the iterable.
     *
     * @param matchers describe every one distinct item of the iterable.
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableOf(Matcher<? super T>... matchers) {
        return (Matcher<R>) new IterableConsistsOfMatcherInAnyOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to have
     * only defined elements. An order of items has no matter. Each one item of {@code ts} is a single distinct
     * item of the iterable.
     *
     * @param ts  describe every one distinct item of the iterable.
     * @param <T> is a type of an item of iterable
     * @param <R> is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R extends Iterable<T>> Matcher<R> iterableOf(T... ts) {
        return iterableOf(convertToMatcherArray(ts));
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to have only defined elements in the
     * listed order. Each one matcher of {@code matchers} describes a single distinct item of the array.
     *
     * @param matchers describe every one distinct item of the array.
     * @param <T>      is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayInOrder(Matcher<? super T>... matchers) {
        return new ArrayConsistsOfMatcherInOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to have only defined elements in the
     * listed order. Each one item of {@code ts} is a single distinct item of the array.
     *
     * @param ts  describe every one distinct item of the array.
     * @param <T> is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayInOrder(T... ts) {
        return arrayInOrder(convertToMatcherArray(ts));
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to have
     * only defined elements. An order of items has no matter. Each one matcher of {@code matchers} describes
     * a single distinct item of the array.
     *
     * @param matchers describe every one distinct item of the array.
     * @param <T>      is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayOf(Matcher<? super T>... matchers) {
        return new ArrayConsistsOfMatcherInAnyOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to have only defined elements.
     * An order of items has no matter. Each one item of {@code ts} is a single distinct item of the array.
     *
     * @param ts  describe every one distinct item of the array.
     * @param <T> is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayOf(T... ts) {
        return arrayOf(convertToMatcherArray(ts));
    }


    /**
     * Creates a matcher that checks a map. The checked map is expected to have only defined key-value pairs
     * in the listed order. Each one matcher of {@code matchers} describes a single distinct key-value pair.
     *
     * @param matchers describe every one distinct key-value pair the map
     * @param <K>      is a type of map keys
     * @param <V>      is a type of map values
     * @param <T>      is a type of map
     * @return return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapInOrder(Matcher<Map.Entry<K, V>>... matchers) {
        return (Matcher<T>) new MapConsistsOfMatcherInOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks a map. The checked map is expected to have only defined key-value pairs.
     * An order of items has no matter. Each one matcher of {@code matchers} describes a single distinct key-value pair.
     *
     * @param matchers describe every one distinct key-value pair the map
     * @param <K>      is a type of map keys
     * @param <V>      is a type of map values
     * @param <T>      is a type of map
     * @return return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapOf(Matcher<Map.Entry<K, V>>... matchers) {
        return (Matcher<T>) new MapConsistsOfMatcherInAnyOrder<>(matchers);
    }


    boolean checkIterable(T toCheck) {
        var actualSize = size(toCheck);
        if (actualSize != matchers.length) {
            appendMismatchDescription(new DifferentSizeMismatch(matchers.length, actualSize));
            return false;
        }

        var toBeChecked = Lists.newArrayList(toCheck);

        return strictlyInOrder ? checkInStrictOrder(toBeChecked) :
                checkInAnyOrder(toBeChecked, true, true, matchers) == matchers.length;
    }

    private boolean checkInStrictOrder(List<R> toCheck) {
        int i = 0;
        var matches = true;
        for (var m : matchers) {
            var o = toCheck.get(i);
            if (!m.matches(o)) {
                var d = new StringDescription();
                m.describeMismatch(o, d);
                appendMismatchDescription(new ItemMismatch(i, o, d));
                matches = false;
            }
            i++;
        }

        return matches;
    }

    private abstract static class IterableConsistsOfMatcher<R> extends SetOfObjectsConsistsOfMatcher<Iterable<R>, R, Iterable<R>> {

        @SafeVarargs
        private IterableConsistsOfMatcher(boolean strictlyInOrder, Matcher<? super R>... matchers) {
            super(strictlyInOrder, matchers);
        }

        @Override
        protected boolean featureMatches(Iterable<R> toMatch) {
            return checkIterable(toMatch);
        }
    }

    @Description("in following order: {" + MATCHERS + "}")
    private static class IterableConsistsOfMatcherInOrder<R> extends IterableConsistsOfMatcher<R> {

        @SafeVarargs
        private IterableConsistsOfMatcherInOrder(Matcher<? super R>... matchers) {
            super(true, matchers);
        }
    }

    private static class IterableConsistsOfMatcherInAnyOrder<R> extends IterableConsistsOfMatcher<R> {

        @SafeVarargs
        private IterableConsistsOfMatcherInAnyOrder(Matcher<? super R>... matchers) {
            super(false, matchers);
        }
    }

    private abstract static class ArrayConsistsOfMatcher<R> extends SetOfObjectsConsistsOfMatcher<R[], R, List<R>> {

        @SafeVarargs
        private ArrayConsistsOfMatcher(boolean strictlyInOrder, Matcher<? super R>... matchers) {
            super(strictlyInOrder, matchers);
        }

        @Override
        protected boolean featureMatches(R[] toMatch) {
            return checkIterable(asList(toMatch));
        }
    }

    @Description("in following order: {" + MATCHERS + "}")
    private static class ArrayConsistsOfMatcherInOrder<R> extends ArrayConsistsOfMatcher<R> {

        @SafeVarargs
        private ArrayConsistsOfMatcherInOrder(Matcher<? super R>... matchers) {
            super(true, matchers);
        }
    }

    private static class ArrayConsistsOfMatcherInAnyOrder<R> extends ArrayConsistsOfMatcher<R> {

        @SafeVarargs
        private ArrayConsistsOfMatcherInAnyOrder(Matcher<? super R>... matchers) {
            super(false, matchers);
        }
    }


    private abstract static class MapConsistsOfMatcher<K, V> extends SetOfObjectsConsistsOfMatcher<Map<K, V>, Map.Entry<K, V>, Set<Map.Entry<K, V>>> {

        @SafeVarargs
        private MapConsistsOfMatcher(boolean strictlyInOrder, Matcher<Map.Entry<K, V>>... matchers) {
            super(strictlyInOrder, matchers);
        }

        @Override
        protected boolean featureMatches(Map<K, V> toMatch) {
            return checkIterable(toMatch.entrySet());
        }
    }

    @Description("in following order: {" + MATCHERS + "}")
    private static class MapConsistsOfMatcherInOrder<K, V> extends MapConsistsOfMatcher<K, V> {

        @SafeVarargs
        private MapConsistsOfMatcherInOrder(Matcher<Map.Entry<K, V>>... matchers) {
            super(true, matchers);
        }
    }

    private static class MapConsistsOfMatcherInAnyOrder<K, V> extends MapConsistsOfMatcher<K, V> {

        @SafeVarargs
        private MapConsistsOfMatcherInAnyOrder(Matcher<Map.Entry<K, V>>... matchers) {
            super(false, matchers);
        }
    }
}
