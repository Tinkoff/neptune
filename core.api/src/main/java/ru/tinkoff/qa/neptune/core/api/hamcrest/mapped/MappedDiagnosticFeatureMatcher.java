package ru.tinkoff.qa.neptune.core.api.hamcrest.mapped;

import org.hamcrest.Matcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NullValueMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.ObjectIsNotPresentMismatch;
import ru.tinkoff.qa.neptune.core.api.hamcrest.PropertyValueMismatch;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.core.api.steps.parameters.ParameterValueGetter;

import java.util.Map;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.stream.Collectors.toMap;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.MapEntryMatcher.entryKey;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.iterables.SetOfObjectsItemsMatcher.mapHasEntryValue;

/**
 * This matcher may be used when it is necessary to chek some object or set of objects of the same type and
 * when some property of an object is considered key and some property is considered value.
 *
 * @param <T> is a type of a checked object
 * @param <K> is a type of a key
 * @param <V> is a type of a value
 */
public abstract class MappedDiagnosticFeatureMatcher<T, K, V> extends NeptuneFeatureMatcher<T> {

    public static final String KEY_MATCHER_MASK = "key";
    public static final String VALUE_MATCHER_MASK = "value";

    @DescriptionFragment(value = KEY_MATCHER_MASK, makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super K> keyMatcher;

    @DescriptionFragment(value = VALUE_MATCHER_MASK, makeReadableBy = ParameterValueGetter.TranslatedDescriptionParameterValueGetter.class)
    private final Matcher<? super V> valueMatcher;

    @SafeVarargs
    protected MappedDiagnosticFeatureMatcher(boolean isNullSafe, Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher, Class<? extends T>... expectedTypes) {
        super(isNullSafe, expectedTypes);
        checkNotNull(keyMatcher);
        checkNotNull(valueMatcher);
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    protected MappedDiagnosticFeatureMatcher(boolean isNullSafe, Matcher<? super K> keyMatcher, Matcher<? super V> valueMatcher) {
        super(isNullSafe);
        checkNotNull(keyMatcher);
        checkNotNull(valueMatcher);
        this.keyMatcher = keyMatcher;
        this.valueMatcher = valueMatcher;
    }

    /**
     * Retrieves a target map from checked object.
     *
     * @param t is a checked object
     * @return a map for the checking
     */
    protected abstract Map<K, V> getMap(T t);

    /**
     * Creates description string when there is no key that meets defined criteria for keys
     *
     * @return string description
     */
    protected abstract String getDescriptionOnKeyAbsence();

    /**
     * Creates description string when value doesn't meet defined criteria
     *
     * @param k is a key value
     * @return string description
     */
    protected abstract String getDescriptionOnValueMismatch(K k);

    @Override
    protected boolean featureMatches(T toMatch) {
        var map = getMap(toMatch);

        if (map == null) {
            appendMismatchDescription(new NullValueMismatch());
            return false;
        }

        var entryMatcher = entryKey(keyMatcher);
        var found = map
                .entrySet()
                .stream()
                .filter(entryMatcher::matches)
                .collect(toMap(Map.Entry::getKey, Map.Entry::getValue));

        if (found.size() == 0) {
            appendMismatchDescription(new ObjectIsNotPresentMismatch(getDescriptionOnKeyAbsence(), keyMatcher));
            return false;
        }

        if (!mapHasEntryValue(valueMatcher).matches(found)) {
            found.forEach((key, value) -> {
                if (!valueMatcher.matches(value)) {
                    appendMismatchDescription(new PropertyValueMismatch(getDescriptionOnValueMismatch(key),
                            value,
                            valueMatcher));
                }
            });
            return false;
        }

        return true;
    }
}
