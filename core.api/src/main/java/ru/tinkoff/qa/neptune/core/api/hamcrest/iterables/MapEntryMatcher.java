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

import static com.google.common.base.Preconditions.checkArgument;
import static org.hamcrest.Matchers.is;

/**
 * This matcher checks an entry of a map.
 *
 * @param <K> is type of a key
 * @param <V> is type of a value
 */
@Description("Key: {key}. Value: {value}")
public final class MapEntryMatcher<K, V> extends NeptuneFeatureMatcher<Map.Entry<K, V>> {

    @DescriptionFragment(value = "key", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super K> keyMatcher;

    @DescriptionFragment(value = "value", makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super V> valueMatcher;

    protected MapEntryMatcher(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        super(true);
        checkArgument(keyMatcher != null || valueMatcher != null,
                "Matcher of the key and/or matcher of the value should be defined");
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
    public static <K, V> Matcher<Map.Entry<K, V>> mapKeyValue(K k, V v) {
        return mapKeyValue(is(k), is(v));
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
    public static <K, V> Matcher<Map.Entry<K, V>> mapKeyValue(Matcher<? super K> keyMatcher, V v) {
        return mapKeyValue(keyMatcher, is(v));
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param k            expected key
     * @param valueMatcher s a criteria to match the value
     * @param <K>          is a type of the key
     * @param <V>          is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapKeyValue(K k, Matcher<? super V> valueMatcher) {
        return mapKeyValue(is(k), valueMatcher);
    }

    /**
     * Creates a matcher that check an entry of a map.
     *
     * @param keyMatcher   is a criteria to match the key
     * @param valueMatcher s a criteria to match the value
     * @param <K>          is a type of the key
     * @param <V>          is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapKeyValue(Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        return new MapEntryMatcher<>(keyMatcher, valueMatcher);
    }

    /**
     * Creates a matcher that check key of an entry of a map.
     *
     * @param k   expected key
     * @param <K> is a type of the key
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapKey(K k) {
        return mapKeyValue(is(k), null);
    }

    /**
     * Creates a matcher that check key of an entry of a map.
     *
     * @param keyMatcher is a criteria to match the key
     * @param <K>        is a type of the key
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapKey(Matcher<? super K> keyMatcher) {
        return mapKeyValue(keyMatcher, null);
    }

    /**
     * Creates a matcher that check value of an entry of a map.
     *
     * @param v   expected value
     * @param <V> is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapValue(V v) {
        return mapKeyValue(null, is(v));
    }

    /**
     * Creates a matcher that check value of an entry of a map.
     *
     * @param valueMatcher s a criteria to match the value
     * @param <V>          is a type of the value
     * @return a matcher
     */
    public static <K, V> Matcher<Map.Entry<K, V>> mapValue(Matcher<? super V> valueMatcher) {
        return mapKeyValue(null, valueMatcher);
    }


    @Override
    protected boolean featureMatches(Map.Entry<K, V> toMatch) {
        var matches = true;
        var key = toMatch.getKey();
        var value = toMatch.getValue();
        if (keyMatcher != null && !keyMatcher.matches(key)) {
            var d = new StringDescription();
            keyMatcher.describeMismatch(key, d);
            appendMismatchDescription(new KeyMismatch(keyMatcher, key, d));
            matches = false;
        }

        if (valueMatcher != null && !valueMatcher.matches(value)) {
            var d = new StringDescription();
            valueMatcher.describeMismatch(value, d);
            appendMismatchDescription(new ValueMismatch(valueMatcher, value, d));
            matches = false;
        }

        return matches;
    }
}
