package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import com.google.common.collect.Lists;
import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.ObjectIsNotPresentMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.Count;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.Item;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.List;
import java.util.Map;
import java.util.Set;

import static java.util.Arrays.asList;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThanOrEqualTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.AbstractSetOfObjectsMatcher.MATCHERS;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.*;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.COUNT;

@Description("has item(s): {" + MATCHERS + "}. Expected count: {" + COUNT + "}")
public abstract class SetOfObjectsItemsMatcher<S, R, T extends Iterable<R>> extends AbstractSetOfObjectsMatcher<S, R> {

    static final String COUNT = "count";

    @DescriptionFragment(value = COUNT, makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<Integer> countMatcher;

    @SafeVarargs
    private SetOfObjectsItemsMatcher(Matcher<Integer> countMatcher, Matcher<? super R>... matchers) {
        super(true, matchers);
        this.countMatcher = countMatcher;
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has at least
     * one item that meets every defined criteria.
     *
     * @param matchers criteria for an item
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItem(Matcher<? super T>... matchers) {
        return iterableHasItems(greaterThanOrEqualTo(1), matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has at least
     * one item that meets defined criteria.
     *
     * @param matcher criteria for an item
     * @param <T>     is a type of an item of iterable
     * @param <R>     is a type of the checked iterable
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItem(Matcher<? super T> matcher) {
        return iterableHasItem(new Matcher[]{matcher});
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has at least
     * one item which equal to defined one.
     *
     * @param expected is an item that is expected to be contained
     * @param <T>      is a type of an item of iterable
     * @param <R>      is a type of the checked iterable
     * @return a matcher
     */

    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItem(T expected) {
        return iterableHasItem(equalTo(expected));
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has some items
     * that meet every defined criteria. Expected count of such items also described by criteria.
     *
     * @param countMatcher criteria for count of items
     * @param matchers     criteria for an item
     * @param <T>          is a type of an item of iterable
     * @param <R>          is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItems(Matcher<Integer> countMatcher,
                                                                         Matcher<? super T>... matchers) {
        return (Matcher<R>) new IterableHasItemsMatcher<>(countMatcher, matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has some items
     * that meet defined criteria. Expected count of such items also described by criteria.
     *
     * @param countMatcher criteria for count of items
     * @param matcher      criteria for an item
     * @param <T>          is a type of an item of iterable
     * @param <R>          is a type of the checked iterable
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItems(Matcher<Integer> countMatcher,
                                                                         Matcher<? super T> matcher) {
        return iterableHasItems(countMatcher, new Matcher[]{matcher});
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has items
     * which equal to defined one. Expected count of such items also described by criteria.
     *
     * @param countMatcher criteria for count of items
     * @param expected     is an item that is expected to be contained
     * @param <T>          is a type of an item of iterable
     * @param <R>          is a type of the checked iterable
     * @return a matcher
     */
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItems(Matcher<Integer> countMatcher,
                                                                         T expected) {
        return iterableHasItems(countMatcher, equalTo(expected));
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has some items
     * that meet every defined criteria. Expected count of such items is strictly defined.
     *
     * @param expectedCount expected count of items
     * @param matchers      criteria for an item
     * @param <T>           is a type of an item of iterable
     * @param <R>           is a type of the checked iterable
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItems(int expectedCount,
                                                                         Matcher<? super T>... matchers) {
        return iterableHasItems(equalTo(expectedCount), matchers);
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has some items
     * that meet defined criteria. Expected count of such items is strictly defined.
     *
     * @param expectedCount expected count of items
     * @param matcher       criteria for an item
     * @param <T>           is a type of an item of iterable
     * @param <R>           is a type of the checked iterable
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItems(int expectedCount,
                                                                         Matcher<? super T> matcher) {
        return iterableHasItems(expectedCount, new Matcher[]{matcher});
    }

    /**
     * Creates a matcher that checks an {@link Iterable}. It is expected that checked iterable has items
     * which equal to defined one.  Expected count of such items is strictly defined.
     *
     * @param expectedCount expected count of items
     * @param expected      is an item that is expected to be contained
     * @param <T>           is a type of an item of iterable
     * @param <R>           is a type of the checked iterable
     * @return a matcher
     */
    public static <T, R extends Iterable<T>> Matcher<R> iterableHasItems(int expectedCount,
                                                                         T expected) {
        return iterableHasItems(expectedCount, equalTo(expected));
    }


    /**
     * Creates a matcher that checks an array. It is expected that checked array has at least
     * one item that meets every defined criteria.
     *
     * @param matchers criteria for an item
     * @param <T>      is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayHasItem(Matcher<? super T>... matchers) {
        return arrayHasItems(greaterThanOrEqualTo(1), matchers);
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has at least
     * one item that meets defined criteria.
     *
     * @param matcher criteria for an item
     * @param <T>     is a type of an item of array
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T[]> arrayHasItem(Matcher<? super T> matcher) {
        return arrayHasItem(new Matcher[]{matcher});
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has at least
     * one item which equal to defined one.
     *
     * @param expected is an item that is expected to be contained
     * @param <T>      is a type of an item of array
     * @return a matcher
     */
    public static <T> Matcher<T[]> arrayHasItem(T expected) {
        return arrayHasItem(equalTo(expected));
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has some items
     * that meet every defined criteria. Expected count of such items also described by criteria.
     *
     * @param countMatcher criteria for count of items
     * @param matchers     criteria for an item
     * @param <T>          is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayHasItems(Matcher<Integer> countMatcher,
                                                 Matcher<? super T>... matchers) {
        return new ArrayHasItemsMatcher<>(countMatcher, matchers);
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has some items
     * that meet defined criteria. Expected count of such items also described by criteria.
     *
     * @param countMatcher criteria for count of items
     * @param matcher      criteria for an item
     * @param <T>          is a type of an item of array
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T[]> arrayHasItems(Matcher<Integer> countMatcher,
                                                 Matcher<? super T> matcher) {
        return arrayHasItems(countMatcher, new Matcher[]{matcher});
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has items
     * which equal to defined one. Expected count of such items also described by criteria.
     *
     * @param countMatcher criteria for count of items
     * @param expected     is an item that is expected to be contained
     * @param <T>          is a type of an item of array
     * @return a matcher
     */
    public static <T> Matcher<T[]> arrayHasItems(Matcher<Integer> countMatcher,
                                                 T expected) {
        return arrayHasItems(countMatcher, equalTo(expected));
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has some items
     * that meet every defined criteria. Expected count of such items is strictly defined.
     *
     * @param expectedCount expected count of items
     * @param matchers      criteria for an item
     * @param <T>           is a type of an item of array
     * @return a matcher
     */
    @SafeVarargs
    public static <T> Matcher<T[]> arrayHasItems(int expectedCount,
                                                 Matcher<? super T>... matchers) {
        return arrayHasItems(equalTo(expectedCount), matchers);
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has some items
     * that meet defined criteria. Expected count of such items is strictly defined.
     *
     * @param expectedCount expected count of items
     * @param matcher       criteria for an item
     * @param <T>           is a type of an item of array
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T> Matcher<T[]> arrayHasItems(int expectedCount,
                                                 Matcher<? super T> matcher) {
        return arrayHasItems(expectedCount, new Matcher[]{matcher});
    }

    /**
     * Creates a matcher that checks an array. It is expected that checked array has items
     * which equal to defined one.  Expected count of such items is strictly defined.
     *
     * @param expectedCount expected count of items
     * @param expected      is an item that is expected to be contained
     * @param <T>           is a type of an item of array
     * @return a matcher
     */
    public static <T> Matcher<T[]> arrayHasItems(int expectedCount,
                                                 T expected) {
        return arrayHasItems(expectedCount, equalTo(expected));
    }


    /**
     * Creates a matcher that checks a map. It is expected that checked map has at least
     * one entry that meets defined criteria.
     *
     * @param keyMatcher    criteria for keys
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntry(Matcher<? super K> keyMatcher,
                                                                     Matcher<? super V>... valueMatchers) {
        return mapHasEntries(greaterThanOrEqualTo(1), keyMatcher, valueMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has at least
     * one entry that meets defined criteria.
     *
     * @param keyMatcher   criteria for keys
     * @param valueMatcher criteria for values
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntry(Matcher<? super K> keyMatcher,
                                                                     Matcher<? super V> valueMatcher) {
        return mapHasEntry(keyMatcher, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has an entry with defined key
     * and value that meets every defined criteria.
     *
     * @param key           is the expected key
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntry(K key,
                                                                     Matcher<? super V>... valueMatchers) {
        return mapHasEntries(1, equalTo(key), valueMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has an entry with defined key
     * and value that meets defined criteria.
     *
     * @param key          is the expected key
     * @param valueMatcher criteria for values
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntry(K key,
                                                                     Matcher<? super V> valueMatcher) {
        return mapHasEntry(key, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has an entry
     * with defined key and value.
     *
     * @param key   is an entry key
     * @param value is an entry value
     * @param <K>   is a type of keys
     * @param <V>   is a type of values
     * @param <T>   is a type of map
     * @return a matcher
     */
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntry(K key,
                                                                     V value) {
        return mapHasEntries(1, equalTo(key), equalTo(value));
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries
     * that meet defined criteria. Expected count of such entries also described by criteria.
     *
     * @param countMatcher  criteria for count of entries
     * @param keyMatcher    criteria for keys
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntries(Matcher<Integer> countMatcher,
                                                                       Matcher<? super K> keyMatcher,
                                                                       Matcher<? super V>... valueMatchers) {
        return (Matcher<T>) new MapHasItemsMatcher<>(countMatcher, mapEntry(keyMatcher, valueMatchers));
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries
     * that meet defined criteria. Expected count of such entries also described by criteria.
     *
     * @param countMatcher criteria for count of entries
     * @param keyMatcher   criteria for keys
     * @param valueMatcher criteria for values
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntries(Matcher<Integer> countMatcher,
                                                                       Matcher<? super K> keyMatcher,
                                                                       Matcher<? super V> valueMatcher) {
        return mapHasEntries(countMatcher, keyMatcher, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries
     * that meet defined criteria. Expected count of such entries is strictly defined.
     *
     * @param expectedCount expected count of entries
     * @param keyMatcher    criteria for keys
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntries(int expectedCount,
                                                                       Matcher<? super K> keyMatcher,
                                                                       Matcher<? super V>... valueMatchers) {
        return mapHasEntries(equalTo(expectedCount), keyMatcher, valueMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries
     * that meet defined criteria. Expected count of such entries is strictly defined.
     *
     * @param expectedCount expected count of entries
     * @param keyMatcher    criteria for keys
     * @param valueMatcher  criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntries(int expectedCount,
                                                                       Matcher<? super K> keyMatcher,
                                                                       Matcher<? super V> valueMatcher) {
        return mapHasEntries(expectedCount, keyMatcher, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * keys meet every defined criteria. Expected count of such keys also described by criteria.
     *
     * @param countMatcher criteria for count of entries
     * @param keyMatchers  criteria for keys
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKeys(Matcher<Integer> countMatcher,
                                                                         Matcher<? super K>... keyMatchers) {
        return (Matcher<T>) new MapHasItemsMatcher<>(countMatcher, entryKey(keyMatchers));
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * keys meet defined criteria. Expected count of such keys also described by criteria.
     *
     * @param countMatcher criteria for count of entries
     * @param keyMatcher   criteria for keys
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKeys(Matcher<Integer> countMatcher,
                                                                         Matcher<? super K> keyMatcher) {
        return mapHasEntryKeys(countMatcher, new Matcher[]{keyMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * keys meet every defined criteria. Expected count of such keys is strictly defined.
     *
     * @param expectedCount criteria for count of entries
     * @param keyMatchers   criteria for keys
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKeys(int expectedCount,
                                                                         Matcher<? super K>... keyMatchers) {
        return mapHasEntryKeys(equalTo(expectedCount), keyMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * keys meet defined criteria. Expected count of such keys is strictly defined.
     *
     * @param expectedCount criteria for count of entries
     * @param keyMatcher    criteria for keys
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKeys(int expectedCount,
                                                                         Matcher<? super K> keyMatcher) {
        return mapHasEntryKeys(expectedCount, new Matcher[]{keyMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has at least
     * one entry whose key meets every defined criteria.
     *
     * @param keyMatchers criteria for keys
     * @param <K>         is a type of keys
     * @param <V>         is a type of values
     * @param <T>         is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKey(Matcher<? super K>... keyMatchers) {
        return mapHasEntryKeys(greaterThanOrEqualTo(1), keyMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has at least
     * one entry whose key meets defined criteria.
     *
     * @param keyMatcher criteria for keys
     * @param <K>        is a type of keys
     * @param <V>        is a type of values
     * @param <T>        is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKey(Matcher<? super K> keyMatcher) {
        return mapHasEntryKey(new Matcher[]{keyMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has one entry
     * with defined key.
     *
     * @param key is an entry key
     * @param <K> is a type of keys
     * @param <V> is a type of values
     * @param <T> is a type of map
     * @return a matcher
     */
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryKey(K key) {
        return mapHasEntryKeys(1, equalTo(key));
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * values meet every defined criteria. Expected count of such values also described by criteria.
     *
     * @param countMatcher  criteria for count of entries
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValues(Matcher<Integer> countMatcher,
                                                                           Matcher<? super V>... valueMatchers) {
        return (Matcher<T>) new MapHasItemsMatcher<>(countMatcher, entryValue(valueMatchers));
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * values meet defined criteria. Expected count of such values also described by criteria.
     *
     * @param countMatcher criteria for count of entries
     * @param valueMatcher criteria for values
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValues(Matcher<Integer> countMatcher,
                                                                           Matcher<? super V> valueMatcher) {
        return mapHasEntryValues(countMatcher, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * values meet every defined criteria. Expected count of such values is strictly defined.
     *
     * @param expectedCount criteria for count of entries
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValues(int expectedCount,
                                                                           Matcher<? super V>... valueMatchers) {
        return mapHasEntryValues(equalTo(expectedCount), valueMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries whose
     * values meet every defined criteria. Expected count of such values is strictly defined.
     *
     * @param expectedCount criteria for count of entries
     * @param valueMatcher  criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValues(int expectedCount,
                                                                           Matcher<? super V> valueMatcher) {
        return mapHasEntryValues(expectedCount, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has some entries with defined value.
     * Expected count of such values is strictly defined.
     *
     * @param expectedCount criteria for count of entries
     * @param value         is an entry value
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValues(int expectedCount,
                                                                           V value) {
        return mapHasEntryValues(equalTo(expectedCount), equalTo(value));
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has at least
     * one entry whose value meets every defined criteria.
     *
     * @param valueMatchers criteria for values
     * @param <K>           is a type of keys
     * @param <V>           is a type of values
     * @param <T>           is a type of map
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValue(Matcher<? super V>... valueMatchers) {
        return mapHasEntryValues(greaterThanOrEqualTo(1), valueMatchers);
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has at least
     * one entry whose value meets defined criteria.
     *
     * @param valueMatcher criteria for values
     * @param <K>          is a type of keys
     * @param <V>          is a type of values
     * @param <T>          is a type of map
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValue(Matcher<? super V> valueMatcher) {
        return mapHasEntryValue(new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that checks a map. It is expected that checked map has one entry
     * with defined value.
     *
     * @param value is an entry value
     * @param <K>   is a type of keys
     * @param <V>   is a type of values
     * @param <T>   is a type of map
     * @return a matcher
     */
    public static <K, V, T extends Map<K, V>> Matcher<T> mapHasEntryValue(V value) {
        return mapHasEntryValue(equalTo(value));
    }


    boolean itemMatches(T toCheck) {
        var all = all(matchers);
        var count = checkInAnyOrder(Lists.newArrayList(toCheck), false, false, all);
        if (count == 0) {
            appendMismatchDescription(new ObjectIsNotPresentMismatch(new Item(), all));
            return false;
        }

        if (!countMatcher.matches(count)) {
            appendMismatchDescription(new PropertyValueMismatch(new Count(), count, countMatcher));
            return false;
        }

        return true;
    }

    private static class IterableHasItemsMatcher<R> extends SetOfObjectsItemsMatcher<Iterable<R>, R, Iterable<R>> {

        @SafeVarargs
        private IterableHasItemsMatcher(Matcher<Integer> countMatcher, Matcher<? super R>... matchers) {
            super(countMatcher, matchers);
        }

        @Override
        protected boolean featureMatches(Iterable<R> toMatch) {
            return itemMatches(toMatch);
        }
    }

    private static class ArrayHasItemsMatcher<R> extends SetOfObjectsItemsMatcher<R[], R, List<R>> {

        @SafeVarargs
        private ArrayHasItemsMatcher(Matcher<Integer> countMatcher, Matcher<? super R>... matchers) {
            super(countMatcher, matchers);
        }

        @Override
        protected boolean featureMatches(R[] toMatch) {
            return itemMatches(asList(toMatch));
        }
    }

    @Description("has entry(es): {" + MATCHERS + "}. Expected count: {" + COUNT + "}")
    private static class MapHasItemsMatcher<K, V> extends SetOfObjectsItemsMatcher<Map<K, V>, Map.Entry<K, V>, Set<Map.Entry<K, V>>> {

        private MapHasItemsMatcher(Matcher<Integer> countMatcher, Matcher<Map.Entry<K, V>> entryMatcher) {
            super(countMatcher, entryMatcher);
        }

        @Override
        protected boolean featureMatches(Map<K, V> toMatch) {
            return itemMatches(toMatch.entrySet());
        }
    }
}
