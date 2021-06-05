package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.DifferentSizeMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.ItemNotFoundMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.OutOfItemsOrderMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static com.google.common.collect.Iterables.size;
import static java.util.Arrays.asList;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.AbstractSetOfObjectsMatcher.MATCHERS;

/**
 * This matcher checks that any set of objects (iterable, collection, array, map entries) includes objects
 * that meet defined criteria.
 *
 * @param <S> is a type of a checked value
 * @param <R> is a type of an item of iterable object
 * @param <T> is a type of iterable
 */
@Description("includes in any order: {" + MATCHERS + "}")
public abstract class SetOfObjectsIncludesMatcher<S, R, T extends Iterable<R>> extends AbstractSetOfObjectsMatcher<S, R> {

    private final boolean relatively;

    @SafeVarargs
    private SetOfObjectsIncludesMatcher(boolean relatively, Matcher<? super R>... matchers) {
        super(true, matchers);
        this.relatively = relatively;
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to contain
     * defined elements in the listed order relatively to each other. Each one matcher of {@code matchers}
     * describes a single distinct item of the iterable.
     *
     * @param matchers describe every one distinct item of the iterable.
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableIncludesInOrder(Matcher<? super T>... matchers) {
        return (Matcher<R>) new IterableIncludesMatcherInOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to contain
     * defined elements in the listed order relatively to each other. Each one item of {@code ts} is a single distinct
     * item of the iterable.
     *
     * @param ts  describe every one distinct item of the iterable.
     * @param <T> is a type of an item of iterable
     * @param <R> is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R extends Iterable<T>> Matcher<R> iterableIncludesInOrder(T... ts) {
        return iterableIncludesInOrder(convertToMatcherArray(ts));
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to contain
     * defined elements. An order of items has no matter. Each one matcher of {@code matchers} describes a single
     * distinct item of the iterable.
     *
     * @param matchers describe every one distinct item of the iterable.
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableIncludes(Matcher<? super T>... matchers) {
        return (Matcher<R>) new IterableIncludesMatcherInAnyOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. The checked iterable is expected to contain defined
     * elements. An order of items has no matter. Each one item of {@code ts} is a single distinct item of the iterable.
     *
     * @param ts  describe every one distinct item of the iterable.
     * @param <T> is a type of an item of iterable
     * @param <R> is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R extends Iterable<T>> Matcher<R> iterableIncludes(T... ts) {
        return iterableIncludes(convertToMatcherArray(ts));
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to contain defined elements
     * in the listed order relatively to each other. Each one matcher of {@code matchers} describes a
     * single distinct item of the array.
     *
     * @param matchers describe every one distinct item of the array.
     * @param <T>      is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayIncludesInOrder(Matcher<? super T>... matchers) {
        return new ArrayIncludesMatcherInOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to contain defined elements in the
     * listed order relatively to each other. Each one item of {@code ts} is a single distinct item of the array.
     *
     * @param ts  describe every one distinct item of the array.
     * @param <T> is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayIncludesInOrder(T... ts) {
        return arrayIncludesInOrder(convertToMatcherArray(ts));
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to contain
     * defined elements. An order of items has no matter. Each one matcher of {@code matchers} describes
     * a single distinct item of the array.
     *
     * @param matchers describe every one distinct item of the array.
     * @param <T>      is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayIncludes(Matcher<? super T>... matchers) {
        return new ArrayIncludesMatcherInAnyOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks an array. The checked array is expected to contain defined elements.
     * An order of items has no matter. Each one item of {@code ts} is a single distinct item of the array.
     *
     * @param ts  describe every one distinct item of the array.
     * @param <T> is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayIncludes(T... ts) {
        return arrayIncludes(convertToMatcherArray(ts));
    }


    /**
     * Creates a matcher that checks a map. The checked map is expected to contain defined key-value pairs
     * in the listed order relatively to each other. Each one matcher of {@code matchers} describes a single
     * distinct key-value pair.
     *
     * @param matchers describe every one distinct key-value pair the map
     * @param <K>      is a type of map keys
     * @param <V>      is a type of map values
     * @param <T>      is a type of map
     * @return return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapIncludesInOrder(Matcher<Map.Entry<K, V>>... matchers) {
        return (Matcher<T>) new MapIncludesMatcherInOrder<>(matchers);
    }

    /**
     * Creates a matcher that checks a map. The checked map is expected to contain
     * defined key-value pairs. An order of items has no matter. Each one matcher of {@code matchers} describes
     * a single distinct key-value pair.
     *
     * @param matchers describe every one distinct key-value pair the map
     * @param <K>      is a type of map keys
     * @param <V>      is a type of map values
     * @param <T>      is a type of map
     * @return return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapIncludes(Matcher<Map.Entry<K, V>>... matchers) {
        return (Matcher<T>) new MapIncludesMatcherInAnyOrder<>(matchers);
    }

    boolean checkIterable(T toCheck) {
        var actualSize = size(toCheck);
        if (actualSize < matchers.length) {
            appendMismatchDescription(new DifferentSizeMismatch(matchers.length, actualSize));
            return false;
        }

        var toBeChecked = Lists.newArrayList(toCheck);

        return relatively ? checkRelatively(toBeChecked) :
                checkInAnyOrder(toBeChecked, true, true, matchers) == matchers.length;
    }

    private boolean checkRelatively(List<R> toCheck) {
        var alreadyChecked = new LinkedList<R>();
        var alreadyUsed = new LinkedList<Matcher<?>>();

        for (var m : matchers) {
            int index = -1;
            boolean found = false;
            for (var r : toCheck) {
                index++;
                if (m.matches(r)) {
                    found = true;
                    break;
                }
            }

            if (found) {
                var checked = toCheck.subList(0, index + 1);
                alreadyChecked.addAll(checked);
                toCheck.subList(0, index + 1).clear();
                alreadyUsed.addLast(m);
                continue;
            }

            if (alreadyChecked.size() > 0) {
                appendMismatchDescription(new OutOfItemsOrderMismatch(m,
                        alreadyChecked.getLast(),
                        alreadyChecked.size() - 1,
                        alreadyUsed.getLast()));
            } else {
                appendMismatchDescription(new ItemNotFoundMismatch(m));
            }
            return false;
        }

        return true;
    }

    private abstract static class IterableIncludesMatcher<R> extends SetOfObjectsIncludesMatcher<Iterable<R>, R, Iterable<R>> {

        @SafeVarargs
        private IterableIncludesMatcher(boolean relatively, Matcher<? super R>... matchers) {
            super(relatively, matchers);
        }

        @Override
        protected boolean featureMatches(Iterable<R> toMatch) {
            return checkIterable(toMatch);
        }
    }

    @Description("includes in following order: {" + MATCHERS + "}")
    private static class IterableIncludesMatcherInOrder<R> extends IterableIncludesMatcher<R> {

        @SafeVarargs
        private IterableIncludesMatcherInOrder(Matcher<? super R>... matchers) {
            super(true, matchers);
        }
    }

    private static class IterableIncludesMatcherInAnyOrder<R> extends IterableIncludesMatcher<R> {

        @SafeVarargs
        private IterableIncludesMatcherInAnyOrder(Matcher<? super R>... matchers) {
            super(false, matchers);
        }
    }

    private abstract static class ArrayIncludesMatcher<R> extends SetOfObjectsIncludesMatcher<R[], R, List<R>> {

        @SafeVarargs
        private ArrayIncludesMatcher(boolean relatively, Matcher<? super R>... matchers) {
            super(relatively, matchers);
        }

        @Override
        protected boolean featureMatches(R[] toMatch) {
            return checkIterable(asList(toMatch));
        }
    }

    @Description("includes in following order: {" + MATCHERS + "}")
    private static class ArrayIncludesMatcherInOrder<R> extends ArrayIncludesMatcher<R> {

        @SafeVarargs
        private ArrayIncludesMatcherInOrder(Matcher<? super R>... matchers) {
            super(true, matchers);
        }
    }

    private static class ArrayIncludesMatcherInAnyOrder<R> extends ArrayIncludesMatcher<R> {

        @SafeVarargs
        private ArrayIncludesMatcherInAnyOrder(Matcher<? super R>... matchers) {
            super(false, matchers);
        }
    }


    private abstract static class MapIncludesMatcher<K, V> extends SetOfObjectsIncludesMatcher<Map<K, V>, Map.Entry<K, V>, Set<Map.Entry<K, V>>> {

        @SafeVarargs
        private MapIncludesMatcher(boolean relatively, Matcher<Map.Entry<K, V>>... matchers) {
            super(relatively, matchers);
        }

        @Override
        protected boolean featureMatches(Map<K, V> toMatch) {
            return checkIterable(toMatch.entrySet());
        }
    }

    @Description("includes in following order: {" + MATCHERS + "}")
    private static class MapIncludesMatcherInOrder<K, V> extends MapIncludesMatcher<K, V> {

        @SafeVarargs
        private MapIncludesMatcherInOrder(Matcher<Map.Entry<K, V>>... matchers) {
            super(true, matchers);
        }
    }

    private static class MapIncludesMatcherInAnyOrder<K, V> extends MapIncludesMatcher<K, V> {

        @SafeVarargs
        private MapIncludesMatcherInAnyOrder(Matcher<Map.Entry<K, V>>... matchers) {
            super(false, matchers);
        }
    }
}
