package ru.tinkoff.qa.neptune.core.api.hamcrest.pojo;

import org.hamcrest.Matcher;
import org.hamcrest.StringDescription;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllMatchersParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.lang.reflect.Modifier.isPublic;
import static java.lang.reflect.Modifier.isStatic;
import static java.util.Arrays.stream;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.hamcrest.Matchers.equalTo;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;

/**
 * Is the matcher that invokes defined public and non-static get-method without parameters and checks
 * returned value. This matcher is recommended to be used to check POJOs.
 *
 * @param <T> is a type of checked value
 * @param <R> is a type of a value which is expected to be returned by the getter.
 */
@Description("Public getter '{getter}' is expected to return a value: {criteria}")
public final class PojoGetterReturnsMatcher<T, R> extends NeptuneFeatureMatcher<T> {

    @DescriptionFragment("getter")
    private final String getterName;

    @DescriptionFragment(value = "criteria", makeReadableBy = AllMatchersParameterValueGetter.class)
    private final Matcher<? super R>[] valueMatchers;

    @SafeVarargs
    private PojoGetterReturnsMatcher(String getterName, Matcher<? super R>... valueMatchers) {
        super(true);
        checkArgument(isNotBlank(getterName), "Name of the getter-method should be defined");
        checkNotNull(valueMatchers);
        checkArgument(valueMatchers.length > 0, "At least one matcher should be defined");
        this.getterName = getterName;
        this.valueMatchers = valueMatchers;
    }

    /**
     * Creates a matcher that invokes defined public and non-static get-method without parameters and checks
     * returned value. It is expected that returned value meets every defined criteria. This matcher is recommended
     * to be used to check POJOs.
     *
     * @param getterName    is the name of the getter-method
     * @param valueMatchers criteria to check returned value
     * @param <T>           is a type of checked value
     * @param <R>           is a type of a value which is expected to be returned by the getter.
     * @return a matcher
     */
    @SafeVarargs
    public static <T, R> Matcher<T> getterReturns(String getterName, Matcher<? super R>... valueMatchers) {
        return new PojoGetterReturnsMatcher<>(getterName, valueMatchers);
    }

    /**
     * Creates a matcher that invokes defined public and non-static get-method without parameters and checks
     * returned value. It is expected that returned value meets defined criteria. This matcher is recommended
     * to be used to check POJOs.
     *
     * @param getterName   is the name of the getter-method
     * @param valueMatcher criteria to check returned value
     * @param <T>          is a type of checked value
     * @param <R>          is a type of a value which is expected to be returned by the getter.
     * @return a matcher
     */
    @SuppressWarnings("unchecked")
    public static <T, R> Matcher<T> getterReturns(String getterName, Matcher<? super R> valueMatcher) {
        return getterReturns(getterName, new Matcher[]{valueMatcher});
    }

    /**
     * Creates a matcher that invokes defined public and non-static get-method without parameters and checks
     * returned value. It is expected that returned value equals to defined by {@code value}. This matcher is
     * recommended to be used to check POJOs.
     *
     * @param getterName is the name of the getter-method
     * @param value      is expected value
     * @param <T>        is a type of checked value
     * @param <R>        is a type of a value which is expected to be returned by the getter.
     * @return a matcher
     */
    public static <T, R> Matcher<T> getterReturns(String getterName, R value) {
        return getterReturns(getterName, equalTo(value));
    }

    @Override
    protected boolean featureMatches(T toMatch) {
        var clazz = toMatch.getClass();
        var currentClass = clazz;
        Method m = null;

        while (!currentClass.equals(Object.class) && m == null) {
            m = stream(currentClass.getDeclaredMethods())
                    .filter(method -> {
                        var modifiers = method.getModifiers();
                        return method.getName().equals(getterName)
                                && !isStatic(modifiers)
                                && isPublic(modifiers);
                    })
                    .findFirst()
                    .orElse(null);
            currentClass = currentClass.getSuperclass();
        }

        if (m == null) {
            appendMismatchDescription(new NoSuchMethodMismatch(clazz, getterName));
            return false;
        }

        var all = all(valueMatchers);
        try {
            if (!all.matches(m.invoke(toMatch))) {
                var d = new StringDescription();
                all.describeMismatch(toMatch, d);
                appendMismatchDescription(new ReturnedValueMismatch(d));
                return false;
            }
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }

        return true;
    }
}
