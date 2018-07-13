package com.github.toy.constructor.selenium.hamcrest.matchers;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.StringDescription;
import org.hamcrest.internal.ReflectiveTypeFinder;

public abstract class TypeSafeDiagnosingMatcher<T> extends BaseMatcher<T> {

    private static final ReflectiveTypeFinder TYPE_FINDER = new ReflectiveTypeFinder("matchesSafely", 2, 0);
    private final Class<?> expectedType;
    private Description savedDescription;

    /**
     * Subclasses should implement this. The item will already have been checked
     * for the specific type and will never be null.
     */
    protected abstract boolean matchesSafely(T item, Description mismatchDescription);

    /**
     * Use this constructor if the subclass that implements <code>matchesSafely</code>
     * is <em>not</em> the class that binds &lt;T&gt; to a type.
     * @param expectedType The expectedType of the actual value.
     */
    protected TypeSafeDiagnosingMatcher(Class<?> expectedType) {
        this.expectedType = expectedType;
    }

    /**
     * Use this constructor if the subclass that implements <code>matchesSafely</code>
     * is <em>not</em> the class that binds &lt;T&gt; to a type.
     * @param typeFinder A type finder to extract the type
     */
    protected TypeSafeDiagnosingMatcher(ReflectiveTypeFinder typeFinder) {
        this.expectedType = typeFinder.findExpectedType(getClass());
    }

    /**
     * The default constructor for simple sub types
     */
    protected TypeSafeDiagnosingMatcher() {
        this(TYPE_FINDER);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final boolean matches(Object item) {
        boolean result;
        Description mismatchDescription = new StringDescription();
        if (item == null || !expectedType.isInstance(item)) {
            result = false;
            super.describeMismatch(item, mismatchDescription);
        }
        else {
            result = matchesSafely((T) item, mismatchDescription);
        }

        if (!result) {
            saveMismatchDescription(mismatchDescription);
        }
        return result;
    }

    @SuppressWarnings("unchecked")
    @Override
    public final void describeMismatch(Object item, Description mismatchDescription) {
        mismatchDescription.appendText(getSavedMismatchDescription().toString());
        saveMismatchDescription(null);
    }

    private Description getSavedMismatchDescription() {
        return savedDescription;
    }

    private void saveMismatchDescription(Description description) {
        this.savedDescription = description;
    }
}
