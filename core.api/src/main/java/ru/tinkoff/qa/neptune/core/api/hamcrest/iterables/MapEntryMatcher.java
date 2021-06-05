package ru.tinkoff.qa.neptune.core.api.hamcrest.iterables;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.KeyMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.descriptions.ValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.Map;

import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.AnyThingMatcher.anything;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;

/**
 * This matcher checks an entry of a map.
 *
 * @param <K> is type of a key
 * @param <V> is type of a value
 */
@Description("Key: {key} Value: {value}")
public final class MapEntryMatcher<K, V> extends NeptuneFeatureMatcher<Map.Entry<K, V>> {

    @DescriptionFragment(value = "key", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super K> keyMatcher;

    @DescriptionFragment(value = "value", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super V> valueMatcher;

    protected MapEntryMatcher(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        super(true);
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param k   expected key
     * @param v   expected value
     * @param <K> is a type of the key
     * @param <V> is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapEntry(K k, V v) {
        return mapEntry(equalTo(k), equalTo(v));
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param keyMatcher is a criteria to match the key
     * @param v          expected value
     * @param <K>        is a type of the key
     * @param <V>        is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapEntry(Matcher<? super K> keyMatcher, V v) {
        return mapEntry(keyMatcher, equalTo(v));
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param k            expected key
     * @param valueMatcher is a criteria to match the value
     * @param <K>          is a type of the key
     * @param <V>          is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapEntry(K k, Matcher<? super V> valueMatcher) {
        return mapEntry(equalTo(k), valueMatcher);
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param keyMatcher   is a criteria to match the key
     * @param valueMatcher is a criteria to match the value
     * @param <K>          is a type of the key
     * @param <V>          is a type of the value
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Matcher<Map.Entry<K, V>> mapEntry(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        return mapEntry(keyMatcher, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param keyMatcher   is a criteria to match the key
     * @param valueMatcher is a criteria to match the value
     * @param <K>          is a type of the key
     * @param <V>          is a type of the value
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V> Matcher<Map.Entry<K, V>> mapEntry(Matcher<? super K> keyMatcher, Matcher<? super V>... valueMatcher) {
        return new MapEntryMatcher<>(keyMatcher, all(valueMatcher));
    }

    /**
     * Creates a matcher that check key of an entry of a map.
     *
     * @param k   expected key
     * @param <K> is a type of the key
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> entryKey(K k) {
        return entryKey(equalTo(k));
    }

    /**
     * Creates a matcher that check key of an entry of a map.
     *
     * @param keyMatcher a criteria to match the key
     * @param <K>        is a type of the key
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Matcher<Map.Entry<K, V>> entryKey(Matcher<? super K> keyMatcher) {
        return entryKey(new Matcher[]{keyMatcher});
    }

    /**
     * Creates a matcher that check key of an entry of a map.
     *
     * @param keyMatchers criteria to match the key
     * @param <K>         is a type of the key
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V> Matcher<Map.Entry<K, V>> entryKey(Matcher<? super K>... keyMatchers) {
        return mapEntry(all(keyMatchers), anything());
    }

    /**
     * Creates a matcher that check value of an entry of a map.
     *
     * @param v   expected value
     * @param <V> is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> entryValue(V v) {
        return entryValue(equalTo(v));
    }

    /**
     * Creates a matcher that check value of an entry of a map.
     *
     * @param valueMatchers criteria to match the value
     * @param <V>           is a type of the value
     * @return a matcher
     */
    @SafeVarargs
    public static <K, V> Matcher<Map.Entry<K, V>> entryValue(Matcher<? super V>... valueMatchers) {
        return mapEntry(anything(), valueMatchers);
    }

    /**
     * Creates a matcher that check value of an entry of a map.
     *
     * @param valueMatcher a criteria to match the value
     * @param <V>          is a type of the value
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <K, V> Matcher<Map.Entry<K, V>> entryValue(Matcher<? super V> valueMatcher) {
        return entryValue(new Matcher[]{valueMatcher});
    }


    @Override
    protected boolean featureMatches(Map.Entry<K, V> toMatch) {
        var matches = true;
        var key = toMatch.getKey();
        var value = toMatch.getValue();
        if (!keyMatcher.matches(key)) {
            var d = new StringDescription();
            keyMatcher.describeMismatch(key, d);
            appendMismatchDescription(new KeyMismatch(key, d));
            matches = false;
        }

        if (!valueMatcher.matches(value)) {
            var d = new StringDescription();
            valueMatcher.describeMismatch(value, d);
            appendMismatchDescription(new ValueMismatch(value, d));
            matches = false;
        }

        return matches;
    }
}
