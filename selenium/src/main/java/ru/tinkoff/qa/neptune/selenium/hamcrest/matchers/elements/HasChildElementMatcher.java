package ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.elements;

import org.hamcrest.Matcher;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;
import ru.tinkoff.qa.neptune.core.api.hamcrest.AllMatchersParameterValueGetter;
import ru.tinkoff.qa.neptune.core.api.hamcrest.NeptuneFeatureMatcher;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.Description;
import ru.tinkoff.qa.neptune.core.api.steps.annotations.DescriptionFragment;
import ru.tinkoff.qa.neptune.selenium.api.widget.Widget;
import ru.tinkoff.qa.neptune.selenium.functions.searching.SearchSupplier;
import ru.tinkoff.qa.neptune.selenium.hamcrest.matchers.descriptions.ElementNotFoundMismatch;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static java.time.Duration.ofMillis;
import static java.util.Objects.nonNull;
import static ru.tinkoff.qa.neptune.core.api.hamcrest.common.all.AllCriteriaMatcher.all;
import static ru.tinkoff.qa.neptune.core.api.steps.SequentialGetStepSupplier.turnReportingOff;

@Description("has child element {search} {elementMatchers}")
public final class HasChildElementMatcher<T extends SearchContext> extends NeptuneFeatureMatcher<SearchContext> {

    @DescriptionFragment("search")
    private final SearchSupplier<T> search;

    @DescriptionFragment(value = "elementMatchers", makeReadableBy = AllMatchersParameterValueGetter.class)
    private final Matcher<T>[] elementMatchers;

    private HasChildElementMatcher(SearchSupplier<T> search, Matcher<T>[] elementMatchers) {
        super(true, WebElement.class, Widget.class);
        checkArgument(nonNull(search), "The way to find child element should be defined");
        checkNotNull(elementMatchers);
        this.elementMatchers = elementMatchers;
        this.search = turnReportingOff(search.clone().timeOut(ofMillis(0)));
    }

    /**
     * Creates a matcher that checks a child page element.
     *
     * @param search          is the way to find desired child element.
     * @param elementMatchers criteria which the child element is expected to meet.
     * @param <T>             is the type of the child element
     * @return created instance of {@link HasChildElementMatcher}
     */
    @SafeVarargs
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElement(SearchSupplier<T> search,
                                                                                   Matcher<T>... elementMatchers) {
        return new HasChildElementMatcher<>(search, elementMatchers);
    }

    /**
     * Creates a matcher that checks a child page element.
     *
     * @param search         is the way to find desired child element.
     * @param elementMatcher criteria which the child element is expected to meet.
     * @param <T>            is the type of the child element
     * @return created instance of {@link HasChildElementMatcher}
     */
    @SuppressWarnings("unchecked")
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElement(SearchSupplier<T> search,
                                                                                   Matcher<T> elementMatcher) {
        return hasChildElement(search, new Matcher[]{elementMatcher});
    }

    /**
     * Creates a matcher that checks a child page element.
     *
     * @param search is the way to find desired child element.
     * @param <T>    is the type of the child element
     * @return created instance of {@link HasChildElementMatcher}
     */
    @SuppressWarnings("unchecked")
    public static <T extends SearchContext> Matcher<SearchContext> hasChildElement(SearchSupplier<T> search) {
        return hasChildElement(search, new Matcher[]{});
    }

    @Override
    protected boolean featureMatches(SearchContext toMatch) {
        try {
            var found = search.get().apply(toMatch);

            if (elementMatchers.length > 0) {
                var m = all(elementMatchers);

                if (!m.matches(found)) {
                    appendMismatchDescription(m, found);
                    return false;
                }
            }
            return true;
        } catch (NoSuchElementException e) {
            appendMismatchDescription(new ElementNotFoundMismatch(search));
            return false;
        }
    }
}
